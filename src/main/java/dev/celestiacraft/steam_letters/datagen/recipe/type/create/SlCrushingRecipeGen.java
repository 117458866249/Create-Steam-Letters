package dev.celestiacraft.steam_letters.datagen.recipe.type.create;

import com.simibubi.create.api.data.recipe.CrushingRecipeGen;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.common.register.SlMaterials;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Blocks;

public class SlCrushingRecipeGen extends CrushingRecipeGen {
	public SlCrushingRecipeGen(PackOutput output) {
		super(output, CreateLettersSteam.MODID);

		create("material/tin/nugget/deepslate", (builder) -> {
			return builder.output(0.25f, SlMaterials.TIN_NUGGET, 2)
					.output(0.125f, SlMaterials.TIN_NUGGET, 1)
					.require(Blocks.DEEPSLATE);
		});

		create("material/tin/nugget/cobbled_deepslate", (builder) -> {
			return builder.output(0.25f, SlMaterials.TIN_NUGGET, 2)
					.output(0.125f, SlMaterials.TIN_NUGGET, 1)
					.require(Blocks.COBBLED_DEEPSLATE);
		});
	}
}