package dev.celestiacraft.steam_letters.compat.kubejs.schema;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import org.jetbrains.annotations.NotNull;

public class BurnerSchema {
	public static final RecipeSchema SOLID_FUEL = new RecipeSchema(
			new RecipeKey<>(new FuelItemComponent(), "fuel"),
			new RecipeKey<>(NumberComponent.INT, "time")
	);

	public static final RecipeSchema FLUID_FUEL = new RecipeSchema(
			new RecipeKey<>(new FuelFluidComponent(), "fuel"),
			new RecipeKey<>(NumberComponent.INT, "time")
	);

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
			splitItem(ItemComponents.INPUT.write(recipe, holder.value), json);
		}

		@Override
		public void readFromJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<InputItem> holder, @NotNull JsonObject json) {
			if (json.has("item")) {
				holder.value = read(recipe, json.get("item"));
			} else if (json.has("tag")) {
				holder.value = read(recipe, "#" + json.get("tag").getAsString());
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

		/**
		 * 摊平成配方自己的两个字段之一, 本组件用的 key 名不会进 JSON
		 */
		@Override
		public void writeToJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<FuelFluid> holder, @NotNull JsonObject json) {
			FuelFluid value = holder.value;
			if (value.isTag()) {
				json.addProperty("fluidTag", value.getTag().toString());
			} else {
				json.addProperty("fluid", value.getFluid().getId());
			}
		}

		@Override
		public void readFromJson(@NotNull RecipeJS recipe, @NotNull RecipeComponentValue<FuelFluid> holder, @NotNull JsonObject json) {
			if (json.has("fluid")) {
				holder.value = read(recipe, json.get("fluid"));
			} else if (json.has("fluidTag")) {
				holder.value = read(recipe, "#" + json.get("fluidTag").getAsString());
			}
		}
	}
}