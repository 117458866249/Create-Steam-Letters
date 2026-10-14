package dev.celestiacraft.csa.api;

import dev.celestiacraft.csa.CreateSteamAges;

public class CsaTranKeys {
	public static final String
			BASIC_TAB;

	static {
		BASIC_TAB = setCreativeTabKey("basic");
	}

	private static String setCreativeTabKey(String key) {
		return String.format("itemGroup.%s.%s", CreateSteamAges.MODID, key);
	}
}