package dev.celestiacraft.steam_letters.common.recipe.builder;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.celestiacraft.steam_letters.common.register.SlRecipeTypes;
import dev.celestiacraft.steam_letters.common.recipe.type.SolidFuelRecipe;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public abstract class SolidFuelRecipeGen extends ProcessingRecipeGen {
	protected SolidFuelRecipeGen(PackOutput output, String defaultNamespace) {
		super(output, defaultNamespace);
	}

	@Override
	protected @NotNull IRecipeTypeInfo getRecipeType() {
		return SlRecipeTypes.SOLID_FUEL_INFO;
	}

	protected GeneratedRecipe recipe(String name, @NotNull UnaryOperator<SolidFuelBuilder> transform) {
		return create(name, (ProcessingRecipeBuilder<SolidFuelRecipe> builder) -> {
			return transform.apply(new SolidFuelBuilder(builder)).unwrap();
		});
	}

	public static class SolidFuelBuilder {
		private final ProcessingRecipeBuilder<SolidFuelRecipe> builder;
		private boolean superheated;

		private SolidFuelBuilder(@NotNull ProcessingRecipeBuilder<SolidFuelRecipe> builder) {
			this.builder = builder;
		}

		public SolidFuelBuilder item(@NotNull ItemLike item) {
			return ingredient(Ingredient.of(item));
		}

		public SolidFuelBuilder item(@NotNull TagKey<Item> tag) {
			return ingredient(Ingredient.of(tag));
		}

		public SolidFuelBuilder ingredient(@NotNull Ingredient ingredient) {
			builder.withItemIngredients(ingredient);
			return this;
		}

		public SolidFuelBuilder time(int ticks) {
			builder.duration(ticks);
			return this;
		}

		public SolidFuelBuilder superheated(boolean superheated) {
			this.superheated = superheated;
			return this;
		}

		public SolidFuelBuilder superheated() {
			superheated = true;
			return this;
		}

		public SolidFuelBuilder whenModLoaded(@NotNull String modid) {
			builder.whenModLoaded(modid);
			return this;
		}

		public SolidFuelBuilder whenModMissing(@NotNull String modid) {
			builder.whenModMissing(modid);
			return this;
		}

		public SolidFuelBuilder condition(@NotNull ICondition condition) {
			builder.withCondition(condition);
			return this;
		}

		private ProcessingRecipeBuilder<SolidFuelRecipe> unwrap() {
			SolidFuelRecipe.markNextSuperheated(superheated);
			return builder;
		}
	}
}