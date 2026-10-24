package dev.celestiacraft.steam_letters.tags;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SlItemTags {
	public static final TagKey<Item>
			STEAM_FLYWHEEL,
			STEAM_ENGINE,
			STEAM_BOILER,
			STEAM_SOLID_BURNER,
			STEAM_FLUID_BURNER,
			STEAM_SOLAR_BOILER,
			STEAM_BURNERS,
			STEAM_PRODUCTION,
			STEAM_CONTAINER;

	public static final TagKey<Item>
			ANDESITE_ALLOY_INGOT,
			BRONZE_INGOT,
			BRONZE_PLATES,
			BRONZE_BLOCK,
			TIN_INGOT,
			TIN_NUGGET,
			TIN_PLATES,
			TIN_BLOCK;

	static {
		STEAM_FLYWHEEL = addMod("steam_flywheel");
		STEAM_ENGINE = addMod("steam_engine");
		STEAM_BOILER = addMod("steam_boiler");
		STEAM_SOLID_BURNER = addMod("steam_solid_burner");
		STEAM_FLUID_BURNER = addMod("steam_fluid_burner");
		STEAM_SOLAR_BOILER = addMod("steam_solar_boiler");
		STEAM_BURNERS = addMod("steam_burners");
		STEAM_PRODUCTION = addMod("steam_production");
		STEAM_CONTAINER = addMod("steam_containers");

		ANDESITE_ALLOY_INGOT = addForge("ingots/andesite_alloy");
		BRONZE_INGOT = addForge("ingots/bronze");
		BRONZE_PLATES = addForge("plates/bronze");
		BRONZE_BLOCK = addForge("storage_blocks/bronze");
		TIN_INGOT = addForge("ingots/tin");
		TIN_NUGGET = addForge("nuggets/tin");
		TIN_PLATES = addForge("plates/tin");
		TIN_BLOCK = addForge("storage_blocks/tin");
	}

	private static TagKey<Item> add(String namespace, String name) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
	}

	private static TagKey<Item> addMod(String name) {
		return add(CreateLettersSteam.MODID, name);
	}

	private static TagKey<Item> addForge(String name) {
		return add("forge", name);
	}
}