package dev.celestiacraft.csa.config;

import dev.celestiacraft.csa.config.api.ConfigModule;
import dev.celestiacraft.csa.config.common.*;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final SteamConfig STEAM;
	public static final BronzeBasinConfig BRONZE_BASIN;
	public static final AdvancedSpoutConfig ADVANCED_SPOUT;
	public static final SteamHammerConfig STEAM_HAMMER;
	public static final SteamMixerConfig STEAM_MIXER;

	static {
		BUILDER.comment("All settings below will only take effect after restarting the server or client.")
				.translation(ConfigModule.translationKey("general"))
				.push("general");

		STEAM = new SteamConfig(BUILDER);
		BRONZE_BASIN = new BronzeBasinConfig(BUILDER);
		STEAM_HAMMER = new SteamHammerConfig(BUILDER);
		STEAM_MIXER = new SteamMixerConfig(BUILDER);
		ADVANCED_SPOUT = new AdvancedSpoutConfig(BUILDER);

		SPEC = BUILDER.build();
		BUILDER.pop();
	}
}