package dev.celestiacraft.steam_letters.common.block.burner;

import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import dev.celestiacraft.steam_letters.config.common.SteamBurnerConfig;
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
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * 流体燃烧室的方块实体.
 * <p>
 * 可燃性完全由 {@code steam_letters:fluid_fuel} 配方决定, 不参考流体的类型或属性;
 * 每开始新一轮燃烧固定消耗 {@link FluidFuelRecipe#FUEL_AMOUNT} mB, 不足则不开新一轮.
 * 当前燃烧的剩余时间是独立字段, 与罐内流体存量无关.
 */
public class FluidBurnerBlockEntity extends AbstractBurnerBlockEntity {
	private static final String NBT_TANK = "FuelTank";

	@Getter
	private final FluidTank fuelTank;

	/**
	 * 配方查找缓存, 以罐内流体为键.
	 */
	private FluidStack recipeKey = FluidStack.EMPTY;
	@Nullable
	private FluidFuelRecipe recipeValue;
	private boolean recipeCacheValid;

	private LazyOptional<IFluidHandler> fluidCapability;

	public FluidBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		int capacity = spec.getCapacity();
		if (capacity <= 0) {
			capacity = SteamBurnerConfig.FLUID_CAPACITY.get();
		}

		fuelTank = new FluidTank(capacity) {
			@Override
			protected void onContentsChanged() {
				invalidateRecipeCache();
				setChanged();
			}

			@Override
			public boolean isFluidValid(FluidStack stack) {
				return isFuel(stack);
			}
		};

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

	// ------------------------------------------------------------------ 燃料

	@Override
	protected boolean tickFuel() {
		if (burnTime > 0) {
			burnTime--;
			return true;
		}

		if (fuelTank.isEmpty()) {
			return false;
		}

		FluidStack stored = fuelTank.getFluid();
		if (stored.getAmount() < FluidFuelRecipe.FUEL_AMOUNT) {
			return false;
		}

		FluidFuelRecipe recipe = findRecipe(stored);
		if (recipe == null) {
			return false;
		}

		fuelTank.drain(FluidFuelRecipe.FUEL_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
		burnTime = recipe.getTime();
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
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		if (tag.contains(NBT_TANK)) {
			fuelTank.readFromNBT(tag.getCompound(NBT_TANK));
		}
		invalidateRecipeCache();
	}
}