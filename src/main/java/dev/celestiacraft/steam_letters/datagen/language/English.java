package dev.celestiacraft.steam_letters.datagen.language;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.client.ponder.SlPonderPlugin;
import dev.celestiacraft.steam_letters.common.block.advanced_spout.AdvancedSpoutScrollValueBehaviour;
import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.createmod.ponder.api.registration.LangRegistryAccess;
import net.createmod.ponder.foundation.PonderIndex;

public class English {
	public static void addTranslations(RegistrateLangProvider provider) {
		addOtherLang(provider);
		addSteamHammerLang(provider);
		addSteamPumpLang(provider);
		addSteamPipeLang(provider);
		addBronzeBasinLang(provider);
		addAdvancedSpoutLang(provider);
		addGoggleLang(provider);
		addSteamEngineLang(provider);
		addJeiLang(provider);
		addSteamContainerLang(provider);
		addConfigLang(provider);
		addConfigCommentLang(provider);

		SlPonderPlugin.register();
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
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_PRODUCTION), "Steam Production");
		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_RATE), "Share of Full Output");
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
		provider.add(SlTranKeys.langKey(SlTranKeys.JEI_HEATED), "Heated");
		provider.add(SlTranKeys.langKey(SlTranKeys.JEI_SUPERHEATED), "Superheated");
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

	private static void addSteamPumpLang(RegistrateLangProvider provider) {
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PUMP_SUMMARY),
				"Only ever _moves steam_, and only when it is the _sole_ medium in the pipe"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PUMP_BEHAVIOUR1),
				"Requires _no rotational force_ and consumes _no stress"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PUMP_BEHAVIOUR2),
				"Works on its own once a steam pipe is attached"
		);
	}

	private static void addSteamPipeLang(RegistrateLangProvider provider) {
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PIPE_SUMMARY),
				"Only _steam_ may flow through this pipe"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PIPE_BEHAVIOUR),
				"Use it together with the _Bronze Steam Pump_"
		);
	}

	private static void addBronzeBasinLang(RegistrateLangProvider provider) {
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_BRONZE_BASIN_SUMMARY),
				"Each fluid slot holds _16 times_ the fluid of a regular basin"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_BRONZE_BASIN_BEHAVIOUR1),
				"Accepts _multiple fluids_, used by mixing and compacting recipes"
		);
		provider.add(
				SlTranKeys.langKey(SlTranKeys.TOOLTIP_BRONZE_BASIN_BEHAVIOUR2),
				"Processes more recipes before it needs to be refilled"
		);

		provider.add(SlTranKeys.langKey(SlTranKeys.GOGGLE_BRONZE_DEPOT_CONTENTS), "Bronze Depot Contents");
	}

	private static void addAdvancedSpoutLang(RegistrateLangProvider provider) {
		provider.add(AdvancedSpoutScrollValueBehaviour.LANG_PARALLEL, "Parallel");
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
		provider.add(ConfigModule.translationKey("steam_boiler", "superheated_threshold_1"), "Heat percentage at which superheated production reaches tier 2");
		provider.add(ConfigModule.translationKey("steam_boiler", "superheated_threshold_2"), "Heat percentage at which superheated production reaches tier 3");
		provider.add(ConfigModule.translationKey("steam_boiler", "superheated_threshold_3"), "Heat percentage at which superheated production reaches tier 4");
		provider.add(ConfigModule.translationKey("steam_boiler", "superheated_production_1"), "Superheated steam output multiplier right above the maximum heat");
		provider.add(ConfigModule.translationKey("steam_boiler", "superheated_production_2"), "Superheated steam output multiplier of tier 2");
		provider.add(ConfigModule.translationKey("steam_boiler", "superheated_production_3"), "Superheated steam output multiplier of tier 3");
		provider.add(ConfigModule.translationKey("steam_boiler", "superheated_production_4"), "Superheated steam output multiplier of tier 4");

		provider.add(ConfigModule.translationKey("solar_boiler"), "Solar Boiler");
		provider.add(ConfigModule.translationKey("solar_boiler", "render_fluid"), "Render fluid inside the solar boiler");
		provider.add(ConfigModule.translationKey("solar_boiler", "sunlight_efficiency"), "Efficiency multiplier when the solar boiler has unobstructed sky access");
		provider.add(ConfigModule.translationKey("solar_boiler", "artificial_light_efficiency"), "Efficiency multiplier when the solar boiler runs on artificial light");
		provider.add(ConfigModule.translationKey("solar_boiler", "artificial_light_threshold"), "Minimum block light level required for artificial light operation");
		provider.add(ConfigModule.translationKey("solar_boiler", "require_daylight"), "Whether sunlight operation also requires it to be daytime");
		provider.add(ConfigModule.translationKey("solar_boiler", "rain_efficiency"), "Weather multiplier while raining");
		provider.add(ConfigModule.translationKey("solar_boiler", "thunder_efficiency"), "Weather multiplier during thunderstorms");

		provider.add(ConfigModule.translationKey("steam_burner"), "Steam Burner");
		provider.add(ConfigModule.translationKey("steam_burner", "heatup_seconds"), "Seconds required for the burner to reach maximum heat");
		provider.add(ConfigModule.translationKey("steam_burner", "cooldown_seconds"), "Seconds required for the burner to lose all of its heat");
		provider.add(ConfigModule.translationKey("steam_burner", "superheated_heatup_multiplier"), "Heat-up speed multiplier while burning superheated fuel");

		provider.add(ConfigModule.translationKey("steam_engine"), "Steam Engine");
		provider.add(ConfigModule.translationKey("steam_engine", "suck_efficiency"), "Ratio of steam gained when a flywheel engine draws steam directly from a boiler (0.75 = 25% loss)");
		provider.add(ConfigModule.translationKey("steam_engine", "superheated_consumption_multiplier"), "Steam consumption multiplier of a flywheel engine running on superheated steam");
		provider.add(ConfigModule.translationKey("steam_engine", "heatup_seconds"), "Seconds the engine needs to heat up before outputting its full stress");
		provider.add(ConfigModule.translationKey("steam_engine", "superheated_acceleration_multiplier"), "Divisor applied to the heat-up time when running on superheated steam");
		provider.add(ConfigModule.translationKey("steam_engine", "superheated_output_multiplier"), "Stress output multiplier when running on superheated steam");

		provider.add(ConfigModule.translationKey("steam", "output_steam_fluid"), "Fluid id produced by boilers. Existing fluid inside machines is never converted when this changes");
		provider.add(ConfigModule.translationKey("steam", "output_superheated_steam_fluid"), "Fluid id produced by boilers while superheated. Existing fluid inside machines is never converted when this changes");
		provider.add(ConfigModule.translationKey("steam", "do_overheated_steam_replace"), "Whether the normal steam left in the output tank is converted into superheated steam instead of being destroyed");
	}

	/**
	 * 配置项注释的英文键.
	 * <p>
	 * Configured 会以 {@code <界面翻译键>.comment} 查询注释, 命中即显示译文, 未命中则回退到 toml 里的原文.
	 * 因此这里的内容必须与配置文件中的英文注释逐条对应.
	 */
	private static void addConfigCommentLang(RegistrateLangProvider provider) {
		comment(provider, ConfigModule.translationKey("general"),
				"All settings below only take effect after restarting the server or client.");

		comment(provider, ConfigModule.translationKey("steam", "superheated_steam_consumption_multiplier"),
				"Global steam consumption multiplier for machines running on superheated steam.\n"
						+ "0.25 means such a machine only needs a quarter of the normal steam.");
		comment(provider, ConfigModule.translationKey("steam", "output_steam_fluid"),
				"Fluid id produced by steam boilers and solar boilers.\n"
						+ "Existing tank contents are never converted when this changes.");
		comment(provider, ConfigModule.translationKey("steam", "output_superheated_steam_fluid"),
				"""
						Fluid id produced by boilers while they run superheated.
						Must not point to the same fluid as output_steam_fluid, otherwise the config fails to load.
						Existing tank contents are never converted when this changes.""");
		comment(provider, ConfigModule.translationKey("steam", "do_overheated_steam_replace"),
				"When false the normal steam left in the output tank is destroyed; when true it is converted "
						+ "into the same amount of superheated steam in place.\n"
						+ "Neither option consumes additional water.");

		comment(provider, ConfigModule.translationKey("steam_hammer", "steam_hammer_parallel"),
				"Maximum number of recipes a single steam hammer processes at once.");
		comment(provider, ConfigModule.translationKey("steam_hammer", "steam_consumption"),
				"Steam consumed per finished recipe (mB).");
		comment(provider, ConfigModule.translationKey("steam_hammer", "steam_capacity"),
				"Steam buffer capacity of a steam hammer (mB).");

		comment(provider, ConfigModule.translationKey("steam_mixer", "steam_mixer_parallel"),
				"Maximum number of recipes a single steam mixer processes at once.");
		comment(provider, ConfigModule.translationKey("steam_mixer", "steam_consumption"),
				"Steam consumed per finished recipe (mB).");
		comment(provider, ConfigModule.translationKey("steam_mixer", "steam_capacity"),
				"Steam buffer capacity of a steam mixer (mB).");

		comment(provider, ConfigModule.translationKey("steam_saw", "steam_saw_parallel"),
				"Maximum number of recipes a single steam saw processes at once.");
		comment(provider, ConfigModule.translationKey("steam_saw", "steam_consumption"),
				"Steam consumed per finished recipe (mB).");
		comment(provider, ConfigModule.translationKey("steam_saw", "steam_capacity"),
				"Steam buffer capacity of a steam saw (mB).");
		comment(provider, ConfigModule.translationKey("steam_saw", "processing_time_multiplier"),
				"Working time multiplier of the steam saw (0.25 = 4 times faster than a mechanical saw).");

		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_threshold_1"),
				"Lowest heat percentage at which the boiler starts producing steam.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_threshold_2"),
				"Heat percentage at which the boiler reaches efficiency tier 2.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_threshold_3"),
				"Heat percentage at which the boiler reaches efficiency tier 3.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_threshold_4"),
				"Heat percentage required for the boiler to run at full efficiency.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_1"),
				"Steam output multiplier of efficiency tier 1.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_2"),
				"Steam output multiplier of efficiency tier 2.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_3"),
				"Steam output multiplier of efficiency tier 3.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "efficiency_4"),
				"Steam output multiplier of efficiency tier 4, i.e. the maximum boiler output.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "superheated_threshold_1"),
				"Heat percentage at which superheated production reaches tier 2.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "superheated_threshold_2"),
				"Heat percentage at which superheated production reaches tier 3.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "superheated_threshold_3"),
				"Heat percentage at which superheated production reaches its maximum rate.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "superheated_production_1"),
				"Superheated steam output multiplier while heat stays right above the maximum heat.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "superheated_production_2"),
				"Superheated steam output multiplier of tier 2.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "superheated_production_3"),
				"Superheated steam output multiplier of tier 3.");
		comment(provider, ConfigModule.translationKey("steam_boiler", "superheated_production_4"),
				"Superheated steam output multiplier of tier 4, i.e. the maximum superheated output.");

		comment(provider, ConfigModule.translationKey("solar_boiler", "render_fluid"),
				"Whether or not fluids inside the solar boiler will be rendered.\n"
						+ "Hiding the fluid does not affect steam production.");
		comment(provider, ConfigModule.translationKey("solar_boiler", "sunlight_efficiency"),
				"Efficiency multiplier when the boiler has direct sky light (and no weather penalty).");
		comment(provider, ConfigModule.translationKey("solar_boiler", "artificial_light_efficiency"),
				"Efficiency multiplier when the boiler is lit by block light instead of sky light.");
		comment(provider, ConfigModule.translationKey("solar_boiler", "artificial_light_threshold"),
				"Minimum block light level for the artificial light path to be usable.");
		comment(provider, ConfigModule.translationKey("solar_boiler", "require_daylight"),
				"Whether the sky light path additionally requires daytime.\n"
						+ "Vanilla sky light level stays at 15 at night, so without this the boiler would "
						+ "produce at full speed in complete darkness.");
		comment(provider, ConfigModule.translationKey("solar_boiler", "rain_efficiency"),
				"Weather multiplier while it is raining at the boiler.");
		comment(provider, ConfigModule.translationKey("solar_boiler", "thunder_efficiency"),
				"Weather multiplier while a thunder storm is raging at the boiler "
						+ "(includes snow during thunder).");

		comment(provider, ConfigModule.translationKey("steam_burner", "heatup_seconds"),
				"Total time required for the burner to reach its maximum heat while fuel is burning.");
		comment(provider, ConfigModule.translationKey("steam_burner", "cooldown_seconds"),
				"Total time required for the burner to lose all of its heat while no fuel is burning.");
		comment(provider, ConfigModule.translationKey("steam_burner", "superheated_heatup_multiplier"),
				"Divisor applied to the heat-up time while burning superheated fuel and below the maximum heat.\n"
						+ "4.0 means the burner heats up four times as fast.");

		comment(provider, ConfigModule.translationKey("steam_engine", "suck_efficiency"),
				"Efficiency (0~1) of steam drawn directly from an adjacent boiler.\n"
						+ "4n mB is drawn out of the boiler per tick while only 3n mB reaches the engine, "
						+ "so a directly fed engine caps out at 75% of its stress output.");
		comment(provider, ConfigModule.translationKey("steam_engine", "superheated_consumption_multiplier"),
				"Steam consumption multiplier applied while the flywheel engine runs on superheated steam.\n"
						+ "0.5 means it only needs half the steam of a normal-steam run.");
		comment(provider, ConfigModule.translationKey("steam_engine", "heatup_seconds"),
				"""
						Seconds required for the flywheel engine to heat up before it outputs its full stress.
						The demand climbs through 25%, 50% and 75% of the full load during the heat-up.
						0 means no heat-up at all.""");
		comment(provider, ConfigModule.translationKey("steam_engine", "superheated_acceleration_multiplier"),
				"Divisor applied to the heat-up time while running on superheated steam.\n"
						+ "2.0 means the heat-up takes half as long.");
		comment(provider, ConfigModule.translationKey("steam_engine", "superheated_output_multiplier"),
				"Stress output multiplier applied while running on superheated steam.");

		comment(provider, ConfigModule.translationKey("bronze_basin", "slot_capacity"),
				"Fluid capacity of each tank of a bronze basin (mB).\n"
						+ "Basins have several tanks, so the total capacity is a multiple of this.");

		comment(provider, ConfigModule.translationKey("advanced_spout", "capacity"),
				"Fluid capacity of an advanced spout (mB).");
		comment(provider, ConfigModule.translationKey("advanced_spout", "parallel"),
				"Default parallel of newly placed advanced spouts.\n"
						+ "Can still be changed per block with a wrench.");
	}

	private static void comment(RegistrateLangProvider provider, String translationKey, String comment) {
		provider.add(translationKey + ".comment", comment);
	}
}