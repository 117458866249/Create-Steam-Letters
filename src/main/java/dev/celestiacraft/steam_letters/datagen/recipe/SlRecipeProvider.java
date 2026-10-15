package dev.celestiacraft.steam_letters.datagen.recipe;

import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.datagen.recipe.type.SlShapedRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class SlRecipeProvider extends RecipeProvider {
	public static final String SHAPED = "crafting/shaped/";
	public static final String SHAPELESS = "crafting/shapeless/";

	public SlRecipeProvider(PackOutput output) {
		super(output);
	}

	protected static ResourceLocation save(String path) {
		return SlApis.loadResource(path);
	}

	@Override
	protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
		SlShapedRecipeGen.addRecipe(consumer);
	}
}