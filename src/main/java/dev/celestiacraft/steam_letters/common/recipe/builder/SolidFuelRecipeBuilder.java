package dev.celestiacraft.steam_letters.common.recipe.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.celestiacraft.steam_letters.common.recipe.SlRecipeTypes;
import dev.celestiacraft.steam_letters.common.recipe.type.SolidFuelRecipe;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Consumer;

public class SolidFuelRecipeBuilder {
	private Ingredient ingredient;
	private int time;

	public static SolidFuelRecipeBuilder builder() {
		return new SolidFuelRecipeBuilder();
	}

	public SolidFuelRecipeBuilder item(Ingredient ingredient) {
		this.ingredient = ingredient;
		return this;
	}

	public SolidFuelRecipeBuilder item(ItemLike item) {
		return item(Ingredient.of(item));
	}

	public SolidFuelRecipeBuilder item(ItemStack stack) {
		return item(Ingredient.of(stack));
	}

	public SolidFuelRecipeBuilder tag(TagKey<Item> tag) {
		return item(Ingredient.of(tag));
	}

	public SolidFuelRecipeBuilder time(int time) {
		this.time = time;
		return this;
	}

	public SolidFuelRecipe build(ResourceLocation id) {
		validate();
		return new SolidFuelRecipe(id, ingredient, time);
	}

	public void save(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
		consumer.accept(new Result(build(id)));
	}

	public JsonObject serialize() {
		validate();
		return serialize(ingredient, time);
	}

	private void validate() {
		if (ingredient == null) {
			throw new IllegalStateException("solid_fuel recipe must declare an item or a tag");
		}
		if (time <= 0) {
			throw new IllegalStateException("solid_fuel recipe must declare a positive time, but got " + time);
		}
		serialize(ingredient, time);
	}

	private static JsonObject serialize(Ingredient ingredient, int time) {
		JsonElement element = ingredient.toJson();
		if (!element.isJsonObject()) {
			throw new IllegalStateException("solid_fuel recipe accepts a single item or a single tag, but got " + element);
		}

		JsonObject selector = element.getAsJsonObject();
		boolean hasItem = selector.has("item");
		boolean hasTag = selector.has("tag");
		if (hasItem == hasTag) {
			throw new IllegalStateException("solid_fuel recipe accepts a single item or a single tag, but got " + selector);
		}

		JsonObject json = new JsonObject();
		json.add(hasItem ? "item" : "tag", selector.get(hasItem ? "item" : "tag"));
		json.addProperty("time", time);
		return json;
	}

	private static class Result implements FinishedRecipe {
		private final SolidFuelRecipe recipe;

		private Result(SolidFuelRecipe recipe) {
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
			return SlRecipeTypes.SOLID_FUEL_SERIALIZER.get();
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