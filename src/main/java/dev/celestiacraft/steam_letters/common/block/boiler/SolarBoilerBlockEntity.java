package dev.celestiacraft.steam_letters.common.block.boiler;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.client.lang.SlLang;
import dev.celestiacraft.steam_letters.config.common.SolarBoilerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;
import java.util.Locale;

public class SolarBoilerBlockEntity extends AbstractBoilerBlockEntity implements IHaveGoggleInformation {
	/**
	 * 内置青铜太阳能锅炉的默认参数: 罐 10000 mB, 晴天满日照下产 36 mB/tick 蒸汽,
	 * 与同材料蒸汽引擎的最高消耗速率相等; 雨天/雷暴/人造光按配置折扣.
	 */
	public static final int DEFAULT_CAPACITY = 10000;
	public static final double DEFAULT_PRODUCTION = 36.0D;
	private final SmoothFluid water;
	private final SmoothFluid steam;

	private double cachedEfficiency = -1.0D;

	/**
	 * 上一次同步给客户端的两种流体量, 用于让玻璃顶下的液面跟着服务端变化.
	 */
	private int syncedInput = -1;
	private int syncedOutput = -1;

	public SolarBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		water = new SmoothFluid(getInputTank());
		steam = new SmoothFluid(getOutputTank());
	}

	@Override
	public void tick() {
		super.tick();
		if (level != null && level.isClientSide()) {
			water.tick();
			steam.tick();
		}
	}

	/**
	 * 渲染用的水面高度 (0..1), 已经在两次 tick 之间做过插值.
	 */
	public float getRenderedWaterLevel(float partialTick) {
		return water.getLevel(partialTick);
	}

	/**
	 * 渲染用的蒸汽高度 (0..1), 已经在两次 tick 之间做过插值.
	 */
	public float getRenderedSteamLevel(float partialTick) {
		return steam.getLevel(partialTick);
	}

	/**
	 * 最后一次见过的非空水, 用于渲染水体; 不参与序列化.
	 */
	public FluidStack getRenderedWater() {
		return water.getRendered();
	}

	/**
	 * 最后一次见过的非空蒸汽, 用于渲染气体; 不参与序列化.
	 */
	public FluidStack getRenderedSteam() {
		return steam.getRendered();
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		cachedEfficiency = computeEfficiency();
		syncFluidsToClient();
	}

	/**
	 * {@link FluidTank} 内容变化只会标脏, 不会主动发包; 这里每 lazyTick 比对一次水与蒸汽的量,
	 * 变了才同步, 否则客户端的两层液面永远停在方块加载时的那一刻.
	 */
	private void syncFluidsToClient() {
		if (level == null || level.isClientSide()) {
			return;
		}

		int input = getInputTank().getFluidAmount();
		int output = getOutputTank().getFluidAmount();
		if (input == syncedInput && output == syncedOutput) {
			return;
		}

		syncedInput = input;
		syncedOutput = output;
		sendData();
	}

	@Override
	protected double getCurrentEfficiency() {
		if (level == null) {
			return 0.0D;
		}
		if (cachedEfficiency < 0.0D) {
			cachedEfficiency = computeEfficiency();
		}
		return cachedEfficiency;
	}

	private double computeEfficiency() {
		if (level == null) {
			return 0.0D;
		}

		BlockPos above = worldPosition.above();

		if (hasSkyAccess(above)) {
			return SolarBoilerConfig.getSunlightEfficiency() * getWeatherMultiplier(above);
		}

		if (hasArtificialLight(above)) {
			return SolarBoilerConfig.getArtificialLightEfficiency() * getWeatherMultiplier(above);
		}

		return 0.0D;
	}

	private boolean hasSkyAccess(BlockPos above) {
		if (level.dimension().equals(Level.END)) {
			return hasOpenSky(above);
		}
		if (level.getBrightness(LightLayer.SKY, above) < level.getMaxLightLevel()) {
			return false;
		}
		return SolarBoilerConfig.isSkyLightUsable(level);
	}

	private boolean hasOpenSky(BlockPos above) {
		return level.getHeight(Heightmap.Types.MOTION_BLOCKING, above.getX(), above.getZ()) <= above.getY();
	}

	private boolean hasArtificialLight(BlockPos above) {
		int light = Math.max(level.getBrightness(LightLayer.BLOCK, worldPosition), level.getBrightness(LightLayer.BLOCK, above));
		return light >= SolarBoilerConfig.getArtificialLightThreshold();
	}

	private double getWeatherMultiplier(BlockPos above) {
		if (level.dimension().equals(Level.END)) {
			return 1.0D;
		}
		return SolarBoilerConfig.getWeatherMultiplier(level, above);
	}

	/**
	 * 当前生效的光照来源. 判定顺序与 {@link #getCurrentEfficiency()} 保持一致, 客户端同样可调用.
	 */
	public BoilerLightSource getLightSource() {
		if (level == null) {
			return BoilerLightSource.NONE;
		}

		BlockPos above = worldPosition.above();
		boolean sky = hasSkyAccess(above);

		if (!sky && !hasArtificialLight(above)) {
			return BoilerLightSource.NONE;
		}

		// 末地没有天气, 无遮挡时恒为太阳光
		if (level.dimension().equals(Level.END)) {
			return sky ? BoilerLightSource.SUNLIGHT : BoilerLightSource.NONE;
		}

		if (level.isThundering() && (level.isRainingAt(above) || level.isRaining())) {
			return BoilerLightSource.THUNDER;
		}
		if (level.isRainingAt(above)) {
			return BoilerLightSource.RAIN;
		}

		return sky ? BoilerLightSource.SUNLIGHT : BoilerLightSource.ARTIFICIAL_LIGHT;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		SlLang.translate(SlTranKeys.GOGGLE_SOLAR_BOILER)
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip);

		double efficiency = computeEfficiency();
		boolean working = efficiency > 0.0D;

		SlLang.translate(working ? SlTranKeys.GOGGLE_SOLAR_BOILER_WORKING : SlTranKeys.GOGGLE_SOLAR_BOILER_IDLE)
				.style(working ? ChatFormatting.GREEN : ChatFormatting.RED)
				.style(ChatFormatting.BOLD)
				.forGoggles(tooltip, 1);

		BoilerLightSource source = getLightSource();
		SlLang.translate(lightSourceKey(source))
				.style(lightSourceColor(source))
				.forGoggles(tooltip, 1);

		SlLang.translate(SlTranKeys.GOGGLE_SOLAR_BOILER_EFFICIENCY)
				.style(ChatFormatting.GRAY)
				.text(ChatFormatting.DARK_GRAY, ": ")
				.add(Component.literal(String.format(
						Locale.ROOT,
						"%.0f%%",
						efficiency * 100.0D
				)).withStyle(working ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY))
				.forGoggles(tooltip, 1);

		return true;
	}

	private static String lightSourceKey(BoilerLightSource source) {
		return switch (source) {
			case SUNLIGHT -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_SUNLIGHT;
			case RAIN -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_RAIN;
			case THUNDER -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_THUNDER;
			case ARTIFICIAL_LIGHT -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_ARTIFICIAL;
			case NONE -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_NONE;
		};
	}

	private static ChatFormatting lightSourceColor(BoilerLightSource source) {
		return switch (source) {
			case SUNLIGHT -> ChatFormatting.AQUA;
			case RAIN, THUNDER, ARTIFICIAL_LIGHT -> ChatFormatting.YELLOW;
			case NONE -> ChatFormatting.RED;
		};
	}

	/**
	 * 一个储罐的平滑液面: 服务端每 lazyTick 才同步一次内容, 直接用瞬时值会让液面一格一格地跳.
	 * 这里每 tick 朝目标值指数逼近, 渲染时再按 partialTick 在两次 tick 之间插值.
	 */
	private static class SmoothFluid {
		private static final float SMOOTHING = 0.25F;
		private static final float EPSILON = 1.0E-3F;

		private final FluidTank tank;
		private float level;
		private float previousLevel;
		private boolean initialised;
		private FluidStack rendered = FluidStack.EMPTY;

		private SmoothFluid(FluidTank tank) {
			this.tank = tank;
		}

		private void tick() {
			FluidStack fluid = tank.getFluid();
			int capacity = tank.getCapacity();
			float target = capacity > 0 ? Mth.clamp(fluid.getAmount() / (float) capacity, 0.0F, 1.0F) : 0.0F;

			if (!fluid.isEmpty() && (rendered.isEmpty() || !rendered.getFluid().isSame(fluid.getFluid()))) {
				rendered = fluid.copy();
			}

			if (!initialised) {
				initialised = true;
				level = target;
				previousLevel = target;
				return;
			}

			previousLevel = level;
			level += (target - level) * SMOOTHING;

			if (Math.abs(target - level) < EPSILON) {
				level = target;
			}
		}

		private FluidStack getRendered() {
			return rendered;
		}

		private float getLevel(float partialTick) {
			return initialised ? Mth.lerp(partialTick, previousLevel, level) : 0.0F;
		}
	}
}