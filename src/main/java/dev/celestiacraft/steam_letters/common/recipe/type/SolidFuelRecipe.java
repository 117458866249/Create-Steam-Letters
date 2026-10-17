package dev.celestiacraft.steam_letters.common.recipe.type;

import dev.celestiacraft.steam_letters.common.recipe.SlRecipeTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@Getter
@AllArgsConstructor
public class SolidFuelRecipe implements Recipe<Container>, IBurnerFuelRecipe {
	private final ResourceLocation id;
	private final Ingredient ingredient;
	private final int time;

	public boolean matches(ItemStack stack) {
		return !stack.isEmpty() && ingredient.test(stack);
	}

	@Override
	public List<ItemStack> getFuelItems() {
		return List.of(ingredient.getItems());
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
		return SlRecipeTypes.SOLID_FUEL_SERIALIZER.get();
	}

	@Override
	public @NotNull RecipeType<?> getType() {
		return SlRecipeTypes.SOLID_FUEL.get();
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
			if (best == null || recipe.time > best.time) {
				best = recipe;
			}
		}
		return best;
	}
}