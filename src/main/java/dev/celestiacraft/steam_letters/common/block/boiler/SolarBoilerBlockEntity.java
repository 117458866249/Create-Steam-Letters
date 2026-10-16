package dev.celestiacraft.steam_letters.common.block.boiler;

import dev.celestiacraft.steam_letters.config.common.SolarBoilerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * 太阳能锅炉的方块实体.
 * <p>
 * 环境效率分两段判定:
 * <ol>
 * <li>天空光路: 正上方一格能直接看到天空 (天空光等级满级) => 太阳光基础效率;</li>
 * <li>否则尝试人造光路: 设备或正上方一格的方块光等级 >= 阈值 => 人造光基础效率;</li>
 * </ol>
 * 两者都不满足则效率为 0. 天气系数只乘在已经选中的那条光路上.
 * <p>
 * 末地没有天空光, 规则特殊: 只要上方无遮挡就按太阳光算, 且不受天气影响.
 */
public class SolarBoilerBlockEntity extends AbstractBoilerBlockEntity {
	public SolarBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	protected double getCurrentEfficiency() {
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

	/**
	 * 天空光路是否成立.
	 * <p>
	 * 普通维度直接看天空光等级: 玻璃/树叶/其他非完整方块会让天空光衰减, 因此等级满级就等价于"无遮挡";
	 * 末地没有天空光, 改用高度图判断上方是否清爽.
	 */
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

	/**
	 * 人造光判定: 设备自身与正上方一格取较大值 —— 不透明方块内部方块光恒为 0, 只看本体永远不达标.
	 */
	private boolean hasArtificialLight(BlockPos above) {
		int light = Math.max(level.getBrightness(LightLayer.BLOCK, worldPosition), level.getBrightness(LightLayer.BLOCK, above));
		return light >= SolarBoilerConfig.getArtificialLightThreshold();
	}

	/**
	 * 末地没有天气, 系数天然为 1.0; 这里额外屏蔽掉天气的影响.
	 */
	private double getWeatherMultiplier(BlockPos above) {
		if (level.dimension().equals(Level.END)) {
			return 1.0D;
		}
		return SolarBoilerConfig.getWeatherMultiplier(level, above);
	}

	/**
	 * 仅用于调试/显示: 当前是否在用水做原料.
	 */
	public boolean hasWater() {
		return !getInputTank().isEmpty() && getInputTank().getFluid().getFluid().is(FluidTags.WATER);
	}
}