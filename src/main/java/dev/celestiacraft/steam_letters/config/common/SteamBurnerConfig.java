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
			HEAT_LOSS_PERCENT_PER_SECOND,
			MAX_HEAT;

	public static ForgeConfigSpec.IntValue FLUID_CAPACITY;

	public SteamBurnerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_burner");
	}

	@Override
	protected void addConfigs() {
		HEAT_GAIN_PERCENT_PER_SECOND = builder.translation(key("heat_gain_percent_per_second"))
				.comment("Heat gained per second while burning fuel, as a percentage of the burner max heat.")
				.defineInRange("heat_gain_percent_per_second", 0.5D, 0.0D, 100.0D);
		HEAT_LOSS_PERCENT_PER_SECOND = builder.translation(key("heat_loss_percent_per_second"))
				.comment("Heat lost per second while no fuel is burning, as a percentage of the burner max heat.")
				.defineInRange("heat_loss_percent_per_second", 1.0D, 0.0D, 100.0D);

		MAX_HEAT = builder.translation(key("max_heat"))
				.comment("Default heat ceiling of the built-in burners. Burners registered from KubeJS may override it per block.")
				.defineInRange("max_heat", 100.0D, 1.0D, 100000.0D);

		FLUID_CAPACITY = builder.translation(key("fluid_capacity"))
				.comment("Default fuel tank capacity (mB) of the built-in fluid burners. Fluid fuel recipes always consume 1000 mB per burn.")
				.defineInRange("fluid_capacity", 1000, 1000, 1000000);
	}
}