package dev.celestiacraft.csa.api;

import dev.celestiacraft.csa.CreateSteamAges;

public class CsaTranKeys {
	public static final String
			BASIC_TAB,
			TOOLTIP_STEAM_HAMMER_SUMMARY,
			TOOLTIP_STEAM_HAMMER_CONDITION,
			TOOLTIP_STEAM_HAMMER_BEHAVIOUR1,
			TOOLTIP_STEAM_HAMMER_BEHAVIOUR2;

	static {
		BASIC_TAB = setCreativeTabKey("basic");
		TOOLTIP_STEAM_HAMMER_SUMMARY = "tooltip.steam_hammer.summary";
		TOOLTIP_STEAM_HAMMER_CONDITION = "tooltip.steam_hammer.condition1";
		TOOLTIP_STEAM_HAMMER_BEHAVIOUR1 = "tooltip.steam_hammer.behaviour1";
		TOOLTIP_STEAM_HAMMER_BEHAVIOUR2 = "tooltip.steam_hammer.behaviour2";
	}

	/**
	 * 返回带模组 id 前缀的完整语言键.
	 */
	public static String langKey(String key) {
		return String.format("%s.%s", CreateSteamAges.MODID, key);
	}

	private static String setCreativeTabKey(String key) {
		return String.format("itemGroup.%s.%s", CreateSteamAges.MODID, key);
	}
}