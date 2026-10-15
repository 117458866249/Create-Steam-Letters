package dev.celestiacraft.csa.datagen.recipe.type;

import com.simibubi.create.AllBlocks;
import dev.celestiacraft.csa.common.register.CsaBlocks;
import dev.celestiacraft.csa.datagen.recipe.CsaRecipeProvider;
import dev.celestiacraft.csa.tags.CsaItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

import java.util.function.Consumer;

public class CsaShapedRecipeGen extends CsaRecipeProvider {
	public CsaShapedRecipeGen(PackOutput output) {
		super(output);
	}

	public static void addRecipe(Consumer<FinishedRecipe> consumer) {
		addBronzeBasin(consumer);
	}

	private static void addBronzeBasin(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CsaBlocks.BRONZE_BASIN)
				.pattern("A A")
				.pattern("AAA")
				.define('A', CsaItemTags.BRONZE_INGOT)
				.unlockedBy("bronze", has(CsaItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "bronze_basin_ingot"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CsaBlocks.BRONZE_BASIN)
				.pattern("A A")
				.pattern("AAA")
				.define('A', CsaItemTags.BRONZE_PLATES)
				.unlockedBy("bronze", has(CsaItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "bronze_basin_plate"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CsaBlocks.BRONZE_BASIN)
				.pattern("ABA")
				.pattern("AAA")
				.define('A', CsaItemTags.BRONZE_INGOT)
				.define('B', AllBlocks.BASIN)
				.unlockedBy("bronze", has(CsaItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "bronze_basin_ingot_update"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CsaBlocks.BRONZE_BASIN)
				.pattern("ABA")
				.pattern("AAA")
				.define('A', CsaItemTags.BRONZE_PLATES)
				.define('B', AllBlocks.BASIN)
				.unlockedBy("bronze", has(CsaItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "bronze_basin_plate_update"));
	}
}