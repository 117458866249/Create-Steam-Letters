package dev.celestiacraft.steam_letters.config.api;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 一个配置模块: 在固定的 path 下注册若干配置项, 并统一它们的翻译键与注释写法.
 * <p>
 * 注释契约 —— 三层各管一件事, 不要混:
 * <ul>
 * 	<li>{@code .comment(...)} 写英文原文. Forge 没有任何注释翻译通道,
 * 	{@link ForgeConfigSpec.Builder#comment(String...)} 收到的字面量会被原样写进 toml,
 * 	所以这里直接写玩家能读懂的英文 —— 这是唯一能改善 toml 可读性的位置.</li>
 * 	<li>{@code .translation(key("x"))} 是界面翻译键 (选项名), 供 Configured 渲染,
 * 	与注释互不影响.</li>
 * 	<li>中文释义放在 lang 文件的 {@code <translationKey>.comment} 键里.
 * 	Configured 的 {@code ForgeValue#getComment()} 会先查这个键: 命中就用翻译替换掉英文原文,
 * 	只把 Forge 自动追加的 {@code "Range: ..."} 拼回末尾; 未命中才回退到英文原文.
 * 	结论是 <b>toml 永远是英文, 游戏内界面才是中文</b>.</li>
 * </ul>
 */
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
