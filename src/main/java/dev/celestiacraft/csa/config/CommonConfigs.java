package dev.celestiacraft.csa.config;

import dev.celestiacraft.csa.config.common.SteamConfig;
import dev.celestiacraft.csa.config.common.SteamHammerConfig;
import dev.celestiacraft.csa.config.common.SteamMixerConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final SteamConfig STEAM;
	public static final SteamHammerConfig STEAM_HAMMER;
	public static final SteamMixerConfig STEAM_MIXER;

	static {
		BUILDER.comment("All settings below will only take effect after restarting the server or client.")
				.push("general");

		STEAM = new SteamConfig(BUILDER);
		STEAM_HAMMER = new SteamHammerConfig(BUILDER);
		STEAM_MIXER = new SteamMixerConfig(BUILDER);

		SPEC = BUILDER.build();
		BUILDER.pop();
	}
}