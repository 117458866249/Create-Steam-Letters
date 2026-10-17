package dev.celestiacraft.steam_letters.common.recipe.type;

import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.celestiacraft.steam_letters.common.recipe.SlRecipeTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@AllArgsConstructor
public class FluidFuelRecipe implements Recipe<Container>, IBurnerFuelRecipe {
	public static final int FUEL_AMOUNT = 1000;
	@Getter
	private final ResourceLocation id;
	@Getter
	private final FluidIngredient ingredient;
	@Getter
	private final int time;

	public boolean matches(FluidStack stack) {
		return stack != null && !stack.isEmpty() && ingredient.test(stack);
	}

	@Override
	public List<FluidStack> getFuelFluids() {
		return ingredient.getMatchingFluidStacks();
	}

	@Override
	public boolean matches(@NotNull Container container, @NotNull Level level) {
		return false;
	}

	@Override
	public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return false;
	}

	@Override
	public @NotNull ItemStack getResultItem(@NotNull RegistryAccess access) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public @NotNull RecipeSerializer<?> getSerializer() {
		return SlRecipeTypes.FLUID_FUEL_SERIALIZER.get();
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return SlRecipeTypes.FLUID_FUEL.get();
	}

	/**
	 * 在全部流体燃料配方里找出匹配该流体的一条.
	 * <p>
	 * 调用方必须自行缓存结果, 该方法每次都完整遍历配方列表, 不可在 tick 中反复调用.
	 */
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
			if (best == null || recipe.time > best.time) {
				best = recipe;
			}
		}
		return best;
	}
}