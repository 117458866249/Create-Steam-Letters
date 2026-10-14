package dev.celestiacraft.csa.config.common;

import dev.celestiacraft.csa.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class SteamMixerConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue
			STEAM_HAMMER_PARALLEL,
			STEAM_CONSUMPTION,
			STEAM_CAPACITY;

	public SteamMixerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_mixer", "Steam Mixer");
	}

	@Override
	protected void addConfigs() {
		STEAM_HAMMER_PARALLEL = builder.comment("Steam Mixer recipe parallel")
				.comment(setType("int"))
				.comment(setDefault(16))
				.defineInRange("steam_hammer_parallel", 16, 1, 64);

		STEAM_CONSUMPTION = builder.comment("Steam consumption per run of steam hammer (mB)")
				.comment(setType("int"))
				.comment(setDefault(1000))
				.defineInRange("steam_consumption", 1000, 0, 1000000);

		STEAM_CAPACITY = builder.comment("Steam capacity in steam hammer (mB)")
				.comment(setType("int"))
				.comment(setDefault(10000))
				.defineInRange("steam_capacity", 10000, 1000, 1000000);
	}
}