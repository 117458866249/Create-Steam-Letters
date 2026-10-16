package dev.celestiacraft.steam_letters.common.recipe.type;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FluidFuelRecipeSerializer implements RecipeSerializer<FluidFuelRecipe> {
	private static final Set<String> ALLOWED = new HashSet<>(List.of(
			"type",
			"fluid",
			"fluidTag",
			"time"
	));

	@Override
	public @NotNull FluidFuelRecipe fromJson(@NotNull ResourceLocation id, JsonObject json) {
		for (String field : json.keySet()) {
			if (!ALLOWED.contains(field)) {
				throw new JsonSyntaxException("Unsupported field '" + field + "' in fluid_fuel recipe " + id);
			}
		}

		boolean hasFluid = json.has("fluid");
		boolean hasTag = json.has("fluidTag");
		if (hasFluid == hasTag) {
			throw new JsonSyntaxException("fluid_fuel recipe " + id + " must define exactly one of 'fluid' or 'fluidTag'");
		}

		JsonObject selector = new JsonObject();
		selector.add(hasFluid ? "fluid" : "fluidTag", json.get(hasFluid ? "fluid" : "fluidTag"));
		selector.addProperty("amount", FluidFuelRecipe.FUEL_AMOUNT);
		FluidIngredient ingredient = FluidIngredient.deserialize(selector);

		if (!json.has("time")) {
			throw new JsonSyntaxException("fluid_fuel recipe " + id + " must define 'time'");
		}
		int time = GsonHelper.getAsInt(json, "time");
		if (time <= 0) {
			throw new JsonSyntaxException("fluid_fuel recipe " + id + " has a non-positive 'time' of " + time);
		}

		return new FluidFuelRecipe(id, ingredient, time);
	}

	@Override
	public FluidFuelRecipe fromNetwork(@NotNull ResourceLocation id, @NotNull FriendlyByteBuf buf) {
		FluidIngredient ingredient = FluidIngredient.read(buf);
		int time = buf.readVarInt();
		return new FluidFuelRecipe(id, ingredient, time);
	}

	@Override
	public void toNetwork(@NotNull FriendlyByteBuf buf, FluidFuelRecipe recipe) {
		synchronized (recipe) {
			recipe.getIngredient().write(buf);
		}
		buf.writeVarInt(recipe.getTime());
	}
}
