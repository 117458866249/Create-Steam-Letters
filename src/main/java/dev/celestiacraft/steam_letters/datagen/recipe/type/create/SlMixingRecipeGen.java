package dev.celestiacraft.steam_letters.datagen.recipe.type.create;

import com.simibubi.create.api.data.recipe.MixingRecipeGen;
import com.simibubi.create.content.processing.recipe.HeatCondition;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import dev.celestiacraft.steam_letters.common.register.SlMaterials;
import dev.celestiacraft.steam_letters.tags.SlFluidTags;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.Tags;

public class SlMixingRecipeGen extends MixingRecipeGen {
	public SlMixingRecipeGen(PackOutput output) {
		super(output, CreateLettersSteam.MODID);

		create("superheated_steam", (builder) -> {
			return builder.output(SlFluids.SUPERHEATED_STEAM.get(), 1000)
					.require(SlFluidTags.STEAM, 1000)
					.requiresHeat(HeatCondition.SUPERHEATED);
		});

		create("material/bronze/ingot", (builder) -> {
			SlApis.require(builder, Tags.Items.INGOTS_COPPER, 3);
			return builder.output(SlMaterials.BRONZE_INGOT, 4)
					.require(SlItemTags.ANDESITE_ALLOY_INGOT)
					.requiresHeat(HeatCondition.HEATED);
		});

		create("material/bronze/ingot_but_glass", (builder) -> {
			SlApis.require(builder, Tags.Items.INGOTS_COPPER, 3);
			return builder.output(SlMaterials.BRONZE_INGOT, 4)
					.require(Tags.Items.GLASS)
					.requiresHeat(HeatCondition.HEATED);
		});
	}
}