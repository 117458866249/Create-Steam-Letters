package dev.celestiacraft.steam_letters.api;

import dev.celestiacraft.steam_letters.CreateLettersSteam;

public class SlTranKeys {
	public static final String
			BASIC_TAB,
			TOOLTIP_STEAM_HAMMER_SUMMARY,
			TOOLTIP_STEAM_HAMMER_CONDITION,
			TOOLTIP_STEAM_HAMMER_BEHAVIOUR1,
			TOOLTIP_STEAM_HAMMER_BEHAVIOUR2,
			GOGGLE_STEAM_BURNER,
			GOGGLE_STEAM_BURNER_TEMPERATURE,
			GOGGLE_STEAM_BURNER_FUEL_TIME,
			GOGGLE_STEAM_BURNER_EFFICIENCY,
			GOGGLE_SOLAR_BOILER,
			GOGGLE_SOLAR_BOILER_WORKING,
			GOGGLE_SOLAR_BOILER_IDLE,
			GOGGLE_SOLAR_BOILER_LIGHT_SUNLIGHT,
			GOGGLE_SOLAR_BOILER_LIGHT_RAIN,
			GOGGLE_SOLAR_BOILER_LIGHT_THUNDER,
			GOGGLE_SOLAR_BOILER_LIGHT_ARTIFICIAL,
			GOGGLE_SOLAR_BOILER_LIGHT_NONE,
			GOGGLE_SOLAR_BOILER_PRODUCTION,
			GOGGLE_SOLAR_BOILER_RATE,
			GOGGLE_STEAM_ENGINE,
			GOGGLE_STEAM_ENGINE_STATUS_IDLE,
			GOGGLE_STEAM_ENGINE_STATUS_HEATUP,
			GOGGLE_STEAM_ENGINE_STATUS_RUNNING,
			GOGGLE_STEAM_ENGINE_HEATUP,
			GOGGLE_STEAM_ENGINE_STRESS,
			GOGGLE_STEAM_ENGINE_STEAM,
			JEI_SOLID_FUEL,
			JEI_FLUID_FUEL,
			TOOLTIP_SEALED_CONTAINER_EMPTY,
			TOOLTIP_SEALED_CONTAINER_FLUID,
			TOOLTIP_STEAM_CONTAINER_EXTRACT,
			TOOLTIP_STEAM_CONTAINER_INSERT,
			TOOLTIP_STEAM_CONTAINER_EXTRACT_MORE,
			TOOLTIP_STEAM_PUMP_SUMMARY,
			TOOLTIP_STEAM_PUMP_BEHAVIOUR1,
			TOOLTIP_STEAM_PUMP_BEHAVIOUR2,
			TOOLTIP_STEAM_PIPE_SUMMARY,
			TOOLTIP_STEAM_PIPE_BEHAVIOUR,
			TOOLTIP_BRONZE_BASIN_SUMMARY,
			TOOLTIP_BRONZE_BASIN_BEHAVIOUR1,
			TOOLTIP_BRONZE_BASIN_BEHAVIOUR2;

