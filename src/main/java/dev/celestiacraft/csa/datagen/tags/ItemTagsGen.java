package dev.celestiacraft.csa.datagen.tags;

import com.simibubi.create.AllItems;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import dev.celestiacraft.csa.tags.CsaItemTags;
import net.minecraftforge.common.Tags;

public class ItemTagsGen {
	public static void addItemTags(RegistrateItemTagsProvider provider) {
		provider.addTag(Tags.Items.INGOTS)
				.addTag(CsaItemTags.ANDESITE_ALLOY_INGOT);

		provider.addTag(CsaItemTags.ANDESITE_ALLOY_INGOT)
				.add(AllItems.ANDESITE_ALLOY.asItem());
	}
}