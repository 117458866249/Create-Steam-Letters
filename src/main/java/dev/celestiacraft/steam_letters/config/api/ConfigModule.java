package dev.celestiacraft.steam_letters.config.api;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
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

	/**
	 * 本模块某个配置项的界面翻译键, 形如 {@code steam_letters.config.<path>.<name>}.
	 */
	protected String key(String name) {
		return translationKey(path, name);
	}

	public static String translationKey(String... path) {
		return CreateLettersSteam.MODID + ".config." + String.join(".", path);
	}

	public static boolean validateString(Object object) {
		return object instanceof String;
	}
}