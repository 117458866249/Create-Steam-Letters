package dev.celestiacraft.csa.config.common;

import dev.celestiacraft.csa.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class AdvancedSpoutConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue FILLING_TIME;
	public static ForgeConfigSpec.IntValue CAPACITY;

	public AdvancedSpoutConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "advanced_spout");
	}

	@Override
	protected void addConfigs() {
		FILLING_TIME = builder.translation(key("filling_time"))
				.defineInRange("filling_time", 5, 1, 20);

		CAPACITY = builder.translation(key("capacity"))
				.defineInRange("capacity", 5000, 2000, 1000000);
	}
}