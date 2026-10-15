package dev.celestiacraft.csa.datagen.recipe;

import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.datagen.recipe.type.CsaShapedRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class CsaRecipeProvider extends RecipeProvider {
	public static final String SHAPED = "crafting/shaped/";
	public static final String SHAPELESS = "crafting/shapeless/";

	public CsaRecipeProvider(PackOutput output) {
		super(output);
	}

	protected static ResourceLocation save(String path) {
		return CsaApis.loadResource(path);
	}

	@Override
	protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
		CsaShapedRecipeGen.addRecipe(consumer);
	}
}