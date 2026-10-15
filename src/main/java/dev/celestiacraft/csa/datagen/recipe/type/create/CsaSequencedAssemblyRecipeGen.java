package dev.celestiacraft.csa.datagen.recipe.type.create;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.data.recipe.SequencedAssemblyRecipeGen;
import com.simibubi.create.content.fluids.transfer.FillingRecipe;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.common.register.CsaItems;
import dev.celestiacraft.csa.common.register.CsaMaterials;
import dev.celestiacraft.csa.tags.CsaFluidTags;
import net.minecraft.data.PackOutput;

public class CsaSequencedAssemblyRecipeGen extends SequencedAssemblyRecipeGen {
	public CsaSequencedAssemblyRecipeGen(PackOutput output) {
		super(output, CreateSteamAges.MODID);

		create("steam_mechanism", (builder) -> {
			return builder.require(CsaMaterials.BRONZE_PLATE)
					.transitionTo(CsaItems.INCOMPLETE_STEAM_MECHANISM)
					.loops(2)
					.addOutput(CsaItems.STEAM_MECHANISM, 120)
					.addStep(DeployerApplicationRecipe::new, (step) -> {
						return step.require(AllBlocks.COGWHEEL);
					})
					.addStep(FillingRecipe::new, (step) -> {
						return step.require(CsaFluidTags.STEAM, 500);
					});
		});
	}
}