package dev.celestiacraft.csa.datagen.language;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.api.CsaTranKeys;

import javax.swing.*;

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
				"tooltip.steam_hammer.summary",
				"Processes _whole stacks_ in a single working cycle, covering _Pressing_, _Compacting_ and _Automated Packing_ recipes"
		);
		provider.add(
				"steam_hammer.condition1",
				"When working:"
		);
		provider.add(
				"steam_hammer.behaviour1",
				"Only functions when _%s mB_ of steam is in the device"
		);
		provider.add(
				"steam_hammer.behaviour2",
				"Consumes _%s mB_ of steam per operation"
		);
	}
}