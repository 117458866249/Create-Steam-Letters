package dev.celestiacraft.csa.config.common;

import dev.celestiacraft.csa.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fluids.FluidType;

public class BronzeBasinConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue SLOT_CAPACITY;

	public BronzeBasinConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "bronze_basin");
	}

	@Override
	protected void addConfigs() {
		SLOT_CAPACITY = builder.translation(key("slot_capacity"))
				.defineInRange("slot_capacity", 10 * FluidType.BUCKET_VOLUME, FluidType.BUCKET_VOLUME, 1000000);
	}
}
