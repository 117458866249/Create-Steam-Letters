package dev.celestiacraft.steam_letters.compat.kubejs.schema;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import dev.celestiacraft.steam_letters.common.recipe.type.IBurnerFuelRecipe;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.BooleanComponent;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BurnerSchema {
	public static final RecipeKey<Boolean> SUPERHEATED = new RecipeKey<>(
			new FuelSuperheatedComponent(),
			IBurnerFuelRecipe.SUPERHEATED_KEY
	).optional(false);

	public static final RecipeSchema SOLID_FUEL = new RecipeSchema(
			BurnerRecipeJS.class,
			BurnerRecipeJS::new,
			new RecipeKey<>(new FuelItemComponent(), "fuel"),
			new RecipeKey<>(new FuelTimeComponent(), "time"),
			SUPERHEATED
	);

	public static final RecipeSchema FLUID_FUEL = new RecipeSchema(
			BurnerRecipeJS.class,
			BurnerRecipeJS::new,
			new RecipeKey<>(new FuelFluidComponent(), "fuel"),
			new RecipeKey<>(new FuelTimeComponent(), "time"),
			SUPERHEATED
	);

	public static class BurnerRecipeJS extends RecipeJS {
		public RecipeJS superheated() {
			return superheated(Boolean.FALSE);
		}

		public RecipeJS superheated(@Nullable Boolean superheated) {
			return setValue(SUPERHEATED, Boolean.TRUE.equals(superheated));
		}
	}

	private static void writeIngredients(@NotNull JsonObject json, @NotNull JsonObject ingredient) {
		JsonArray ingredients = new JsonArray();
		ingredients.add(ingredient);
		json.add("ingredients", ingredients);
		json.add("results", new JsonArray());
	}

	@Nullable
	private static JsonObject firstIngredient(@NotNull JsonObject json) {
		if (!json.has("ingredients")) {
			return null;
		}
		JsonElement element = json.get("ingredients");
		if (!element.isJsonArray() || element.getAsJsonArray().isEmpty()) {
			return null;
		}
		JsonElement first = element.getAsJsonArray().get(0);
		return first.isJsonObject() ? first.getAsJsonObject() : null;
	}

	private static void splitItem(@NotNull JsonElement written, @NotNull JsonObject json) {
		if (written.isJsonObject()) {
			JsonObject object = written.getAsJsonObject();
			if (isPrimitive(object, "item")) {
				json.add("item", object.get("item"));
				return;
			}
			if (isPrimitive(object, "tag")) {
				json.addProperty("tag", stripTagPrefix(object.get("tag").getAsString()));
				return;
			}
			if (object.has("ingredient")) {
				splitItem(object.get("ingredient"), json);
			}
			return;
		}

		if (written.isJsonArray() && !written.getAsJsonArray().isEmpty()) {
			splitItem(written.getAsJsonArray().get(0), json);
			return;
		}

		if (written.isJsonPrimitive()) {
			String value = written.getAsString();
			if (!value.isEmpty() && value.charAt(0) == '#') {
				json.addProperty("tag", value.substring(1));
			} else {
				json.addProperty("item", value);
			}
		}
	}

	private static boolean isPrimitive(@NotNull JsonObject object, @NotNull String name) {
		return object.has(name) && object.get(name).isJsonPrimitive();
	}

	private static @NotNull String stripTagPrefix(@NotNull String value) {
		return !value.isEmpty() && value.charAt(0) == '#' ? value.substring(1) : value;
	}

	private static class FuelItemComponent implements RecipeComponent<InputItem> {
		@Override
		public @NotNull Class<?> componentClass() {
			return InputItem.class;
		}

		@Override
		public @NotNull ComponentRole role() {
			return ComponentRole.INPUT;
		}

		@Override
		public @NotNull JsonElement write(@NotNull RecipeJS recipe, @NotNull InputItem value) {
			return ItemComponents.INPUT.write(recipe, value);
		}

		@Override
		public @NotNull InputItem read(@NotNull RecipeJS recipe, @NotNull Object from) {
			if (from instanceof JsonElement element) {
				return ItemComponents.INPUT.read(recipe, element.getAsString());
			}
			return ItemComponents.INPUT.read(recipe, from);
		}

		@Override
		public void writeToJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<InputItem> holder, @NotNull JsonObject json) {
			JsonObject ingredient = new JsonObject();
			splitItem(ItemComponents.INPUT.write(recipe, holder.value), ingredient);
			writeIngredients(json, ingredient);
		}

		@Override
		public void readFromJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<InputItem> holder, @NotNull JsonObject json) {
			JsonObject ingredient = firstIngredient(json);
			if (ingredient == null) {
				return;
			}
			if (ingredient.has("item")) {
				holder.value = read(recipe, ingredient.get("item"));
			} else if (ingredient.has("tag")) {
				holder.value = read(recipe, "#" + ingredient.get("tag").getAsString());
			}
		}
	}

	private static class FuelFluidComponent implements RecipeComponent<FuelFluid> {
		@Override
		public @NotNull Class<?> componentClass() {
			return FuelFluid.class;
		}

		@Override
		public @NotNull ComponentRole role() {
			return ComponentRole.INPUT;
		}

		@Override
		public @NotNull JsonElement write(@NotNull RecipeJS recipe, @NotNull FuelFluid value) {
			return value.toJson();
		}

		@Override
		public @NotNull FuelFluid read(@NotNull RecipeJS recipe, @NotNull Object from) {
			return FuelFluid.of(from);
		}

		@Override
		public void writeToJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<FuelFluid> holder, @NotNull JsonObject json) {
			FuelFluid value = holder.value;
			JsonObject ingredient = new JsonObject();
			if (value.isTag()) {
				ingredient.addProperty("fluidTag", value.getTag().toString());
			} else {
				ingredient.addProperty("fluid", value.getFluid().getId());
			}
			ingredient.addProperty("amount", FluidFuelRecipe.FUEL_AMOUNT);
			writeIngredients(json, ingredient);
		}

		@Override
		public void readFromJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<FuelFluid> holder, @NotNull JsonObject json) {
			JsonObject ingredient = firstIngredient(json);
			if (ingredient == null) {
				return;
			}
			if (ingredient.has("fluid")) {
				holder.value = read(recipe, ingredient.get("fluid"));
			} else if (ingredient.has("fluidTag")) {
				holder.value = read(recipe, "#" + ingredient.get("fluidTag").getAsString());
			}
		}
	}

	private static class FuelTimeComponent implements RecipeComponent<Integer> {
		@Override
		public @NotNull Class<?> componentClass() {
			return Integer.class;
		}

		@Override
		public @NotNull ComponentRole role() {
			return NumberComponent.INT.role();
		}

		@Override
		public @NotNull JsonElement write(@NotNull RecipeJS recipe, @NotNull Integer value) {
			return new JsonPrimitive(value);
		}

		@Override
		public @NotNull Integer read(@NotNull RecipeJS recipe, @NotNull Object from) {
			return NumberComponent.INT.read(recipe, from);
		}

		@Override
		public void writeToJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<Integer> holder, @NotNull JsonObject json) {
			json.addProperty("processingTime", holder.value);
		}

		@Override
		public void readFromJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<Integer> holder, @NotNull JsonObject json) {
			holder.value = json.has("processingTime") ? json.get("processingTime").getAsInt() : 0;
		}
	}

	private static class FuelSuperheatedComponent implements RecipeComponent<Boolean> {
		@Override
		public @NotNull Class<?> componentClass() {
			return Boolean.class;
		}

		@Override
		public @NotNull ComponentRole role() {
			return ComponentRole.OTHER;
		}

		@Override
		public @NotNull JsonElement write(@NotNull RecipeJS recipe, @NotNull Boolean value) {
			return new JsonPrimitive(value);
		}

		@Override
		public @NotNull Boolean read(@NotNull RecipeJS recipe, @Nullable Object from) {
			if (from == null) {
				return Boolean.FALSE;
			}
			return BooleanComponent.BOOLEAN.read(recipe, from);
		}

		@Override
		public void writeToJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<Boolean> holder, @NotNull JsonObject json) {
			String key = IBurnerFuelRecipe.SUPERHEATED_KEY;
			if (Boolean.TRUE.equals(holder.value)) {
				json.addProperty(key, true);
			} else {
				json.remove(key);
			}
		}

		@Override
		public void readFromJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<Boolean> holder, @NotNull JsonObject json) {
			String key = IBurnerFuelRecipe.SUPERHEATED_KEY;
			holder.value = json.has(key) && json.get(key).getAsBoolean();
		}
	}
}