package dev.celestiacraft.csa.config.common;

import dev.celestiacraft.csa.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class SteamHammerConfig extends ConfigModule {
	public static ForgeConfigSpec.DoubleValue
			STEAM_HAMMER_STRESS;

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
				.comment(setType("int"))
				.comment(setDefault(64))
				.defineInRange("steam_hammer_parallel", 64, 1, 64);

		STEAM_CONSUMPTION = builder.comment("Steam consumption per run of steam hammer (mB)")
				.comment(setType("int"))
				.comment(setDefault(1000))
				.defineInRange("steam_consumption", 1000, 0, 1000000);

		STEAM_CAPACITY = builder.comment("Steam capacity in steam hammer (mB)")
				.comment(setType("int"))
				.comment(setDefault(10000))
				.defineInRange("steam_capacity", 10000, 1000, 1000000);

		STEAM_HAMMER_STRESS = builder.comment("Steam Hammer Stress")
				.comment(setType("double"))
				.comment(setDefault(16.0))
				.defineInRange("steam_hammer_stress", 16.0, 1, 64);
	}
}