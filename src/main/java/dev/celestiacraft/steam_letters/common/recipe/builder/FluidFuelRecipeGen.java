package dev.celestiacraft.steam_letters.common.recipe.builder;

import com.simibubi.create.api.data.recipe.ProcessingRecipeGen;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import dev.celestiacraft.steam_letters.common.register.SlRecipeTypes;
import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

import java.util.function.UnaryOperator;

public abstract class FluidFuelRecipeGen extends ProcessingRecipeGen {
	protected FluidFuelRecipeGen(PackOutput output, String defaultNamespace) {
		super(output, defaultNamespace);
	}

	@Override
	protected @NotNull IRecipeTypeInfo getRecipeType() {
		return SlRecipeTypes.FLUID_FUEL_INFO;
	}

	protected GeneratedRecipe recipe(String name, @NotNull UnaryOperator<FluidFuelBuilder> transform) {
		return create(name, (ProcessingRecipeBuilder<FluidFuelRecipe> builder) -> {
			return transform.apply(new FluidFuelBuilder(builder)).unwrap();
		});
	}

	public static class FluidFuelBuilder {
		private final ProcessingRecipeBuilder<FluidFuelRecipe> builder;
		private boolean superheated;

		private FluidFuelBuilder(@NotNull ProcessingRecipeBuilder<FluidFuelRecipe> builder) {
			this.builder = builder;
		}

		public FluidFuelBuilder fluid(@NotNull Fluid fluid) {
			return ingredient(FluidIngredient.fromFluid(fluid, FluidFuelRecipe.FUEL_AMOUNT));
		}

		public FluidFuelBuilder fluid(@NotNull TagKey<Fluid> tag) {
			return ingredient(FluidIngredient.fromTag(tag, FluidFuelRecipe.FUEL_AMOUNT));
		}

		private FluidFuelBuilder ingredient(@NotNull FluidIngredient ingredient) {
			builder.withFluidIngredients(ingredient);
			return this;
		}

		public FluidFuelBuilder time(int ticks) {
			builder.duration(ticks);
			return this;
		}

		public FluidFuelBuilder superheated(boolean superheated) {
			this.superheated = superheated;
			return this;
		}

		public FluidFuelBuilder superheated() {
			superheated = true;
			return this;
		}

		public FluidFuelBuilder whenModLoaded(@NotNull String modid) {
			builder.whenModLoaded(modid);
			return this;
		}

		public FluidFuelBuilder whenModMissing(@NotNull String modid) {
			builder.whenModMissing(modid);
			return this;
		}

		public FluidFuelBuilder condition(@NotNull ICondition condition) {
			builder.withCondition(condition);
			return this;
		}

		private ProcessingRecipeBuilder<FluidFuelRecipe> unwrap() {
			FluidFuelRecipe.markNextSuperheated(superheated);
			return builder;
		}
	}
}