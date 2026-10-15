package dev.celestiacraft.csa.common.block.steam_hammer;

import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import dev.celestiacraft.csa.api.machine.ISteamBasinMachine;
import dev.celestiacraft.csa.api.machine.ISteamPressMachine;
import dev.celestiacraft.csa.config.common.SteamHammerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SteamHammerBlockEntity extends MechanicalPressBlockEntity implements ISteamPressMachine, ISteamBasinMachine {
	private FluidStack steam = FluidStack.EMPTY;
	private LazyOptional<IFluidHandler> steamCapability = LazyOptional.of(this::steamFluidHandler);

	public SteamHammerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public FluidStack getSteamStack() {
		return steam;
	}

	@Override
	public void setSteamStack(FluidStack stack) {
		steam = stack.isEmpty() ? FluidStack.EMPTY : stack.copy();
		setChanged();
	}

	@Override
	public int getSteamCapacity() {
		return SteamHammerConfig.STEAM_CAPACITY.get();
	}

	@Override
	public int getSteamConsumption() {
		return SteamHammerConfig.STEAM_CONSUMPTION.get();
	}

	@Override
	public int getParallelCount() {
		return SteamHammerConfig.STEAM_HAMMER_PARALLEL.get();
	}

	@Override
	protected boolean updateBasin() {
		if (!hasEnoughSteam()) {
			return false;
		}
		return super.updateBasin();
	}

	@Override
	public boolean continueWithPreviousRecipe() {
		if (!hasEnoughSteam()) {
			return false;
		}
		return super.continueWithPreviousRecipe();
	}

	@Override
	public Recipe<?> getBasinRecipe() {
		return currentRecipe;
	}

	@Override
	public Optional<BasinBlockEntity> getBasin() {
		return super.getBasin();
	}

	@Override
	public <C extends Container> boolean matchBasinRecipe(Recipe<C> recipe) {
		return super.matchBasinRecipe(recipe);
	}

	@Override
	public void onBasinRecipeApplied(int applied) {
		getProcessedRecipeTrigger().ifPresent(this::award);
	}

	@Override
	public void applyBasinRecipe() {
		ISteamBasinMachine.super.applyBasinRecipe();
	}

	@Override
	public boolean canProcessInBulk() {
		return ISteamPressMachine.super.canProcessInBulk();
	}

	@Override
	public boolean tryProcessOnBelt(TransportedItemStack input, List<ItemStack> outputList, boolean simulate) {
		return ISteamPressMachine.super.tryProcessOnBelt(input, outputList, simulate);
	}

	@Override
	public boolean tryProcessInWorld(ItemEntity itemEntity, boolean simulate) {
		return ISteamPressMachine.super.tryProcessInWorld(itemEntity, simulate);
	}

	/**
	 * 工作盆路径的蒸汽由 {@code applyBasinRecipe} 按操作扣减.
	 */
	@Override
	public void onPressingCompleted() {
		super.onPressingCompleted();
		if (!pressingBehaviour.onBasin()) {
			consumeSteam();
		}
	}

	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return steamCapability.cast();
		}
		return super.getCapability(capability, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		steamCapability.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		steamCapability = LazyOptional.of(this::steamFluidHandler);
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.put("Steam", steam.writeToNBT(new CompoundTag()));
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		steam = FluidStack.loadFluidStackFromNBT(tag.getCompound("Steam"));
	}
}