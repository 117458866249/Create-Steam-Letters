package dev.celestiacraft.csa.config.common;

import dev.celestiacraft.csa.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class SteamMixerConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue
			STEAM_MIXER_PARALLEL,
			STEAM_CONSUMPTION,
			STEAM_CAPACITY;

	public SteamMixerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_mixer", "Steam Mixer");
	}

	@Override
	protected void addConfigs() {
		STEAM_MIXER_PARALLEL = builder.comment("Steam Mixer recipe parallel")
				.translation(key("steam_mixer_parallel"))
				.defineInRange("steam_mixer_parallel", 16, 1, 64);

		STEAM_CONSUMPTION = builder.comment("Steam consumption per run of steam mixer (mB)")
				.translation(key("steam_consumption"))
				.defineInRange("steam_consumption", 1000, 0, 1000000);

		STEAM_CAPACITY = builder.comment("Steam capacity in steam mixer (mB)")
				.translation(key("steam_capacity"))
				.defineInRange("steam_capacity", 10000, 1000, 1000000);
	}
}