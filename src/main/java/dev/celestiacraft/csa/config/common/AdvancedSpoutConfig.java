package dev.celestiacraft.csa.config.common;

import dev.celestiacraft.csa.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

public class AdvancedSpoutConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue CAPACITY;
	public static ForgeConfigSpec.IntValue PARALLEL;

	public AdvancedSpoutConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "advanced_spout");
	}

	@Override
	protected void addConfigs() {
		CAPACITY = builder.translation(key("capacity"))
				.defineInRange("capacity", 5000, 2000, 1000000);

		PARALLEL = builder.translation(key("parallel"))
				.defineInRange("parallel", 8, 1, 20);
	}
}