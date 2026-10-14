package dev.celestiacraft.create_steam_ages.config.api;

import net.minecraftforge.common.ForgeConfigSpec;

public abstract class ConfigModule {
	protected final ForgeConfigSpec.Builder builder;

	public ConfigModule(ForgeConfigSpec.Builder builder, String path, String comment) {
		this.builder = builder;

		builder.comment(comment + " Settings")
				.push(path);

		addConfigs();

		builder.pop();
	}

	protected abstract void addConfigs();

	public static boolean validateString(Object object) {
		return object instanceof String;
	}

	public static int getMaxStack() {
		return 64;
	}
}