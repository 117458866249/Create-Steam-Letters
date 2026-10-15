package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class SteamHammerConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue
			STEAM_HAMMER_PARALLEL,
			STEAM_CONSUMPTION,
			STEAM_CAPACITY;

	public SteamHammerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_hammer");
	}

	@Override
	protected void addConfigs() {
		STEAM_HAMMER_PARALLEL = builder.translation(key("steam_hammer_parallel"))
				.defineInRange("steam_hammer_parallel", 64, 1, 64);

		STEAM_CONSUMPTION = builder.translation(key("steam_consumption"))
				.defineInRange("steam_consumption", 1000, 0, 1000000);

		STEAM_CAPACITY = builder.translation(key("steam_capacity"))
				.defineInRange("steam_capacity", 10000, 1000, 1000000);
	}
}
