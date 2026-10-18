package dev.celestiacraft.steam_letters.datagen.recipe.type;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.common.recipe.builder.SolidFuelRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;

public class SlSolidFuelRecipeGen extends SolidFuelRecipeGen {
	public SlSolidFuelRecipeGen(PackOutput output) {
		super(output, CreateLettersSteam.MODID);

		recipe("diamond", (builder) -> {
			return builder.item(Tags.Items.GEMS_DIAMOND)
					.time(20 * (60 * 5));
		});

		recipe("diamond_block", (builder) -> {
			return builder.item(Tags.Items.STORAGE_BLOCKS_DIAMOND)
					.time(20 * (60 * 50));
		});
	}
}