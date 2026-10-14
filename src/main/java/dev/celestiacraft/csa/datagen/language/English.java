package dev.celestiacraft.csa.datagen.language;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.api.CsaTranKeys;

public class English {
	public static void addTranslations(RegistrateLangProvider provider) {
		addOtherLang(provider);
		addSteamHammerLang(provider);
	}

	private static void addOtherLang(RegistrateLangProvider provider) {
		provider.add(CsaTranKeys.BASIC_TAB, CreateSteamAges.NAME + ": Basic");
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
}