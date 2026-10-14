package dev.celestiacraft.csa.common.block.steam_mixer;

import com.simibubi.create.content.kinetics.mixer.MechanicalMixerBlockEntity;
import com.simibubi.create.content.processing.basin.BasinBlockEntity;
import dev.celestiacraft.csa.api.machine.ISteamBasinMachine;
import dev.celestiacraft.csa.config.common.SteamMixerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
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

import java.util.Optional;

public class SteamMixerBlockEntity extends MechanicalMixerBlockEntity implements ISteamBasinMachine {
	private FluidStack steam = FluidStack.EMPTY;
	private LazyOptional<IFluidHandler> steamCapability = LazyOptional.of(this::steamFluidHandler);

	public SteamMixerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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
		return SteamMixerConfig.STEAM_CAPACITY.get();
	}

	@Override
	public int getSteamConsumption() {
		return SteamMixerConfig.STEAM_CONSUMPTION.get();
	}

	@Override
	public int getParallelCount() {
		return SteamMixerConfig.STEAM_HAMMER_PARALLEL.get();
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
	public void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.put("Steam", steam.writeToNBT(new CompoundTag()));
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		steam = FluidStack.loadFluidStackFromNBT(tag.getCompound("Steam"));
	}
}