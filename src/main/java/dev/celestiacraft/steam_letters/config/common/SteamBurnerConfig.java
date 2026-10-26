package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 燃烧室通用配置.
 * <p>
 * 升 / 降温按「升到最高热量 / 降回 0 需要多少秒」配置, 与实际 maxHeat 数值解耦:
 * 默认 10 秒烧满在 maxHeat=100 时即为每 2 tick +1 点;
 * 默认 100 秒冷却为每秒 -1 点.
 * <p>
 * 过热燃料: 热量上限翻倍, 且在到达最高热量之前以更快的速度升温.
 */
public class SteamBurnerConfig extends ConfigModule {
	/**
	 * 过热状态下的热量上限倍率: 100% 之上还能再烧出 100%, 即最高 200%.
	 */
	public static final double SUPERHEATED_MAX_HEAT_MULTIPLIER = 2.0D;

	/**
	 * 过热状态下热量高于最高热量的那部分 (100%-200%) 的降温倍率.
	 */
	public static final double SUPERHEATED_COOLDOWN_MULTIPLIER = 2.0D;

	public static ForgeConfigSpec.DoubleValue
			HEATUP_SECONDS,
			COOLDOWN_SECONDS,
			SUPERHEATED_HEATUP_MULTIPLIER;

	public SteamBurnerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_burner");
	}

	@Override
	protected void addConfigs() {
		HEATUP_SECONDS = builder.translation(key("heatup_seconds"))
				.comment("Total time required to heat up while burning fuel, "
						+ "as time required for the burner to reach max heat.")
				.defineInRange("heatup_seconds", 60.0D, 0.0D, 10000.0D);
		COOLDOWN_SECONDS = builder.translation(key("cooldown_seconds"))
				.comment("Total time to lose all heat while no fuel is burning, "
						+ "as time required for the burner to lose all heat.")
				.defineInRange("cooldown_seconds", 30.0D, 0.0D, 10000.0D);
		SUPERHEATED_HEATUP_MULTIPLIER = builder.translation(key("superheated_heatup_multiplier"))
				.comment("Heat gain multiplier applied while the burning fuel is marked as superheated.",
						"It only applies below max heat; above max heat the normal speed is used.")
				.defineInRange("superheated_heatup_multiplier", 4.0D, 1.0D, 100.0D);
	}
}