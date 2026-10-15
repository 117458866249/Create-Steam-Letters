package dev.celestiacraft.csa.datagen.recipe.type.create;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.common.register.CsaFluids;
import dev.celestiacraft.csa.common.register.CsaMaterials;
import dev.celestiacraft.csa.tags.CsaFluidTags;
import dev.celestiacraft.csa.tags.CsaItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.Tags;

public class CsaMixingRecipeGen extends MixingRecipeGen {
	public CsaMixingRecipeGen(PackOutput output) {
		super(output, CreateSteamAges.MODID);

		create("steam", (builder) -> {
			return builder.output(CsaFluids.STEAM.get(), 1000)
					.require(FluidTags.WATER, 1000)
					.requiresHeat(HeatCondition.HEATED);
		});

		create("superheated_steam", (builder) -> {
			return builder.output(CsaFluids.SUPERHEATED_STEAM.get(), 1000)
					.require(CsaFluidTags.STEAM, 1000)
					.requiresHeat(HeatCondition.SUPERHEATED);
		});

		create("material/bronze/ingot", (builder) -> {
			CsaApis.require(builder, Tags.Items.INGOTS_COPPER, 3);
			return builder.output(CsaMaterials.BRONZE_INGOT, 4)
					.require(CsaItemTags.ANDESITE_ALLOY_INGOT)
					.requiresHeat(HeatCondition.HEATED);
		});

		create("material/bronze/ingot_but_glass", (builder) -> {
			CsaApis.require(builder, Tags.Items.INGOTS_COPPER, 3);
			return builder.output(CsaMaterials.BRONZE_INGOT, 4)
					.require(Tags.Items.GLASS)
					.requiresHeat(HeatCondition.HEATED);
		});
	}
}