package dev.celestiacraft.steam_letters.common.block.burner;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.celestiacraft.steam_letters.api.machine.ISteamEngineBlock;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.api.machine.heat.IHeatSource;
import dev.celestiacraft.steam_letters.config.common.SteamBurnerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * 燃烧室的公共实现: 维护自身热量, 供正上方锅炉读取.
 * <p>
 * 升温 / 降温速率按「最高热量的百分比每秒」计算, 燃料只决定燃烧时长, 不影响升温速度;
 * 热量始终钳位在 [0, maxHeat], 燃料耗尽后立刻开始降温.
 */
public abstract class AbstractBurnerBlockEntity extends SmartBlockEntity implements IHeatSource {
	private static final String NBT_HEAT = "Heat";
	private static final String NBT_BURN_TIME = "BurnTime";

	protected final SteamEngineSpec spec;

	/**
	 * 当前热量, 允许小数 —— 每 tick 的增量通常远小于 1, 取整会直接丢掉全部产出.
	 */
	protected double heat;

	/**
	 * 当前这份燃料剩余的燃烧时间 (tick), 0 表示没有燃料在燃烧.
	 */
	protected int burnTime;

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
		if (level == null || level.isClientSide) {
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
		return configured > 0.0D ? configured : SteamBurnerConfig.MAX_HEAT.get();
	}

	@Override
	public boolean isBurningFuel() {
		return burnTime > 0;
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