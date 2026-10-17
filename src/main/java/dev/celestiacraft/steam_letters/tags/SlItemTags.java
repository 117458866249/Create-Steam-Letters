package dev.celestiacraft.steam_letters.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SlItemTags {
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
		ANDESITE_ALLOY_INGOT = create("forge", "ingots/andesite_alloy");
		BRONZE_INGOT = create("forge", "ingots/bronze");
		BRONZE_PLATES = create("forge", "plates/bronze");
		BRONZE_BLOCK = create("forge", "storage_blocks/bronze");
		TIN_INGOT = create("forge", "ingots/tin");
		TIN_NUGGET = create("forge", "nuggets/tin");
		TIN_PLATES = create("forge", "plates/tin");
		TIN_BLOCK = create("forge", "storage_blocks/tin");
	}

	private static TagKey<Item> create(String namespace, String name) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
	}
}