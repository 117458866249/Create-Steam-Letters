package dev.celestiacraft.steam_letters.datagen.recipe.type.vanilla;

import com.simibubi.create.AllBlocks;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.common.register.SlMaterials;
import dev.celestiacraft.steam_letters.datagen.recipe.SlRecipeProvider;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;

public class SlCraftingRecipeGen extends SlRecipeProvider {
	public SlCraftingRecipeGen(PackOutput output) {
		super(output);
	}

	public static void addRecipes(Consumer<FinishedRecipe> consumer) {
		addBronzeBasin(consumer);
		addBurner(consumer);
		addBoiler(consumer);
		addMaterial(consumer);
	}

	private static void addMaterial(Consumer<FinishedRecipe> consumer) {
		// Bronze
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SlMaterials.BRONZE_BLOCK)
				.pattern("AAA")
				.pattern("AAA")
				.pattern("AAA")
				.define('A', SlItemTags.BRONZE_INGOT)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "bronze_block_ingot"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlMaterials.BRONZE_INGOT, 9)
				.requires(SlItemTags.BRONZE_BLOCK)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPELESS + "bronze_ingot_block"));

		// Tin
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SlMaterials.TIN_BLOCK)
				.pattern("AAA")
				.pattern("AAA")
				.pattern("AAA")
				.define('A', SlItemTags.TIN_INGOT)
				.unlockedBy("tin", has(SlItemTags.TIN_INGOT))
				.save(consumer, save(SHAPED + "tin_block_ingot"));

		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, SlMaterials.TIN_INGOT)
				.pattern("AAA")
				.pattern("AAA")
				.pattern("AAA")
				.define('A', SlItemTags.TIN_NUGGET)
				.unlockedBy("tin", has(SlItemTags.TIN_NUGGET))
				.save(consumer, save(SHAPED + "tin_ingot_nugget"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlMaterials.TIN_INGOT, 9)
				.requires(SlItemTags.TIN_BLOCK)
				.unlockedBy("tin", has(SlItemTags.TIN_INGOT))
				.save(consumer, save(SHAPELESS + "tin_ingot_block"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlMaterials.TIN_NUGGET, 9)
				.requires(SlItemTags.TIN_INGOT)
				.unlockedBy("tin", has(SlItemTags.TIN_INGOT))
				.save(consumer, save(SHAPELESS + "tin_nugget_ingot"));
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

	private static void addBurner(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_SOLID_BURNER)
				.pattern("AAA")
				.pattern("ABA")
				.pattern("CCC")
				.define('A', SlItemTags.BRONZE_INGOT)
				.define('B', Blocks.FURNACE)
				.define('C', Blocks.MUD_BRICKS)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "burner/solid_ingot"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_SOLID_BURNER)
				.pattern("AAA")
				.pattern("ABA")
				.pattern("CCC")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', Blocks.FURNACE)
				.define('C', Blocks.MUD_BRICKS)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "burner/solid_plate"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_FLUID_BURNER)
				.pattern("AAA")
				.pattern("ABA")
				.pattern("CCC")
				.define('A', SlItemTags.BRONZE_INGOT)
				.define('B', AllBlocks.FLUID_TANK)
				.define('C', Blocks.MUD_BRICKS)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "burner/fluid_ingot"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_FLUID_BURNER)
				.pattern("AAA")
				.pattern("ABA")
				.pattern("CCC")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', AllBlocks.FLUID_TANK)
				.define('C', Blocks.MUD_BRICKS)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "burner/fluid_plate"));
	}

	private static void addBoiler(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_BOILER)
				.pattern("AAA")
				.pattern("ABA")
				.pattern("AAA")
				.define('A', SlItemTags.BRONZE_INGOT)
				.define('B', AllBlocks.FLUID_TANK)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "boiler/ingot"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_BOILER)
				.pattern("AAA")
				.pattern("ABA")
				.pattern("AAA")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', AllBlocks.FLUID_TANK)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "boiler/plate"));
	}
}