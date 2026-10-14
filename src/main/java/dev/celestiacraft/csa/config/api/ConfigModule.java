package dev.celestiacraft.csa.config.api;

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

	public static String setType(String type) {
		return "type: " + type;
	}

	public static String setDefault(String type) {
		return "defalut: " + type;
	}

	public static String setDefault(int defalut) {
		return "defalut: " + defalut;
	}

	public static String setDefault(double defalut) {
		return "defalut: " + defalut;
	}

	public static String setDefault(boolean defalut) {
		return "defalut: " + defalut;
	}
}