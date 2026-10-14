package dev.celestiacraft.csa.config.common;

import dev.celestiacraft.csa.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class SteamConfig extends ConfigModule {
	public static ForgeConfigSpec.DoubleValue
			SUPERHEATED_STEAM_CONSUMPTION_MULTIPLIER;

	public SteamConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam", "Steam");
	}

	@Override
	protected void addConfigs() {
		SUPERHEATED_STEAM_CONSUMPTION_MULTIPLIER = builder.comment("Steam consumption multiplier when the machine runs on superheated steam")
				.comment("type: double")
				.comment("default: 0.25")
				.defineInRange("superheated_steam_consumption_multiplier", 0.25, 0.0, 1.0);
	}
}