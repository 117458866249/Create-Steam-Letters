package dev.celestiacraft.csa.datagen.recipe.type;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.ItemApplicationRecipeGen;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.common.register.CsaBlocks;
import dev.celestiacraft.csa.tags.CsaItemTags;
import net.minecraft.data.PackOutput;

public class CsaItemApplicationRecipeGen extends ItemApplicationRecipeGen {
	public CsaItemApplicationRecipeGen(PackOutput output) {
		super(output, CreateSteamAges.MODID);

		create("bronze_casing_ingot", (builder) -> {
			return builder.require(AllBlocks.COPPER_CASING)
					.require(CsaItemTags.BRONZE_INGOT)
					.output(CsaBlocks.BRONZE_CASING);
		});

		create("bronze_casing_plate", (builder) -> {
			return builder.require(AllBlocks.COPPER_CASING)
					.require(CsaItemTags.BRONZE_PLATES)
					.output(CsaBlocks.BRONZE_CASING);
		});
	}
}