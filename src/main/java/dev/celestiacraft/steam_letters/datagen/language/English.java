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
		addConfigLang(provider);
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
		provider.add(ConfigModule.translationKey("steam_boiler", "capacity"), "Fluid capacity of each steam boiler tank (mB)");
		provider.add(ConfigModule.translationKey("steam_boiler", "production"), "Base steam production at full efficiency (mB/tick)");

		provider.add(ConfigModule.translationKey("solar_boiler"), "Solar Boiler");
		provider.add(ConfigModule.translationKey("solar_boiler", "sunlight_efficiency"), "Efficiency multiplier when the solar boiler has unobstructed sky access");
		provider.add(ConfigModule.translationKey("solar_boiler", "artificial_light_efficiency"), "Efficiency multiplier when the solar boiler runs on artificial light");
		provider.add(ConfigModule.translationKey("solar_boiler", "artificial_light_threshold"), "Minimum block light level required for artificial light operation");
		provider.add(ConfigModule.translationKey("solar_boiler", "require_daylight"), "Whether sunlight operation also requires it to be daytime");
		provider.add(ConfigModule.translationKey("solar_boiler", "rain_efficiency"), "Weather multiplier while raining");
		provider.add(ConfigModule.translationKey("solar_boiler", "thunder_efficiency"), "Weather multiplier during thunderstorms");
		provider.add(ConfigModule.translationKey("solar_boiler", "capacity"), "Fluid capacity of each solar boiler tank (mB)");
		provider.add(ConfigModule.translationKey("solar_boiler", "production"), "Base steam production at full efficiency (mB/tick)");

		provider.add(ConfigModule.translationKey("steam_burner"), "Steam Burner");
		provider.add(ConfigModule.translationKey("steam_burner", "heat_gain_percent_per_second"), "Heat gained per second while burning, as a percentage of the maximum heat");
		provider.add(ConfigModule.translationKey("steam_burner", "heat_loss_percent_per_second"), "Heat lost per second while idle, as a percentage of the maximum heat");
		provider.add(ConfigModule.translationKey("steam_burner", "max_heat"), "Default maximum heat of burners that do not declare one");
		provider.add(ConfigModule.translationKey("steam_burner", "fluid_capacity"), "Default fluid fuel tank capacity (mB)");

		provider.add(ConfigModule.translationKey("steam", "output_steam_fluid"), "Fluid id produced by boilers. Existing fluid inside machines is never converted when this changes");
	}
}