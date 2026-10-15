package dev.celestiacraft.steam_letters.datagen.tags;

import com.simibubi.create.AllItems;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraftforge.common.Tags;

public class ItemTagsGen {
	public static void addItemTags(RegistrateItemTagsProvider provider) {
		provider.addTag(Tags.Items.INGOTS)
				.addTag(SlItemTags.ANDESITE_ALLOY_INGOT);

		provider.addTag(SlItemTags.ANDESITE_ALLOY_INGOT)
				.add(AllItems.ANDESITE_ALLOY.asItem());
	}
}