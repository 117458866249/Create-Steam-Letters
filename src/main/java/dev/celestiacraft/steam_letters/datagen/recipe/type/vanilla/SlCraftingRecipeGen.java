package dev.celestiacraft.steam_letters.datagen.recipe.type.vanilla;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.common.register.SlItems;
import dev.celestiacraft.steam_letters.common.register.SlMaterials;
import dev.celestiacraft.steam_letters.datagen.recipe.SlRecipeProvider;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class SlCraftingRecipeGen extends SlRecipeProvider {
	public SlCraftingRecipeGen(PackOutput output) {
		super(output);
	}

	public static void addRecipes(Consumer<FinishedRecipe> consumer) {
		addBronzeBasin(consumer);
		addBronzeDepot(consumer);
		addBurner(consumer);
		addBoiler(consumer);
		addMaterial(consumer);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlItems.EMPTY_STEAM_CONTAINER)
				.pattern(" A ")
				.pattern("BCB")
				.pattern(" A ")
				.define('A', Items.DRIED_KELP)
				.define('B', SlItemTags.TIN_INGOT)
				.define('C', Tags.Items.GLASS_PANES)
				.unlockedBy("dried_kelp", has(Items.DRIED_KELP))
				.save(consumer, save(SHAPED + "empty_steam_container"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlItems.BRONZE_WHISK)
				.pattern(" A ")
				.pattern("CBC")
				.pattern("CCC")
				.define('A', Tags.Items.INGOTS_IRON)
				.define('B', AllItems.WHISK)
				.define('C', SlItemTags.BRONZE_PLATES)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "bronze_whisk"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.ADVANCED_SPOUT)
				.pattern("ABA")
				.pattern("MCM")
				.pattern(" A ")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', SlBlocks.BRONZE_CASING)
				.define('M', SlItems.STEAM_MECHANISM)
				.define('C', AllBlocks.SPOUT)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "advanced_spout"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.STEAM_HAMMER)
				.pattern("ABA")
				.pattern("MCM")
				.pattern(" D ")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', SlBlocks.BRONZE_CASING)
				.define('M', SlItems.STEAM_MECHANISM)
				.define('C', AllBlocks.MECHANICAL_PRESS)
				.define('D', SlMaterials.BRONZE_BLOCK)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "steam_hammer"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.STEAM_MIXER)
				.pattern("ABA")
				.pattern("MCM")
				.pattern(" D ")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', SlBlocks.BRONZE_CASING)
				.define('M', SlItems.STEAM_MECHANISM)
				.define('C', AllBlocks.MECHANICAL_MIXER)
				.define('D', SlItems.BRONZE_WHISK)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "steam_mixer"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.STEAM_SAW)
				.pattern("MCM")
				.pattern("ABA")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', SlBlocks.BRONZE_CASING)
				.define('M', SlItems.STEAM_MECHANISM)
				.define('C', AllBlocks.MECHANICAL_SAW)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "steam_saw"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_STEAM_PUMP)
				.pattern("AMA")
				.pattern(" B ")
				.pattern("AMA")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', AllBlocks.FLUID_PIPE)
				.define('M', SlItems.STEAM_MECHANISM)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "steam_pump"));
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
				.pattern(" A ")
				.define('A', SlItemTags.BRONZE_INGOT)
				.define('B', AllBlocks.BASIN)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "bronze_basin_ingot_update"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_BASIN)
				.pattern("ABA")
				.pattern(" A ")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', AllBlocks.BASIN)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "bronze_basin_plate_update"));
	}

	private static void addBronzeDepot(Consumer<FinishedRecipe> consumer) {
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlBlocks.BRONZE_DEPOT)
				.requires(SlItemTags.BRONZE_INGOT)
				.requires(SlBlocks.BRONZE_CASING)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPELESS + "bronze_depot_ingot"));

		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, SlBlocks.BRONZE_DEPOT)
				.requires(SlItemTags.BRONZE_PLATES)
				.requires(SlBlocks.BRONZE_CASING)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPELESS + "bronze_depot_plate"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_DEPOT)
				.pattern(" A ")
				.pattern("ABA")
				.pattern(" A ")
				.define('A', SlItemTags.BRONZE_INGOT)
				.define('B', AllBlocks.DEPOT)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_INGOT))
				.save(consumer, save(SHAPED + "bronze_depot_ingot_update"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, SlBlocks.BRONZE_DEPOT)
				.pattern(" A ")
				.pattern("ABA")
				.pattern(" A ")
				.define('A', SlItemTags.BRONZE_PLATES)
				.define('B', AllBlocks.DEPOT)
				.unlockedBy("bronze", has(SlItemTags.BRONZE_PLATES))
				.save(consumer, save(SHAPED + "bronze_depot_plate_update"));
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