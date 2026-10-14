package dev.celestiacraft.create_steam_ages.api;

import dev.celestiacraft.create_steam_ages.CreateSteamAges;

public class CSATranKeys {
	public static final String
			BASIC_TAB;

	static {
		BASIC_TAB = setCreativeTabKey("basic");
	}

	private static String setCreativeTabKey(String key) {
		return String.format("itemGroup.%s.%s", CreateSteamAges.MODID, key);
	}
}