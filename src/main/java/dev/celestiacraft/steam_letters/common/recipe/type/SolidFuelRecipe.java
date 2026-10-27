package dev.celestiacraft.steam_letters.common.recipe.type;

import com.google.gson.JsonObject;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import dev.celestiacraft.steam_letters.common.register.SlRecipeTypes;
import lombok.Setter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

@Setter
public class SolidFuelRecipe extends ProcessingRecipe<RecipeWrapper> implements IBurnerFuelRecipe {
	private static final ThreadLocal<Boolean> DATAGEN_SUPERHEATED = new ThreadLocal<>();

	private boolean superheated;

	public SolidFuelRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(SlRecipeTypes.SOLID_FUEL_INFO, params);
		superheated = consumeSuperheated();
	}

	public static void markNextSuperheated(boolean superheated) {
		DATAGEN_SUPERHEATED.set(superheated);
	}

	private static boolean consumeSuperheated() {
		Boolean marked = DATAGEN_SUPERHEATED.get();
		if (marked == null) {
			return false;
		}
		DATAGEN_SUPERHEATED.remove();
		return marked;
	}

	@Override
	public boolean isSuperheated() {
		return superheated;
	}

	@Override
	public void readAdditional(@NotNull JsonObject json) {
		super.readAdditional(json);
		superheated = json.has(SUPERHEATED_KEY) && json.get(SUPERHEATED_KEY).getAsBoolean();
	}

	@Override
	public void writeAdditional(@NotNull JsonObject json) {
		super.writeAdditional(json);
		if (superheated) {
			json.addProperty(SUPERHEATED_KEY, true);
		}
	}

	@Override
	public void readAdditional(@NotNull FriendlyByteBuf buf) {
		super.readAdditional(buf);
		superheated = buf.readBoolean();
	}

	@Override
	public void writeAdditional(@NotNull FriendlyByteBuf buf) {
		super.writeAdditional(buf);
		buf.writeBoolean(superheated);
	}

	@Override
	protected int getMaxInputCount() {
		return 1;
	}

	@Override
	protected int getMaxOutputCount() {
		return 0;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	@Nullable
	public Ingredient ingredient() {
		return ingredients.isEmpty() ? null : ingredients.get(0);
	}

	public boolean matches(ItemStack stack) {
		Ingredient ingredient = ingredient();
		return ingredient != null && !stack.isEmpty() && ingredient.test(stack);
	}

	@Override
	public List<ItemStack> getFuelItems() {
		Ingredient ingredient = ingredient();
		return ingredient == null ? List.of() : List.of(ingredient.getItems());
	}

	@Override
	public int getTime() {
		return getProcessingDuration();
	}

	@Override
	public boolean matches(@NotNull RecipeWrapper wrapper, @NotNull Level level) {
		return false;
	}

	@Nullable
	public static SolidFuelRecipe find(Level level, ItemStack stack) {
		if (stack.isEmpty()) {
			return null;
		}

		List<SolidFuelRecipe> recipes = level.getRecipeManager().getAllRecipesFor(SlRecipeTypes.SOLID_FUEL.get());
		SolidFuelRecipe best = null;
		for (SolidFuelRecipe recipe : recipes) {
			if (!recipe.matches(stack)) {
				continue;
			}
			if (best == null || recipe.getTime() > best.getTime()) {
				best = recipe;
			}
		}
		return best;
	}
}