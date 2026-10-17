package dev.celestiacraft.steam_letters.datagen.language;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.common.block.advanced_spout.AdvancedSpoutModeScrollValueBehaviour;
import dev.celestiacraft.steam_letters.common.block.advanced_spout.AdvancedSpoutScrollValueBehaviour;
import dev.celestiacraft.steam_letters.config.api.ConfigModule;

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
	}
}