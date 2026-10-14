package dev.celestiacraft.csa.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CsaItemTags {
	public static final TagKey<Item>
			BRONZE_INGOT,
			BRONZE_PLATES,
			ANDESITE_ALLOY_INGOT;

	static {
		BRONZE_INGOT = create("forge", "ingots/bronze");
		BRONZE_PLATES = create("forge", "plates/bronze");
		ANDESITE_ALLOY_INGOT = create("forge", "ingots/andesite_alloy");
	}

	private static TagKey<Item> create(String namespace, String name) {
		return ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
	}
}