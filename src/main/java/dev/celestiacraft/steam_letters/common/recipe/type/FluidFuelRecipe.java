package dev.celestiacraft.steam_letters.common.recipe.type;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.celestiacraft.steam_letters.common.register.SlRecipeTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class FluidFuelRecipe extends ProcessingRecipe<RecipeWrapper> implements IBurnerFuelRecipe {
	/**
	 * 一份燃料的固定量, 1000mB
	 */
	public static final int FUEL_AMOUNT = 1000;

	/**
	 * datagen 专用: 标记下一条被构造的配方是否过热, 用法同 {@link SolidFuelRecipe#markNextSuperheated(boolean)}.
	 */
	private static final ThreadLocal<Boolean> DATAGEN_SUPERHEATED = new ThreadLocal<>();

	private boolean superheated;

	public FluidFuelRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(SlRecipeTypes.FLUID_FUEL_INFO, params);
		superheated = consumeSuperheated();
	}

	public static void markNextSuperheated(boolean superheated) {
		DATAGEN_SUPERHEATED.set(superheated);
	}

	private static boolean consumeSuperheated() {
		Boolean marked = DATAGEN_SUPERHEATED.get();
		if (marked == null) {
			return false;
		}
		DATAGEN_SUPERHEATED.remove();
		return marked;
	}

	@Override
	public boolean isSuperheated() {
		return superheated;
	}

	public void setSuperheated(boolean superheated) {
		this.superheated = superheated;
	}

	@Override
	public void readAdditional(JsonObject json) {
		super.readAdditional(json);
		superheated = json.has(SUPERHEATED_KEY) && json.get(SUPERHEATED_KEY).getAsBoolean();
	}

	@Override
	public void writeAdditional(JsonObject json) {
		super.writeAdditional(json);
		if (superheated) {
			json.addProperty(SUPERHEATED_KEY, true);
		}
	}

	@Override
	public void readAdditional(FriendlyByteBuf buffer) {
		super.readAdditional(buffer);
		superheated = buffer.readBoolean();
	}

	@Override
	public void writeAdditional(FriendlyByteBuf buffer) {
		super.writeAdditional(buffer);
		buffer.writeBoolean(superheated);
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