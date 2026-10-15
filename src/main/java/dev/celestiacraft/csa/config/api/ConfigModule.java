package dev.celestiacraft.csa.config.api;

import dev.celestiacraft.csa.CreateSteamAges;
import net.minecraftforge.common.ForgeConfigSpec;

public abstract class ConfigModule {
	protected final ForgeConfigSpec.Builder builder;
	protected final String path;

	public ConfigModule(ForgeConfigSpec.Builder builder, String path) {
		this.builder = builder;
		this.path = path;

		builder.translation(translationKey(path))
				.push(path);

		addConfigs();

		builder.pop();
	}

	protected abstract void addConfigs();

	protected String key(String name) {
		return translationKey(path, name);
	}

	public static String translationKey(String... path) {
		return CreateSteamAges.MODID + ".config." + String.join(".", path);
	}

	public static boolean validateString(Object object) {
		return object instanceof String;
	}
}