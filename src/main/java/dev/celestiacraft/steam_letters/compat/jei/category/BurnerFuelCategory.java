package dev.celestiacraft.steam_letters.compat.jei.category;

import dev.celestiacraft.steam_letters.common.recipe.type.IBurnerFuelRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class BurnerFuelCategory<R extends IBurnerFuelRecipe> implements IRecipeCategory<R> {
	private static final int WIDTH = 82;
	private static final int HEIGHT = 26;

	private static final int SLOT_X = 1;
	private static final int SLOT_Y = 5;
	private static final int TIME_X = 44;
	private static final int TIME_Y = 9;

	private final RecipeType<R> recipeType;
	private final Component title;
	private final IDrawable icon;

	public BurnerFuelCategory(RecipeType<R> recipeType, Component title, IDrawable icon) {
		this.recipeType = recipeType;
		this.title = title;
		this.icon = icon;
	}

	@Override
	public @NotNull RecipeType<R> getRecipeType() {
		return recipeType;
	}

	@Override
	public @NotNull Component getTitle() {
		return title;
	}

	@Override
	public @Nullable IDrawable getIcon() {
		return icon;
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull R recipe, @NotNull IFocusGroup group) {
		List<ItemStack> items = recipe.getFuelItems();
		if (!items.isEmpty()) {
			builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X, SLOT_Y)
					.addItemStacks(items);
			return;
		}

		List<FluidStack> fluids = recipe.getFuelFluids();
		if (fluids.isEmpty()) {
			return;
		}

		FluidStack first = fluids.get(0);
		builder.addSlot(RecipeIngredientRole.INPUT, SLOT_X, SLOT_Y)
				.addFluidStack(first.getFluid(), first.getAmount());
	}

	@Override
	public void draw(@NotNull R recipe, @NotNull IRecipeSlotsView slotsView, @NotNull GuiGraphics graphics, double mouseX, double mouseY) {
		Component time = Component.literal(String.format(Locale.ROOT, "%.1fs", recipe.getTime() / 20.0D));
		graphics.drawString(Minecraft.getInstance().font, time, TIME_X, TIME_Y, 0x404040, false);
	}
}