package dev.celestiacraft.steam_letters.common.recipe.type;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IBurnerFuelRecipe {
	int getTime();

	default List<ItemStack> getFuelItems() {
		return List.of();
	}

	default List<FluidStack> getFuelFluids() {
		return List.of();
	}
}