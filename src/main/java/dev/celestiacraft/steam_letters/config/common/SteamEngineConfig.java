package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 飞轮引擎配置.
 * <p>
 * 直抽效率对所有引擎统一生效; 其余项为过热蒸汽与预热过程的机制倍率.
 */
public class SteamEngineConfig extends ConfigModule {
	public static ForgeConfigSpec.DoubleValue
			SUCK_EFFICIENCY,
			SUPERHEATED_CONSUMPTION_MULTIPLIER,
			HEATUP_ACCELERATION,
			SUPERHEATED_ACCELERATION_MULTIPLIER,
			SUPERHEATED_OUTPUT_MULTIPLIER;

	public SteamEngineConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_engine");
	}

	@Override
	protected void addConfigs() {
		SUCK_EFFICIENCY = builder.translation(key("suck_efficiency"))
				.comment(commentKeyOf("suck_efficiency"))
				.defineInRange("suck_efficiency", 0.75D, 0.0D, 1.0D);

		SUPERHEATED_CONSUMPTION_MULTIPLIER = builder.translation(key("superheated_consumption_multiplier"))
				.comment(commentKeyOf("superheated_consumption_multiplier"))
				.defineInRange("superheated_consumption_multiplier", 0.5D, 0.0D, 1.0D);

		HEATUP_ACCELERATION = builder.translation(key("heatup_acceleration"))
				.comment(commentKeyOf("heatup_acceleration"))
				.defineInRange("heatup_acceleration", 0.5D, 0.1D, 100.0D);

		SUPERHEATED_ACCELERATION_MULTIPLIER = builder.translation(key("superheated_acceleration_multiplier"))
				.comment(commentKeyOf("superheated_acceleration_multiplier"))
				.defineInRange("superheated_acceleration_multiplier", 2.0D, 1.0D, 100.0D);

		SUPERHEATED_OUTPUT_MULTIPLIER = builder.translation(key("superheated_output_multiplier"))
				.comment(commentKeyOf("superheated_output_multiplier"))
				.defineInRange("superheated_output_multiplier", 1.5D, 1.0D, 100.0D);
	}
}