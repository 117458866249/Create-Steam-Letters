package dev.celestiacraft.steam_letters.datagen.recipe.type;

import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.recipe.builder.FluidFuelRecipeBuilder;
import dev.celestiacraft.steam_letters.common.recipe.builder.SolidFuelRecipeBuilder;
import dev.celestiacraft.steam_letters.datagen.recipe.SlRecipeProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.FluidTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class SlBurnerFuelRecipeGen extends SlRecipeProvider {
	public SlBurnerFuelRecipeGen(PackOutput output) {
		super(output);
	}

	public static void addRecipes(Consumer<FinishedRecipe> consumer) {
		addSolidFuel(consumer);
		addFluidFuel(consumer);
	}

	private static void addFluidFuel(Consumer<FinishedRecipe> consumer) {
		FluidFuelRecipeBuilder.builder()
				.fluid(FluidTags.LAVA)
				.time(20 * 30)
				.save(consumer, SlApis.loadResource("fluid_fuel/lava"));
	}

	private static void addSolidFuel(Consumer<FinishedRecipe> consumer) {
		SolidFuelRecipeBuilder.builder()
				.tag(Tags.Items.GEMS_DIAMOND)
				.time(20 * (60 * 5))
				.save(consumer, SlApis.loadResource("solid_fuel/diamond"));

		SolidFuelRecipeBuilder.builder()
				.tag(Tags.Items.STORAGE_BLOCKS_DIAMOND)
				.time(20 * (60 * 50))
				.save(consumer, SlApis.loadResource("solid_fuel/diamond_block"));
	}
}