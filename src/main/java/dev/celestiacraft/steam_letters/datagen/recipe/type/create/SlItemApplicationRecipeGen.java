package dev.celestiacraft.steam_letters.datagen.recipe.type.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.data.PackOutput;

public class SlItemApplicationRecipeGen extends ItemApplicationRecipeGen {
	public SlItemApplicationRecipeGen(PackOutput output) {
		super(output, CreateLettersSteam.MODID);

		create("bronze_casing_ingot", (builder) -> {
			return builder.require(AllBlocks.COPPER_CASING)
					.require(SlItemTags.BRONZE_INGOT)
					.output(SlBlocks.BRONZE_CASING);
		});

		create("bronze_casing_plate", (builder) -> {
			return builder.require(AllBlocks.COPPER_CASING)
					.require(SlItemTags.BRONZE_PLATES)
					.output(SlBlocks.BRONZE_CASING);
		});
	}
}