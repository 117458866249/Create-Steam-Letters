package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 燃烧室通用配置.
 * <p>
 * 升 / 降温按「升到最高热量 / 降回 0 需要多少秒」配置, 与实际 maxHeat 数值解耦:
 * 默认 10 秒烧满在 maxHeat=100 时即为每 2 tick +1 点;
 * 默认 100 秒冷却为每秒 -1 点.
 */
public class SteamBurnerConfig extends ConfigModule {
	public static ForgeConfigSpec.DoubleValue
			HEATUP_SECONDS,
			COOLDOWN_SECONDS;

	public SteamBurnerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_burner");
	}

	@Override
	protected void addConfigs() {
		HEATUP_SECONDS = builder.translation(key("heatup_seconds"))
				.comment("Total time required to heat up while burning fuel, "
						+ "as time required for the burner to reach max heat.")
				.defineInRange("heatup_seconds", 10.0D, 0.0D, 10000.0D);
		COOLDOWN_SECONDS = builder.translation(key("cooldown_seconds"))
				.comment("Total time to lose all heat while no fuel is burning, "
						+ "as time required for the burner to lose all heat.")
				.defineInRange("cooldown_seconds", 5.0D, 0.0D, 10000.0D);
	}
}