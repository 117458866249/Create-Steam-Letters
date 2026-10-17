package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 燃烧室通用配置.
 * <p>
 * 升温 / 降温速率按「最高热量的百分比每秒」配置, 与实际 maxHeat 数值解耦:
 * 默认 0.5%/s 的升温在 maxHeat=100 时即为 2 秒 +1 点, 1.0%/s 的降温为 1 秒 -1 点.
 */
public class SteamBurnerConfig extends ConfigModule {
	public static ForgeConfigSpec.DoubleValue
			HEAT_GAIN_PERCENT_PER_SECOND,
			HEAT_LOSS_PERCENT_PER_SECOND;

	public SteamBurnerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_burner");
	}

	@Override
	protected void addConfigs() {
		HEAT_GAIN_PERCENT_PER_SECOND = builder.translation(key("heat_gain_percent_per_second"))
				.comment(commentKeyOf("heat_gain_percent_per_second"))
				.defineInRange("heat_gain_percent_per_second", 0.5D, 0.0D, 100.0D);
		HEAT_LOSS_PERCENT_PER_SECOND = builder.translation(key("heat_loss_percent_per_second"))
				.comment(commentKeyOf("heat_loss_percent_per_second"))
				.defineInRange("heat_loss_percent_per_second", 1.0D, 0.0D, 100.0D);
	}
}