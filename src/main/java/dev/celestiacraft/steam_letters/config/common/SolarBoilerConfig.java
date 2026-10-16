package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 太阳能锅炉的环境效率配置.
 * <p>
 * 天气系数只乘在已经选中的光路上 (太阳光或人造光), 不与光照基础效率混为一谈.
 * 例: 人造光 50% × 雨天 50% = 25%.
 */
public class SolarBoilerConfig extends ConfigModule {
	public static ForgeConfigSpec.DoubleValue
			SUNLIGHT_EFFICIENCY,
			ARTIFICIAL_LIGHT_EFFICIENCY,
			RAIN_EFFICIENCY,
			THUNDER_EFFICIENCY;

	public static ForgeConfigSpec.IntValue ARTIFICIAL_LIGHT_THRESHOLD;

	public static ForgeConfigSpec.BooleanValue REQUIRE_DAYLIGHT;

	public SolarBoilerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "solar_boiler");
	}

	@Override
	protected void addConfigs() {
		SUNLIGHT_EFFICIENCY = builder.translation(key("sunlight_efficiency"))
				.comment("Efficiency multiplier when the boiler has direct sky light (and no weather penalty).")
				.defineInRange("sunlight_efficiency", 1.0D, 0.0D, 1.0D);

		ARTIFICIAL_LIGHT_EFFICIENCY = builder.translation(key("artificial_light_efficiency"))
				.comment("Efficiency multiplier when the boiler is lit by block light instead of sky light.")
				.defineInRange("artificial_light_efficiency", 0.5D, 0.0D, 1.0D);

		ARTIFICIAL_LIGHT_THRESHOLD = builder.translation(key("artificial_light_threshold"))
				.comment("Minimum block light level for the artificial light path to be usable.")
				.defineInRange("artificial_light_threshold", 8, 0, 15);

		REQUIRE_DAYLIGHT = builder.translation(key("require_daylight"))
				.comment("Whether the sky light path additionally requires daytime.",
						"Vanilla sky light level stays at 15 during the night, so without this the boiler would produce at full speed in complete darkness.")
				.define("require_daylight", true);

		RAIN_EFFICIENCY = builder.translation(key("rain_efficiency"))
				.comment("Weather multiplier while it is raining at the boiler.")
				.defineInRange("rain_efficiency", 0.5D, 0.0D, 1.0D);

		THUNDER_EFFICIENCY = builder.translation(key("thunder_efficiency"))
				.comment("Weather multiplier while a thunder storm is raging at the boiler (includes snow during thunder).")
				.defineInRange("thunder_efficiency", 0.25D, 0.0D, 1.0D);
	}

	/**
	 * 天气系数. 末地等没有天气的维度恒为 1.0.
	 */
	public static double getWeatherMultiplier(Level level, BlockPos pos) {
		if (level.isThundering() && (level.isRainingAt(pos) || level.isRaining())) {
			return THUNDER_EFFICIENCY.get();
		}
		if (level.isRainingAt(pos)) {
			return RAIN_EFFICIENCY.get();
		}
		return 1.0D;
	}

	/**
	 * 天空光路是否可用: 未被遮挡, 并且 (按配置) 处于白天.
	 * <p>
	 * 末地没有天空光, 由调用方另行处理, 不走这里.
	 */
	public static boolean isSkyLightUsable(Level level) {
		return !REQUIRE_DAYLIGHT.get() || level.isDay();
	}

	public static double getSunlightEfficiency() {
		return SUNLIGHT_EFFICIENCY.get();
	}

	public static double getArtificialLightEfficiency() {
		return ARTIFICIAL_LIGHT_EFFICIENCY.get();
	}

	public static int getArtificialLightThreshold() {
		return ARTIFICIAL_LIGHT_THRESHOLD.get();
	}
}