package dev.celestiacraft.csa.datagen.recipe.type;

import com.simibubi.create.api.data.recipe.PressingRecipeGen;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.common.register.CsaMaterials;
import dev.celestiacraft.csa.tags.CsaItemTags;
import net.minecraft.data.PackOutput;

public class CsaPressRecipeGen extends PressingRecipeGen {
	public CsaPressRecipeGen(PackOutput output) {
		super(output, CreateSteamAges.MODID);

		create("material/bronze/plate", (builder) -> {
			return builder.output(CsaMaterials.BRONZE_PLATE)
					.require(CsaItemTags.BRONZE_INGOT);
		});
	}
}