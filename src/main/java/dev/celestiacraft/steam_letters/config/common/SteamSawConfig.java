package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class SteamSawConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue
			STEAM_SAW_PARALLEL,
			STEAM_CONSUMPTION,
			STEAM_CAPACITY;

	public static ForgeConfigSpec.DoubleValue
			PROCESSING_TIME_MULTIPLIER;

	public SteamSawConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_saw");
	}

	@Override
	protected void addConfigs() {
		STEAM_SAW_PARALLEL = builder.translation(key("steam_saw_parallel"))
				.defineInRange("steam_saw_parallel", 16, 1, 64);

		STEAM_CONSUMPTION = builder.translation(key("steam_consumption"))
				.defineInRange("steam_consumption", 500, 0, 1000000);

		STEAM_CAPACITY = builder.translation(key("steam_capacity"))
				.defineInRange("steam_capacity", 10000, 1000, 1000000);

		PROCESSING_TIME_MULTIPLIER = builder.translation(key("processing_time_multiplier"))
				.defineInRange("processing_time_multiplier", 0.25D, 0.01D, 100.0D);
	}
}