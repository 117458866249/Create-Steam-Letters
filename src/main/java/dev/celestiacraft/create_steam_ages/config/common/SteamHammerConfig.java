package dev.celestiacraft.create_steam_ages.config.common;

import dev.celestiacraft.create_steam_ages.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class SteamHammerConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue
			STEAM_HAMMER_PARALLEL,
			STEAM_CONSUMPTION,
			STEAM_CAPACITY;

	public SteamHammerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_hammer", "Steam Hammer");
	}

	@Override
	protected void addConfigs() {
		STEAM_HAMMER_PARALLEL = builder.comment("Steam Hammer recipe parallel")
				.comment("type: int")
				.comment("default: 64")
				.defineInRange("steam_hammer_parallel", 64, 1, getMaxStack());

		STEAM_CONSUMPTION = builder.comment("Steam consumption per run of steam hammer (mB)")
				.comment("type: int")
				.comment("default: 1000")
				.defineInRange("steam_consumption", 1000, 0, 1000000);

		STEAM_CAPACITY = builder.comment("Steam capacity in steam hammer (mB)")
				.comment("type: int")
				.comment("default: 10000")
				.defineInRange("steam_capacity", 10000, 1000, 1000000);
	}
}