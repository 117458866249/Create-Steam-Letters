package dev.celestiacraft.steam_letters.common.recipe.type;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.fluid.FluidIngredient;
import dev.celestiacraft.steam_letters.common.recipe.SlRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 流体燃料配方.
 * <p>
 * 一份 {@link #FUEL_AMOUNT} mB 的该流体恰好能铺满 {@link #getTime()} tick 的燃烧,
 * 所以每 tick 从罐里抽 {@code FUEL_AMOUNT / time} mB.
 * <p>
 * 本类走的是 Create 的处理配方体系, JSON 形状为
 * {@code {"ingredients": [{"fluidTag": "...", "amount": 1000}], "results": [], "processingTime": 600}},
 * 因此可以直接用 {@code ProcessingRecipeGen} 生成, 也能被 {@code ProcessingRecipeSerializer} 读写.
 */
public class FluidFuelRecipe extends ProcessingRecipe<RecipeWrapper> implements IBurnerFuelRecipe {
	/**
	 * 一份燃料的流体量, 同时也是配方里 fluid ingredient 的 amount.
	 */
	public static final int FUEL_AMOUNT = 1000;

	public FluidFuelRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
		super(SlRecipeTypes.FLUID_FUEL_INFO, params);
	}

	@Override
	protected int getMaxInputCount() {
		return 0;
	}

	@Override
	protected int getMaxOutputCount() {
		return 0;
	}

	@Override
	protected int getMaxFluidInputCount() {
		return 1;
	}

	@Override
	protected boolean canSpecifyDuration() {
		return true;
	}

	/**
	 * 本配方声明的唯一流体原料, 配方损坏或未声明时为 {@code null}.
	 */
	@Nullable
	public FluidIngredient ingredient() {
		return fluidIngredients.isEmpty() ? null : fluidIngredients.get(0);
	}

	public boolean matches(@Nullable FluidStack stack) {
		FluidIngredient ingredient = ingredient();
		return ingredient != null && stack != null && !stack.isEmpty() && ingredient.test(stack);
	}

	@Override
	public List<FluidStack> getFuelFluids() {
		FluidIngredient ingredient = ingredient();
		return ingredient == null ? List.of() : ingredient.getMatchingFluidStacks();
	}

	@Override
	public int getTime() {
		return getProcessingDuration();
	}

	/**
	 * 燃烧室不走配方匹配, 只按流体判定, 详见 {@link #matches(FluidStack)}.
	 */
	@Override
	public boolean matches(@NotNull RecipeWrapper inv, @NotNull Level level) {
		return false;
	}

	/**
	 * 在全部流体燃料配方里找出匹配该流体的一条.
	 * <p>
	 * 调用方必须自行缓存结果, 该方法每次都完整遍历配方列表, 不可在 tick 中反复调用.
	 */
	@Nullable
	public static FluidFuelRecipe find(Level level, FluidStack stack) {
		if (stack == null || stack.isEmpty()) {
			return null;
		}

		List<FluidFuelRecipe> recipes = level.getRecipeManager().getAllRecipesFor(SlRecipeTypes.FLUID_FUEL.get());
		FluidFuelRecipe best = null;
		for (FluidFuelRecipe recipe : recipes) {
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