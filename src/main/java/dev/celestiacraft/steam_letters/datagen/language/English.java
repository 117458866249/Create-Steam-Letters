package dev.celestiacraft.steam_letters.datagen.language;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.common.block.advanced_spout.AdvancedSpoutModeScrollValueBehaviour;
import dev.celestiacraft.steam_letters.common.block.advanced_spout.AdvancedSpoutScrollValueBehaviour;
import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.createmod.ponder.api.registration.LangRegistryAccess;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.common.ForgeConfigSpec;

public class English {
	public static void addTranslations(RegistrateLangProvider provider) {
		addOtherLang(provider);
		addSteamHammerLang(provider);
		addAdvancedSpoutLang(provider);
		addGoggleLang(provider);
		addSteamEngineLang(provider);
		addJeiLang(provider);
		addSteamContainerLang(provider);
		addConfigLang(provider);

		LangRegistryAccess access = PonderIndex.getLangAccess();
		access.provideLang(CreateLettersSteam.MODID, provider::add);
	}

	private static void addSteamContainerLang(RegistrateLangProvider provider) {
		provider.add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_SEALED_CONTAINER_EMPTY), "Holds up to _%s mB_ of steam");
		provider.add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_SEALED_CONTAINER_FLUID), "%s: _%s_ / %s mB");
		provider.add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_CONTAINER_EXTRACT), "▸ Right-click a boiler, fluid tank or steam engine to extract steam");
		provider.add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_CONTAINER_INSERT), "▸ Right-click a fluid tank or steam engine to inject steam");
		provider.add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_CONTAINER_EXTRACT_MORE), "▸ Sneak-right-click to keep extracting");
	}

	private static void addGoggleLang(RegistrateLangProvider provider) {
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER), "Steam Burner");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER_TEMPERATURE), "Temperature");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER_FUEL_TIME), "Fuel Remaining");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER_EFFICIENCY), "Steam Output");

		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER), "Solar Boiler");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_WORKING), "Producing Steam");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_IDLE), "No Light");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_SUNLIGHT), "Light: Sunlight");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_RAIN), "Light: Sunlight (Rain)");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_THUNDER), "Light: Sunlight (Thunder)");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_ARTIFICIAL), "Light: Artificial");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_NONE), "Light: None");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_EFFICIENCY), "Efficiency");
	}

	private static void addSteamEngineLang(RegistrateLangProvider provider) {
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE), "Steam Engine");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_IDLE), "Idle");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_HEATUP), "Heating Up");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_RUNNING), "Running");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_HEATUP), "Heat-up");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STRESS), "Stress Output");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STEAM), "Steam");
	}

	private static void addJeiLang(RegistrateLangProvider provider) {
		provider.add(SlTranKeys.langKey(SlTranKeys.JEI_SOLID_FUEL), "Solid Fuel");
		provider.add(SlTranKeys.langKey(SlTranKeys.JEI_FLUID_FUEL), "Fluid Fuel");
	}

	private static void addOtherLang(RegistrateLangProvider provider) {
		provider.add(SlTranKeys.BASIC_TAB, CreateLettersSteam.NAME);
	}

	private static void addSteamHammerLang(RegistrateLangProvider provider) {
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_SUMMARY),
				"Processes _whole stacks_ in a single working cycle, covering _Pressing_, _Compacting_ and _Automated Packing_ recipes"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_CONDITION),
				"When working:"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR1),
				"Only functions when _%s mB_ of steam is in the device"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR2),
				"Consumes _%s mB_ of steam per operation"
		);
	}

	private static void addAdvancedSpoutLang(RegistrateLangProvider provider) {
		provider.add(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE, "Mode");
		provider.add(AdvancedSpoutScrollValueBehaviour.LANG_FILLING_TIME, "Filling time");
		provider.add(AdvancedSpoutScrollValueBehaviour.LANG_PARALLEL, "Parallel");
		provider.add(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE_BOTH, "Speed+Parallel");
		provider.add(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE_SPEED, "Speed");
		provider.add(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE_PARALLEL, "Parallel");
		provider.add(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE_BOTH_SHORT, "Both");
		provider.add(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE_SPEED_SHORT, "Speed");
		provider.add(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE_PARALLEL_SHORT, "Batch");
		provider.add(AdvancedSpoutScrollValueBehaviour.LANG_VALUE_SPEED, "%s ticks");
		provider.add(AdvancedSpoutScrollValueBehaviour.LANG_VALUE_PARALLEL, "x%s");
	}

	private static void addConfigLang(RegistrateLangProvider provider) {
		provider.add(ConfigModule.translationKey("general"), "General");

		provider.add(ConfigModule.translationKey("steam"), "Steam");
		provider.add(ConfigModule.translationKey("steam", "superheated_steam_consumption_multiplier"), "Steam consumption multiplier when the machine runs on superheated steam");

		provider.add(ConfigModule.translationKey("steam_hammer"), "Steam Hammer");
		provider.add(ConfigModule.translationKey("steam_hammer", "steam_hammer_parallel"), "Steam Hammer recipe parallel");
		provider.add(ConfigModule.translationKey("steam_hammer", "steam_consumption"), "Steam consumption per run of steam hammer (mB)");
		provider.add(ConfigModule.translationKey("steam_hammer", "steam_capacity"), "Steam capacity in steam hammer (mB)");

		provider.add(ConfigModule.translationKey("steam_mixer"), "Steam Mixer");
		provider.add(ConfigModule.translationKey("steam_mixer", "steam_mixer_parallel"), "Steam Mixer recipe parallel");
		provider.add(ConfigModule.translationKey("steam_mixer", "steam_consumption"), "Steam consumption per run of steam mixer (mB)");
		provider.add(ConfigModule.translationKey("steam_mixer", "steam_capacity"), "Steam capacity in steam mixer (mB)");

		provider.add(ConfigModule.translationKey("steam_saw"), "Steam Saw");
		provider.add(ConfigModule.translationKey("steam_saw", "processing_time_multiplier"), "Steam Saw working time multiplier (0.25 = 4 times faster than a mechanical saw)");
		provider.add(ConfigModule.translationKey("steam_saw", "steam_capacity"), "Steam capacity in steam saw (mB)");
		provider.add(ConfigModule.translationKey("steam_saw", "steam_consumption"), "Steam consumption per run of steam saw (mB)");
		provider.add(ConfigModule.translationKey("steam_saw", "steam_saw_parallel"), "Steam Saw recipe parallel");

		provider.add(ConfigModule.translationKey("bronze_basin"), "Bronze Basin");
		provider.add(ConfigModule.translationKey("bronze_basin", "slot_capacity"), "Fluid capacity of each bronze basin tank (mB)");

		provider.add(ConfigModule.translationKey("advanced_spout"), "Advanced Spout");
		provider.add(ConfigModule.translationKey("advanced_spout", "capacity"), "Fluid capacity in advanced spout (mB)");
		provider.add(ConfigModule.translationKey("advanced_spout", "parallel"), "Default parallel of newly placed advanced spouts");

		provider.add(ConfigModule.translationKey("steam_boiler"), "Steam Boiler");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_1"), "Heat percentage at which the boiler reaches efficiency tier 1");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_2"), "Heat percentage at which the boiler reaches efficiency tier 2");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_3"), "Heat percentage at which the boiler reaches efficiency tier 3");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_4"), "Heat percentage at which the boiler reaches efficiency tier 4");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_1"), "Steam output multiplier of efficiency tier 1");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_2"), "Steam output multiplier of efficiency tier 2");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_3"), "Steam output multiplier of efficiency tier 3");
		provider.add(ConfigModule.translationKey("steam_boiler", "efficiency_4"), "Steam output multiplier of efficiency tier 4");

		provider.add(ConfigModule.translationKey("solar_boiler"), "Solar Boiler");
		provider.add(ConfigModule.translationKey("solar_boiler", "sunlight_efficiency"), "Efficiency multiplier when the solar boiler has unobstructed sky access");
		provider.add(ConfigModule.translationKey("solar_boiler", "artificial_light_efficiency"), "Efficiency multiplier when the solar boiler runs on artificial light");
		provider.add(ConfigModule.translationKey("solar_boiler", "artificial_light_threshold"), "Minimum block light level required for artificial light operation");
		provider.add(ConfigModule.translationKey("solar_boiler", "require_daylight"), "Whether sunlight operation also requires it to be daytime");
		provider.add(ConfigModule.translationKey("solar_boiler", "rain_efficiency"), "Weather multiplier while raining");
		provider.add(ConfigModule.translationKey("solar_boiler", "thunder_efficiency"), "Weather multiplier during thunderstorms");

		provider.add(ConfigModule.translationKey("steam_burner"), "Steam Burner");
		provider.add(ConfigModule.translationKey("steam_burner", "heat_gain_percent_per_second"), "Heat gained per second while burning, as a percentage of the maximum heat");
		provider.add(ConfigModule.translationKey("steam_burner", "heat_loss_percent_per_second"), "Heat lost per second while idle, as a percentage of the maximum heat");

		provider.add(ConfigModule.translationKey("steam_engine"), "Steam Engine");
		provider.add(ConfigModule.translationKey("steam_engine", "suck_efficiency"), "Ratio of steam gained when a flywheel engine draws steam directly from a boiler (0.75 = 25% loss)");
		provider.add(ConfigModule.translationKey("steam_engine", "superheated_consumption_multiplier"), "Steam consumption multiplier of a flywheel engine running on superheated steam");
		provider.add(ConfigModule.translationKey("steam_engine", "heatup_acceleration"), "Growth of steam consumption during heat-up (mB/tick per tick)");
		provider.add(ConfigModule.translationKey("steam_engine", "superheated_acceleration_multiplier"), "Heat-up acceleration multiplier when running on superheated steam");
		provider.add(ConfigModule.translationKey("steam_engine", "superheated_output_multiplier"), "Stress output multiplier when running on superheated steam");

		provider.add(ConfigModule.translationKey("steam", "output_steam_fluid"), "Fluid id produced by boilers. Existing fluid inside machines is never converted when this changes");

		addConfigCommentLang(provider);
	}

	/**
	 * 配置项的说明文字 (comment) 翻译.
	 * <p>
	 * {@link ForgeConfigSpec.Builder#comment(String...)} 只接受纯字符串,
	 * 没有任何翻译通道, 所以各 ConfigModule 里写的是 {@code .comment(commentKeyOf("xxx"))} ——
	 * 直接把说明本身写成翻译键, toml 里看到的就是那串键名.
	 * <p>
	 * Configured 的 {@code ForgeValue#getComment()} 会拿 {@code <translationKey>.comment} 去查
	 * {@code I18n.exists}, 命中就渲染这里的翻译, 并把 Forge 自动追加的 {@code "Range: ..."} 拼回末尾;
	 * 只有在找不到翻译时, 才会把键名原样显示在界面上. 所以每个键都必须在这里 (以及 zh_cn.json) 有值.
	 */
	private static void addConfigCommentLang(RegistrateLangProvider provider) {
		provider.add(ConfigModule.commentKey("general"),
				"All settings below will only take effect after restarting the server or client.");

		provider.add(ConfigModule.commentKey("steam", "output_steam_fluid"),
				"Fluid id produced by steam boilers and solar boilers. Existing tank contents are never converted when this changes.");

		provider.add(ConfigModule.commentKey("steam_boiler", "efficiency_threshold_1"),
				"Lowest heat percentage at which the boiler starts producing steam.");
		provider.add(ConfigModule.commentKey("steam_boiler", "efficiency_threshold_4"),
				"Heat percentage required for the boiler to run at full efficiency.");

		provider.add(ConfigModule.commentKey("solar_boiler", "sunlight_efficiency"),
				"Efficiency multiplier when the boiler has direct sky light (and no weather penalty).");
		provider.add(ConfigModule.commentKey("solar_boiler", "artificial_light_efficiency"),
				"Efficiency multiplier when the boiler is lit by block light instead of sky light.");
		provider.add(ConfigModule.commentKey("solar_boiler", "artificial_light_threshold"),
				"Minimum block light level for the artificial light path to be usable.");
		provider.add(ConfigModule.commentKey("solar_boiler", "require_daylight"),
				"Whether the sky light path additionally requires daytime.\n"
						+ "Vanilla sky light level stays at 15 during the night, so without this the boiler would produce at full speed in complete darkness.");
		provider.add(ConfigModule.commentKey("solar_boiler", "rain_efficiency"),
				"Weather multiplier while it is raining at the boiler.");
		provider.add(ConfigModule.commentKey("solar_boiler", "thunder_efficiency"),
				"Weather multiplier while a thunder storm is raging at the boiler (includes snow during thunder).");

		provider.add(ConfigModule.commentKey("steam_burner", "heat_gain_percent_per_second"),
				"Heat gained per second while burning fuel, as a percentage of the burner max heat.");
		provider.add(ConfigModule.commentKey("steam_burner", "heat_loss_percent_per_second"),
				"Heat lost per second while no fuel is burning, as a percentage of the burner max heat.");

		provider.add(ConfigModule.commentKey("steam_engine", "suck_efficiency"),
				"""
						Efficiency (0~1) of steam drawn directly from an adjacent boiler.
						4n mB is drawn out of the boiler per tick while only 3n mB reaches the engine,
						so a directly fed engine caps out at 75% of its stress output.""");
		provider.add(ConfigModule.commentKey("steam_engine", "superheated_consumption_multiplier"),
				"Steam consumption multiplier applied while the flywheel engine runs on superheated steam.\n"
						+ "0.5 means it only needs half the steam of a normal-steam run.");
		provider.add(ConfigModule.commentKey("steam_engine", "heatup_acceleration"),
				"Steam consumption ramp-up (mB/tick per tick) while the flywheel engine is heating up.\n"
						+ "0.5 means a 36 mB/tick engine needs 72 ticks to reach full consumption.");
		provider.add(ConfigModule.commentKey("steam_engine", "superheated_acceleration_multiplier"),
				"Heat-up acceleration multiplier applied while running on superheated steam.");
		provider.add(ConfigModule.commentKey("steam_engine", "superheated_output_multiplier"),
				"Stress output multiplier applied while running on superheated steam.");
	}
}