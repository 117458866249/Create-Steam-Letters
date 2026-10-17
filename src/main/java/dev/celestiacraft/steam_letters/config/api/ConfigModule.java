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

	protected String key(String name) {
		return translationKey(path, name);
	}

	/**
	 * 本模块某个配置项的说明 (comment) 翻译键, 等价于 {@code key(name) + ".comment"}.
	 * <p>
	 * {@link ForgeConfigSpec.Builder#comment(String...)} 只接受纯字符串, 没有任何翻译通道;
	 * 而 Configured 的 {@code ForgeValue#getComment()} 会先查 {@code <translationKey>.comment},
	 * 命中 {@code I18n.exists} 就渲染翻译. 所以这里直接把注释写成这个键,
	 * 让 {@code ValueSpec#getComment()} 返回的东西本身就是"可翻译的".
	 * <p>
	 * toml 里看到的就是这一串键名 —— 它同时充当"这里有一条待翻译的说明"的标记;
	 * 游戏内配置界面渲染的是 lang 文件里的内容, 不是这串键名.
	 */
	protected String commentKeyOf(String name) {
		return key(name) + ".comment";
	}

	/**
	 * Configured 会先查 {@code <translationKey>.comment} 有没有翻译, 命中就渲染翻译, 否则回退到
	 * {@link ForgeConfigSpec.Builder#comment(String...)} 写死的英文. 这里生成的就是那个键.
	 */
	public static String commentKey(String... path) {
		return translationKey(path) + ".comment";
	}

	public static String translationKey(String... path) {
		return CreateLettersSteam.MODID + ".config." + String.join(".", path);
	}

	public static boolean validateString(Object object) {
		return object instanceof String;
	}
}