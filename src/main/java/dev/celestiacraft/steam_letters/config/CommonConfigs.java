package dev.celestiacraft.steam_letters.config;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import dev.celestiacraft.steam_letters.config.common.*;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfigs {
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final SteamConfig STEAM;
	public static final BronzeBasinConfig BRONZE_BASIN;
	public static final AdvancedSpoutConfig ADVANCED_SPOUT;
	public static final SteamHammerConfig STEAM_HAMMER;
	public static final SteamMixerConfig STEAM_MIXER;
	public static final SteamSawConfig STEAM_SAW;
	public static final SteamBoilerConfig STEAM_BOILER;
	public static final SolarBoilerConfig SOLAR_BOILER;
	public static final SteamBurnerConfig STEAM_BURNER;
	public static final SteamEngineConfig STEAM_ENGINE;

	static {
		BUILDER.comment(ConfigModule.commentKey("general"))
				.translation(ConfigModule.translationKey("general"))
				.push("general");

		STEAM = new SteamConfig(BUILDER);
		BRONZE_BASIN = new BronzeBasinConfig(BUILDER);
		STEAM_HAMMER = new SteamHammerConfig(BUILDER);
		STEAM_MIXER = new SteamMixerConfig(BUILDER);
		STEAM_SAW = new SteamSawConfig(BUILDER);
		ADVANCED_SPOUT = new AdvancedSpoutConfig(BUILDER);
		STEAM_BOILER = new SteamBoilerConfig(BUILDER);
		SOLAR_BOILER = new SolarBoilerConfig(BUILDER);
		STEAM_BURNER = new SteamBurnerConfig(BUILDER);
		STEAM_ENGINE = new SteamEngineConfig(BUILDER);

		SPEC = BUILDER.build();
		BUILDER.pop();
	}
}