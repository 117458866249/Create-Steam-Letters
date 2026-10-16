package dev.celestiacraft.steam_letters.common.recipe.type;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SolidFuelRecipeSerializer implements RecipeSerializer<SolidFuelRecipe> {
	private static final Set<String> ALLOWED = new HashSet<>(Arrays.asList("type", "item", "tag", "time"));

	@Override
	public @NotNull SolidFuelRecipe fromJson(@NotNull ResourceLocation id, JsonObject json) {
		for (String field : json.keySet()) {
			if (!ALLOWED.contains(field)) {
				throw new JsonSyntaxException("Unsupported field '" + field + "' in solid_fuel recipe " + id);
			}
		}

		boolean hasItem = json.has("item");
		boolean hasTag = json.has("tag");
		if (hasItem == hasTag) {
			throw new JsonSyntaxException("solid_fuel recipe " + id + " must define exactly one of 'item' or 'tag'");
		}

		JsonObject selector = new JsonObject();
		selector.add(hasItem ? "item" : "tag", json.get(hasItem ? "item" : "tag"));
		Ingredient ingredient = Ingredient.fromJson(selector);

		if (!json.has("time")) {
			throw new JsonSyntaxException("solid_fuel recipe " + id + " must define 'time'");
		}
		int time = GsonHelper.getAsInt(json, "time");
		if (time <= 0) {
			throw new JsonSyntaxException("solid_fuel recipe " + id + " has a non-positive 'time' of " + time);
		}

		return new SolidFuelRecipe(id, ingredient, time);
	}

	@Override
	public SolidFuelRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
		Ingredient ingredient = Ingredient.fromNetwork(buf);
		int time = buf.readVarInt();
		return new SolidFuelRecipe(id, ingredient, time);
	}

	@Override
	public void toNetwork(@NotNull FriendlyByteBuf buf, SolidFuelRecipe recipe) {
		recipe.getIngredient().toNetwork(buf);
		buf.writeVarInt(recipe.getTime());
	}
}