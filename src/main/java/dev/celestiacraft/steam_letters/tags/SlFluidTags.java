package dev.celestiacraft.steam_letters.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

public class SlFluidTags {
	public static TagKey<Fluid>
			STEAM,
			SUPERHEATED_STEAM;

	static {
		STEAM = create("forge", "steam");
		SUPERHEATED_STEAM = create("forge", "superheated_steam");
	}

	private static TagKey<Fluid> create(String namespace, String name) {
		return FluidTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
	}
}