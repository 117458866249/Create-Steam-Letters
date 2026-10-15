package dev.celestiacraft.csa.datagen.language;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.api.CsaTranKeys;
import dev.celestiacraft.csa.config.api.ConfigModule;

public class English {
	public static void addTranslations(RegistrateLangProvider provider) {
		addOtherLang(provider);
		addSteamHammerLang(provider);
		addConfigLang(provider);
	}

	private static void addOtherLang(RegistrateLangProvider provider) {
		provider.add(CsaTranKeys.BASIC_TAB, CreateSteamAges.NAME + " - Basic");
	}

	private static void addSteamHammerLang(RegistrateLangProvider provider) {
		provider.add(
				CsaTranKeys.langKey(CsaTranKeys.TOOLTIP_STEAM_HAMMER_SUMMARY),
				"Processes _whole stacks_ in a single working cycle, covering _Pressing_, _Compacting_ and _Automated Packing_ recipes"
		);
		provider.add(
				CsaTranKeys.langKey(CsaTranKeys.TOOLTIP_STEAM_HAMMER_CONDITION),
				"When working:"
		);
		provider.add(
				CsaTranKeys.langKey(CsaTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR1),
				"Only functions when _%s mB_ of steam is in the device"
		);
		provider.add(
				CsaTranKeys.langKey(CsaTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR2),
				"Consumes _%s mB_ of steam per operation"
		);
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

		provider.add(ConfigModule.translationKey("bronze_basin"), "Bronze Basin");
		provider.add(ConfigModule.translationKey("bronze_basin", "slot_capacity"), "Fluid capacity of each bronze basin tank (mB)");

		provider.add(ConfigModule.translationKey("advanced_spout"), "Advanced Spout");
		provider.add(ConfigModule.translationKey("advanced_spout", "filling_time"), "Advanced Spout filling time in ticks");
		provider.add(ConfigModule.translationKey("advanced_spout", "capacity"), "Fluid capacity in advanced spout (mB)");
	}
}