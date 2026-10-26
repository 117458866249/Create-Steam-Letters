package dev.celestiacraft.steam_letters.common.block.burner;

import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * 流体燃烧室的方块实体.
 * <p>
 * 可燃性完全由 {@code steam_letters:fluid_fuel} 配方决定, 不参考流体的类型或属性;
 * 燃料按 tick 持续推进: 一份 {@link FluidFuelRecipe#FUEL_AMOUNT} mB 恰好铺满配方的
 * {@code time} tick, 所以每 tick 从罐里抽 {@code FUEL_AMOUNT / time} mB,
 * 一整份烧完的时间与配方声明的时长完全一致.
 * <p>
 * 罐里不足一整份时按同样速率继续烧, <b>有多少烧多少</b>, 烧空即停; 因此不存在
 * 凑不满一份就永远点不着的情况.
 */
public class FluidBurnerBlockEntity extends AbstractBurnerBlockEntity {
	/**
	 * 内置青铜流体燃烧室的默认燃料罐容量 (mB).
	 */
	public static final int DEFAULT_CAPACITY = 1000;

	private static final String NBT_TANK = "FuelTank";
	private static final String NBT_BURN_TOTAL = "BurnTotal";

	@Getter
	private final FuelFluidTank fuelTank;

	/**
	 * 配方查找缓存, 以罐内流体为键.
	 */
	private FluidStack recipeKey = FluidStack.EMPTY;
	@Nullable
	private FluidFuelRecipe recipeValue;
	private boolean recipeCacheValid;

	/**
	 * 当前这一轮燃烧的名义总时长 (tick);
	 * 0 表示当前没有进行中的一轮. 需要持久化, 否则读档后本轮剩余时间无从恢复.
	 */
	private int burnTotalTime;

	private LazyOptional<IFluidHandler> fluidCapability;

	public FluidBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		int capacity = spec.getCapacity();
		if (capacity <= 0) {
			capacity = DEFAULT_CAPACITY;
		}

		fuelTank = new FuelFluidTank(this, capacity, this::isFuel);

		fluidCapability = LazyOptional.of(() -> fuelTank);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		invalidateRecipeCache();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		invalidateRecipeCache();
	}

	@Override
	protected boolean tickFuel() {
		if (level == null) {
			return false;
		}

		if (burnTime <= 0 && !beginRound()) {
			return false;
		}

		burnTime--;
		return true;
	}

	/**
	 * 开启新的一轮燃烧: 找配方, 并一次性扣掉一份燃料 ({@link FluidFuelRecipe#FUEL_AMOUNT} mB).
	 * <p>
	 * 罐里不足一整份时不开始新一轮, 剩下的燃料原样留在罐里等补满, 不会被提前烧掉.
	 * 开轮时一次性扣料, 所以一轮的时长完全由配方的 {@code processingTime} 决定.
	 */
	private boolean beginRound() {
		FluidStack stored = fuelTank.getFluid();
		if (stored.isEmpty()) {
			return false;
		}

		FluidFuelRecipe recipe = findRecipe(stored);
		if (recipe == null) {
			return false;
		}

		int total = recipe.getTime();
		if (total <= 0) {
			return false;
		}

		if (stored.getAmount() < FluidFuelRecipe.FUEL_AMOUNT) {
			return false;
		}

		FluidStack drained = fuelTank.drain(FluidFuelRecipe.FUEL_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
		if (drained.getAmount() < FluidFuelRecipe.FUEL_AMOUNT) {
			return false;
		}

		burnTotalTime = total;
		burnTime = total;
		setChanged();
		return true;
	}

	/**
	 * 该流体是否为有效燃料. 判定只走配方系统, 且不做缓存写入, 避免污染正在燃烧的流体的缓存.
	 */
	public boolean isFuel(FluidStack stack) {
		if (level == null || stack == null || stack.isEmpty()) {
			return false;
		}
		return FluidFuelRecipe.find(level, stack) != null;
	}

	@Nullable
	private FluidFuelRecipe findRecipe(FluidStack stack) {
		if (level == null || stack.isEmpty()) {
			return null;
		}
		if (recipeCacheValid && !recipeKey.isEmpty() && recipeKey.isFluidEqual(stack)) {
			return recipeValue;
		}

		recipeKey = stack.copy();
		recipeValue = FluidFuelRecipe.find(level, stack);
		recipeCacheValid = true;
		return recipeValue;
	}

	public void invalidateRecipeCache() {
		recipeCacheValid = false;
		recipeKey = FluidStack.EMPTY;
		recipeValue = null;
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return fluidCapability.cast();
		}
		return super.getCapability(capability, direction);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		fluidCapability.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		fluidCapability = LazyOptional.of(() -> fuelTank);
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.put(NBT_TANK, fuelTank.writeToNBT(new CompoundTag()));
		tag.putInt(NBT_BURN_TOTAL, burnTotalTime);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		if (tag.contains(NBT_TANK)) {
			fuelTank.readFromNBT(tag.getCompound(NBT_TANK));
		}
		burnTotalTime = tag.getInt(NBT_BURN_TOTAL);
		invalidateRecipeCache();
	}
}