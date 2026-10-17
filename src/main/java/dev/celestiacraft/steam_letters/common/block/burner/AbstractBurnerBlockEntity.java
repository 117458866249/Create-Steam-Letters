package dev.celestiacraft.steam_letters.common.block.burner;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.api.machine.ISteamEngineBlock;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.api.machine.heat.IHeatSource;
import dev.celestiacraft.steam_letters.client.lang.SlLang;
import dev.celestiacraft.steam_letters.config.common.SteamBoilerConfig;
import dev.celestiacraft.steam_letters.config.common.SteamBurnerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Locale;

/**
 * 燃烧室的公共实现: 维护自身热量, 供正上方锅炉读取.
 * <p>
 * 升温 / 降温速率按「最高热量的百分比每秒」计算, 燃料只决定燃烧时长, 不影响升温速度;
 * 热量始终钳位在 [0, maxHeat], 燃料耗尽后立刻开始降温.
 */
public abstract class AbstractBurnerBlockEntity extends SmartBlockEntity implements IHeatSource, IHaveGoggleInformation {
	private static final String NBT_HEAT = "Heat";
	private static final String NBT_BURN_TIME = "BurnTime";

	/**
	 * 内置青铜燃烧室的默认热量上限.
	 */
	public static final double DEFAULT_MAX_HEAT = 100.0D;

	protected final SteamEngineSpec spec;

	/**
	 * 当前热量, 允许小数 —— 每 tick 的增量通常远小于 1, 取整会直接丢掉全部产出.
	 */
	protected double heat;

	/**
	 * 当前这份燃料剩余的燃烧时间 (tick), 0 表示没有燃料在燃烧.
	 */
	protected int burnTime;

	/**
	 * 上次同步给客户端的数值, 用来避免每 10 tick 无条件发包.
	 */
	private double syncedHeat = Double.NaN;
	private int syncedBurnTime = -1;

	protected AbstractBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		spec = ((ISteamEngineBlock) state.getBlock()).getEngineSpec();
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
	}

	@Override
	public void tick() {
		super.tick();
		if (level == null || level.isClientSide()) {
			return;
		}

		boolean burning = tickFuel();
		tickHeat(burning);
		updateLitState(burning);
	}

	/**
	 * 推进一 tick 的燃料消耗.
	 *
	 * @return 本 tick 是否有燃料在燃烧
	 */
	protected abstract boolean tickFuel();

	private void tickHeat(boolean burning) {
		double maxHeat = getMaxHeat();
		if (maxHeat <= 0.0D) {
			heat = 0.0D;
			return;
		}

		double percentPerSecond = burning
				? SteamBurnerConfig.HEAT_GAIN_PERCENT_PER_SECOND.get()
				: SteamBurnerConfig.HEAT_LOSS_PERCENT_PER_SECOND.get();
		double delta = maxHeat * percentPerSecond / 100.0D / 20.0D;

		heat = burning ? heat + delta : heat - delta;
		heat = Mth.clamp(heat, 0.0D, maxHeat);
	}

	@Override
	public double getHeat() {
		return heat;
	}

	@Override
	public double getMaxHeat() {
		double configured = spec.getMaxHeat();
		return configured > 0.0D ? configured : DEFAULT_MAX_HEAT;
	}

	@Override
	public boolean isBurningFuel() {
		return burnTime > 0;
	}

	/**
	 * 当前热量占最高热量的百分比 (0-100).
	 */
	public double getHeatPercent() {
		double maxHeat = getMaxHeat();
		if (maxHeat <= 0.0D) {
			return 0.0D;
		}
		return Mth.clamp(heat / maxHeat * 100.0D, 0.0D, 100.0D);
	}

	/**
	 * 热量与燃烧剩余时间每 10 tick 向客户端同步一次, 供护目镜显示.
	 * <p>
	 * 只在数值真正变化时发包, 闲置的燃烧室不产生额外网络流量.
	 */
	@Override
	public void lazyTick() {
		super.lazyTick();
		if (level == null || level.isClientSide()) {
			return;
		}
		if (heat == syncedHeat && burnTime == syncedBurnTime) {
			return;
		}
		syncedHeat = heat;
		syncedBurnTime = burnTime;
		sendData();
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		SlLang.translate(SlTranKeys.GOGGLE_STEAM_BURNER)
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip);

		addGoggleLine(tooltip, SlTranKeys.GOGGLE_STEAM_BURNER_TEMPERATURE, Component.literal(
				formatNumber(heat) + "%" + " / " + formatNumber(getMaxHeat()) + "%"
		).withStyle(ChatFormatting.GOLD));

		addGoggleLine(tooltip, SlTranKeys.GOGGLE_STEAM_BURNER_FUEL_TIME, Component.literal(
				formatSeconds(burnTime)
		).withStyle(burnTime > 0 ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));

		double efficiency = SteamBoilerConfig.getEfficiency(getHeatPercent());
		addGoggleLine(tooltip, SlTranKeys.GOGGLE_STEAM_BURNER_EFFICIENCY, Component.literal(
				formatPercent(efficiency)
		).withStyle(efficiency > 0.0D ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));

		return true;
	}

	private static void addGoggleLine(List<Component> tooltip, String langKey, Component value) {
		SlLang.translate(langKey)
				.style(ChatFormatting.GRAY)
				.text(ChatFormatting.DARK_GRAY, ": ")
				.add(value)
				.forGoggles(tooltip, 1);
	}

	private static String formatNumber(double value) {
		return String.format(Locale.ROOT, "%.1f", value);
	}

	private static String formatSeconds(int ticks) {
		return String.format(Locale.ROOT, "%.1fs", ticks / 20.0D);
	}

	private static String formatPercent(double ratio) {
		return String.format(Locale.ROOT, "%.0f%%", ratio * 100.0D);
	}

	private void updateLitState(boolean lit) {
		if (level == null) {
			return;
		}
		BlockState state = getBlockState();
		if (!state.hasProperty(AbstractBurnerBlock.LIT) || state.getValue(AbstractBurnerBlock.LIT) == lit) {
			return;
		}
		level.setBlockAndUpdate(worldPosition, state.setValue(AbstractBurnerBlock.LIT, lit));
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.putDouble(NBT_HEAT, heat);
		tag.putInt(NBT_BURN_TIME, burnTime);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		heat = tag.getDouble(NBT_HEAT);
		burnTime = tag.getInt(NBT_BURN_TIME);
	}
}