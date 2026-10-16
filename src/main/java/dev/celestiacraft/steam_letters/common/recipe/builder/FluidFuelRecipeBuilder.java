package dev.celestiacraft.steam_letters.common.recipe.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.celestiacraft.steam_letters.common.recipe.SlRecipeTypes;
import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class FluidFuelRecipeBuilder {
	private FluidIngredient ingredient;
	private int time;

	private FluidFuelRecipeBuilder() {
	}

	public static FluidFuelRecipeBuilder builder() {
		return new FluidFuelRecipeBuilder();
	}

	public FluidFuelRecipeBuilder fluid(FluidIngredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public FluidFuelRecipeBuilder fluid(Fluid fluid) {
		return fluid(FluidIngredient.fromFluid(fluid, FluidFuelRecipe.FUEL_AMOUNT));
	}

	public FluidFuelRecipeBuilder fluid(TagKey<Fluid> tag) {
		return fluid(FluidIngredient.fromTag(tag, FluidFuelRecipe.FUEL_AMOUNT));
	}

	public FluidFuelRecipeBuilder fluid(FluidStack stack) {
		return fluid(FluidIngredient.fromFluidStack(stack));
	}

	public FluidFuelRecipeBuilder time(int time) {
		this.time = time;
		return this;
	}

	public FluidFuelRecipe build(ResourceLocation id) {
		validate();
		return new FluidFuelRecipe(id, ingredient, time);
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(build(id)));
	}

	public JsonObject serialize() {
		validate();
		return serialize(ingredient, time);
	}

	private void validate() {
		if (ingredient == null || ingredient == FluidIngredient.EMPTY) {
			throw new IllegalStateException("fluid_fuel recipe must declare a fluid ingredient");
		}
		if (time <= 0) {
			throw new IllegalStateException("fluid_fuel recipe must declare a positive time, but got " + time);
		}
	}

	private static JsonObject serialize(FluidIngredient ingredient, int time) {
		JsonObject json = ingredient.serialize();

		json.remove("amount");
		json.remove("nbt");
		json.addProperty("time", time);

		return json;
	}

	private static class Result implements FinishedRecipe {
		private final FluidFuelRecipe recipe;

		private Result(FluidFuelRecipe recipe) {
			this.recipe = recipe;
		}

		@Override
		public void serializeRecipeData(@NotNull JsonObject json) {
			for (Map.Entry<String, JsonElement> entry : serialize(recipe.getIngredient(), recipe.getTime()).entrySet()) {
				json.add(entry.getKey(), entry.getValue());
			}
		}

		@Override
		public @NotNull ResourceLocation getId() {
			return recipe.getId();
		}

		@Override
		public @NotNull RecipeSerializer<?> getType() {
			return SlRecipeTypes.FLUID_FUEL_SERIALIZER.get();
		}

		@Override
		public @Nullable JsonObject serializeAdvancement() {
			return null;
		}

		@Override
		public @Nullable ResourceLocation getAdvancementId() {
			return null;
		}
	}
}
