package dev.celestiacraft.steam_letters.common.recipe.type;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import dev.celestiacraft.steam_letters.common.recipe.SlRecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class SolidFuelRecipe extends ProcessingRecipe<RecipeWrapper> implements IBurnerFuelRecipe {
	public SolidFuelRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(SlRecipeTypes.SOLID_FUEL_INFO, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 0;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	@Nullable
	public Ingredient ingredient() {
		return ingredients.isEmpty() ? null : ingredients.get(0);
	}

	public boolean matches(ItemStack stack) {
		Ingredient ingredient = ingredient();
		return ingredient != null && !stack.isEmpty() && ingredient.test(stack);
	}

	@Override
	public List<ItemStack> getFuelItems() {
		Ingredient ingredient = ingredient();
		return ingredient == null ? List.of() : List.of(ingredient.getItems());
	}

	@Override
	public int getTime() {
		return getProcessingDuration();
	}

	@Override
	public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level level) {
		return false;
	}

	@Nullable
	public static SolidFuelRecipe find(Level level, ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}

		List<SolidFuelRecipe> recipes = level.getRecipeManager().getAllRecipesFor(SlRecipeTypes.SOLID_FUEL.get());
		SolidFuelRecipe best = null;
		for (SolidFuelRecipe recipe : recipes) {
			if (!recipe.matches(stack)) {
				continue;
			}
			if (best == null || recipe.getTime() > best.getTime()) {
				best = recipe;
			}
		}
		return best;
	}
}