	static {
		BASIC_TAB = setCreativeTabKey("basic");
		TOOLTIP_STEAM_HAMMER_SUMMARY = "tooltip.steam_hammer.summary";
		TOOLTIP_STEAM_HAMMER_CONDITION = "tooltip.steam_hammer.condition1";
		TOOLTIP_STEAM_HAMMER_BEHAVIOUR1 = "tooltip.steam_hammer.behaviour1";
		TOOLTIP_STEAM_HAMMER_BEHAVIOUR2 = "tooltip.steam_hammer.behaviour2";

		GOGGLE_STEAM_BURNER = "gui.goggles.steam_burner";
		GOGGLE_STEAM_BURNER_TEMPERATURE = "gui.goggles.steam_burner.temperature";
		GOGGLE_STEAM_BURNER_FUEL_TIME = "gui.goggles.steam_burner.fuel_time";
		GOGGLE_STEAM_BURNER_EFFICIENCY = "gui.goggles.steam_burner.efficiency";

		GOGGLE_SOLAR_BOILER = "gui.goggles.solar_boiler";
		GOGGLE_SOLAR_BOILER_WORKING = "gui.goggles.solar_boiler.working";
		GOGGLE_SOLAR_BOILER_IDLE = "gui.goggles.solar_boiler.idle";
		GOGGLE_SOLAR_BOILER_LIGHT_SUNLIGHT = "gui.goggles.solar_boiler.light.sunlight";
		GOGGLE_SOLAR_BOILER_LIGHT_RAIN = "gui.goggles.solar_boiler.light.rain";
		GOGGLE_SOLAR_BOILER_LIGHT_THUNDER = "gui.goggles.solar_boiler.light.thunder";
		GOGGLE_SOLAR_BOILER_LIGHT_ARTIFICIAL = "gui.goggles.solar_boiler.light.artificial";
		GOGGLE_SOLAR_BOILER_LIGHT_NONE = "gui.goggles.solar_boiler.light.none";
		GOGGLE_SOLAR_BOILER_PRODUCTION = "gui.goggles.solar_boiler.production";
		GOGGLE_SOLAR_BOILER_RATE = "gui.goggles.solar_boiler.rate";

		GOGGLE_STEAM_ENGINE = "gui.goggles.steam_engine";
		GOGGLE_STEAM_ENGINE_STATUS_IDLE = "gui.goggles.steam_engine.status.idle";
		GOGGLE_STEAM_ENGINE_STATUS_HEATUP = "gui.goggles.steam_engine.status.heatup";
		GOGGLE_STEAM_ENGINE_STATUS_RUNNING = "gui.goggles.steam_engine.status.running";
		GOGGLE_STEAM_ENGINE_HEATUP = "gui.goggles.steam_engine.heatup";
		GOGGLE_STEAM_ENGINE_STRESS = "gui.goggles.steam_engine.stress";
		GOGGLE_STEAM_ENGINE_STEAM = "gui.goggles.steam_engine.steam";

		JEI_SOLID_FUEL = "jei.solid_fuel";
		JEI_FLUID_FUEL = "jei.fluid_fuel";

		TOOLTIP_SEALED_CONTAINER_EMPTY = "tooltip.sealed_steam_container.empty";
		TOOLTIP_SEALED_CONTAINER_FLUID = "tooltip.sealed_steam_container.fluid";
		TOOLTIP_STEAM_CONTAINER_EXTRACT = "tooltip.steam_container.extract";
		TOOLTIP_STEAM_CONTAINER_INSERT = "tooltip.steam_container.insert";
		TOOLTIP_STEAM_CONTAINER_EXTRACT_MORE = "tooltip.steam_container.extract_more";

		TOOLTIP_STEAM_PUMP_SUMMARY = "tooltip.steam_pump.summary";
		TOOLTIP_STEAM_PUMP_BEHAVIOUR1 = "tooltip.steam_pump.behaviour1";
		TOOLTIP_STEAM_PUMP_BEHAVIOUR2 = "tooltip.steam_pump.behaviour2";
		TOOLTIP_STEAM_PIPE_SUMMARY = "tooltip.steam_pipe.summary";
		TOOLTIP_STEAM_PIPE_BEHAVIOUR = "tooltip.steam_pipe.behaviour";
		TOOLTIP_BRONZE_BASIN_SUMMARY = "tooltip.bronze_basin.summary";
		TOOLTIP_BRONZE_BASIN_BEHAVIOUR1 = "tooltip.bronze_basin.behaviour1";
		TOOLTIP_BRONZE_BASIN_BEHAVIOUR2 = "tooltip.bronze_basin.behaviour2";
	}

	/**
	 * 返回带模组 id 前缀的完整语言键.
	 */
	public static String langKey(String key) {
		return String.format("%s.%s", CreateLettersSteam.MODID, key);
	}

	private static String setCreativeTabKey(String key) {
		return String.format("itemGroup.%s.%s", CreateLettersSteam.MODID, key);
	}
}