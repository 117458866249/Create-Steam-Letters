package dev.celestiacraft.steam_letters.compat.kubejs.recipe.schema;

import dev.celestiacraft.steam_letters.compat.kubejs.recipe.schema.schema.BurnerSchema;
import dev.latvian.mods.kubejs.recipe.RecipeJS;

import javax.annotation.Nullable;

public class BurnerRecipeJS extends RecipeJS {
	public RecipeJS superheated() {
		return superheated(Boolean.TRUE);
	}

	public RecipeJS superheated(@Nullable Boolean superheated) {
		return setValue(BurnerSchema.SUPERHEATED, Boolean.TRUE.equals(superheated));
	}
}