package dev.celestiacraft.csa.datagen.recipe.type;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.common.register.CsaFluids;
import dev.celestiacraft.csa.tags.CSAFluidTags;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;

public class CsaMixingRecipeGen extends MixingRecipeGen {
	public final GeneratedRecipe
			STEAM,
			SUPERHEATED_STEAM;

	public CsaMixingRecipeGen(PackOutput output) {
		super(output, CreateSteamAges.MODID);

		STEAM = create("steam", (builder) -> {
			return builder.require(FluidTags.WATER, 1000)
					.output(CsaFluids.STEAM.get(), 1000);
		});

		SUPERHEATED_STEAM = create("superheated_steam", (builder) -> {
			return builder.require(CSAFluidTags.STEAM, 1000)
					.output(CsaFluids.SUPERHEATED_STEAM.get(), 1000)
					.requiresHeat(HeatCondition.SUPERHEATED);
		});
	}
}