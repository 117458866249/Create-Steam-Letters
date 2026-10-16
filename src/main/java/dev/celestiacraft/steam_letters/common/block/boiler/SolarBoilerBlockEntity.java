package dev.celestiacraft.steam_letters.common.block.boiler;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.client.lang.SlLang;
import dev.celestiacraft.steam_letters.config.common.SolarBoilerConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;
import java.util.Locale;

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
public class SolarBoilerBlockEntity extends AbstractBoilerBlockEntity implements IHaveGoggleInformation {
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
	 * 护目镜上呈现的光照来源: 把光路与天气合并成一个可直接展示的状态.
	 */
	public enum LightSource {
		SUNLIGHT,
		RAIN,
		THUNDER,
		ARTIFICIAL_LIGHT,
		NONE
	}

	/**
	 * 当前生效的光照来源. 判定顺序与 {@link #getCurrentEfficiency()} 保持一致, 客户端同样可调用.
	 */
	public LightSource getLightSource() {
		if (level == null) {
			return LightSource.NONE;
		}

		BlockPos above = worldPosition.above();
		boolean sky = hasSkyAccess(above);

		if (!sky && !hasArtificialLight(above)) {
			return LightSource.NONE;
		}

		// 末地没有天气, 无遮挡时恒为太阳光
		if (level.dimension().equals(Level.END)) {
			return sky ? LightSource.SUNLIGHT : LightSource.NONE;
		}

		if (level.isThundering() && (level.isRainingAt(above) || level.isRaining())) {
			return LightSource.THUNDER;
		}
		if (level.isRainingAt(above)) {
			return LightSource.RAIN;
		}

		return sky ? LightSource.SUNLIGHT : LightSource.ARTIFICIAL_LIGHT;
	}

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		SlLang.translate(SlTranKeys.GOGGLE_SOLAR_BOILER)
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip);

		double efficiency = getCurrentEfficiency();
		boolean working = efficiency > 0.0D;

		SlLang.translate(working ? SlTranKeys.GOGGLE_SOLAR_BOILER_WORKING : SlTranKeys.GOGGLE_SOLAR_BOILER_IDLE)
				.style(working ? ChatFormatting.GREEN : ChatFormatting.RED)
				.style(ChatFormatting.BOLD)
				.forGoggles(tooltip, 1);

		LightSource source = getLightSource();
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

	private static String lightSourceKey(LightSource source) {
		return switch (source) {
			case SUNLIGHT -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_SUNLIGHT;
			case RAIN -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_RAIN;
			case THUNDER -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_THUNDER;
			case ARTIFICIAL_LIGHT -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_ARTIFICIAL;
			case NONE -> SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_NONE;
		};
	}

	private static ChatFormatting lightSourceColor(LightSource source) {
		return switch (source) {
			case SUNLIGHT -> ChatFormatting.AQUA;
			case RAIN, THUNDER, ARTIFICIAL_LIGHT -> ChatFormatting.YELLOW;
			case NONE -> ChatFormatting.RED;
		};
	}
}