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

public class SolarBoilerBlockEntity extends AbstractBoilerBlockEntity implements IHaveGoggleInformation {
	/**
	 * 内置青铜太阳能锅炉的默认参数: 罐 4000 mB, 晴天满日照下产 36 mB/tick 蒸汽,
	 * 与同材料蒸汽引擎的最高消耗速率相等; 雨天/雷暴/人造光按配置折扣.
	 */
	public static final int DEFAULT_CAPACITY = 4000;
	public static final double DEFAULT_PRODUCTION = 36.0D;

	public SolarBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private double cachedEfficiency = -1.0D;

	@Override
	public void lazyTick() {
		super.lazyTick();
		cachedEfficiency = computeEfficiency();
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

		double efficiency = getCurrentEfficiency();
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
}