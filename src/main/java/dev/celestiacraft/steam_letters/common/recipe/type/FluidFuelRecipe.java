package dev.celestiacraft.steam_letters.common.recipe.type;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.celestiacraft.steam_letters.common.recipe.SlRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class FluidFuelRecipe extends ProcessingRecipe<RecipeWrapper> implements IBurnerFuelRecipe {
	/**
	 * 一份燃料的流体量, 同时也是配方里 fluid ingredient 的 amount.
	 */
	public static final int FUEL_AMOUNT = 1000;

	public FluidFuelRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(SlRecipeTypes.FLUID_FUEL_INFO, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 0;
	}

	@Override
	protected int getMaxOutputCount() {
		return 0;
	}

	@Override
	protected int getMaxFluidInputCount() {
		return 1;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	@Nullable
	public FluidIngredient ingredient() {
		return fluidIngredients.isEmpty() ? null : fluidIngredients.get(0);
	}

	public boolean matches(@Nullable FluidStack stack) {
		FluidIngredient ingredient = ingredient();
		return ingredient != null && stack != null && !stack.isEmpty() && ingredient.test(stack);
	}

	@Override
	public List<FluidStack> getFuelFluids() {
		FluidIngredient ingredient = ingredient();
		return ingredient == null ? List.of() : ingredient.getMatchingFluidStacks();
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
	public static FluidFuelRecipe find(Level level, FluidStack stack) {
		if (stack == null || stack.isEmpty()) {
			return null;
		}

		List<FluidFuelRecipe> recipes = level.getRecipeManager().getAllRecipesFor(SlRecipeTypes.FLUID_FUEL.get());
		FluidFuelRecipe best = null;
		for (FluidFuelRecipe recipe : recipes) {
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