package dev.celestiacraft.steam_letters.datagen.recipe.type.vanilla;

import com.simibubi.create.AllBlocks;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.datagen.recipe.SlRecipeProvider;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

import java.util.function.Consumer;

public class SlShapedRecipeGen extends SlRecipeProvider {
	public SlShapedRecipeGen(PackOutput output) {
		super(output);
	}

	public static void addRecipes(Consumer<FinishedRecipe> consumer) {
		addBronzeBasin(consumer);
	}

	private static void addBronzeBasin(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_BASIN)
				.pattern("A A")
				.pattern("AAA")
				.define('A', SlItemTags.BRONZE_INGOT)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "bronze_basin_ingot"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_BASIN)
				.pattern("A A")
				.pattern("AAA")
				.define('A', SlItemTags.BRONZE_PLATES)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "bronze_basin_plate"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_BASIN)
				.pattern("ABA")
				.pattern("AAA")
				.define('A', SlItemTags.BRONZE_INGOT)
				.define('B', AllBlocks.BASIN)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "bronze_basin_ingot_update"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_BASIN)
				.pattern("ABA")
				.pattern("AAA")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', AllBlocks.BASIN)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "bronze_basin_plate_update"));
	}
}