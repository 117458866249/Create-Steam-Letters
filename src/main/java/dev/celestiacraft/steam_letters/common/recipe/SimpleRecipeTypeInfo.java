package dev.celestiacraft.steam_letters.common.recipe;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

@AllArgsConstructor
public class SimpleRecipeTypeInfo implements IRecipeTypeInfo {
	@Getter
	private final ResourceLocation id;
	private final Supplier<? extends RecipeType<?>> type;
	private final Supplier<? extends RecipeSerializer<?>> serializer;

	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializer.get();
	}

	@Override
	public <T extends RecipeType<?>> T getType() {
		return (T) type.get();
	}
}