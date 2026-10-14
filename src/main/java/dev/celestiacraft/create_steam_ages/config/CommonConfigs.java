package dev.celestiacraft.create_steam_ages.config;

import dev.celestiacraft.create_steam_ages.config.common.SteamHammerConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final SteamHammerConfig STEAM_HAMMER;

	static {
		BUILDER.comment("All settings below will only take effect after restarting the server or client.")
				.push("general");

		STEAM_HAMMER = new SteamHammerConfig(BUILDER);

		SPEC = BUILDER.build();
		BUILDER.pop();
	}
}