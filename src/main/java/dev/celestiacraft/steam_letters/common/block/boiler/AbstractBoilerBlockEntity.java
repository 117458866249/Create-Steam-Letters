package dev.celestiacraft.steam_letters.common.block.boiler;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.celestiacraft.steam_letters.api.machine.ISteamEngineBlock;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.api.machine.heat.IHeatSource;
import dev.celestiacraft.steam_letters.config.common.SteamConfig;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractBoilerBlockEntity extends SmartBlockEntity {
	public static final int TANK_INPUT = 0;
	public static final int TANK_OUTPUT = 1;

	private static final String NBT_INPUT_TANK = "InputTank";
	private static final String NBT_OUTPUT_TANK = "OutputTank";
	private static final String NBT_PENDING = "Pending";

	@Getter
	protected final SteamEngineSpec spec;
	@Getter
	private final FluidTank inputTank;
	@Getter
	private final FluidTank outputTank;
	private LazyOptional<IFluidHandler> fluidCapability;

	private double pending;

	protected AbstractBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		spec = ((ISteamEngineBlock) state.getBlock()).getEngineSpec();
		int capacity = spec.getCapacity();

		inputTank = new FluidTank(capacity) {
			@Override
			protected void onContentsChanged() {
				setChanged();
			}

			@Override
			public boolean isFluidValid(FluidStack stack) {
				return !stack.isEmpty() && stack.getFluid().is(FluidTags.WATER);
			}
		};

		outputTank = new FluidTank(capacity) {
			@Override
			protected void onContentsChanged() {
				setChanged();
			}

			@Override
			public boolean isFluidValid(FluidStack stack) {
				return isOutputSteam(stack);
			}
		};

		fluidCapability = LazyOptional.of(this::createFluidHandler);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
	}

	@Override
	public void tick() {
		super.tick();
		if (level == null || level.isClientSide()) {
			return;
		}
		tickProduction();
	}

	private void tickProduction() {
		double efficiency = getCurrentEfficiency();
		double production = spec.getProduction();

		if (efficiency <= 0.0D || production <= 0.0D) {
			pending = 0.0D;
			return;
		}

		double produced = production * efficiency;
		pending += produced;

		int requested = (int) Math.floor(pending);
		if (requested <= 0) {
			return;
		}

		FluidStack steam = createSteam(requested);
		if (steam.isEmpty()) {
			pending = 0.0D;
			return;
		}

		// 先模拟, 只有水够且输出罐真的放得下时才会执行, 因此永远不会多消耗水
		FluidStack drained = inputTank.drain(requested, IFluidHandler.FluidAction.SIMULATE);
		int accepted = outputTank.fill(steam, IFluidHandler.FluidAction.SIMULATE);
		int producible = Math.min(drained.getAmount(), accepted);

		if (producible <= 0) {
			// 被水或空间卡住: 余额只保留一个 tick 的量, 避免长时间停机后突然爆发
			pending = Math.min(pending, produced);
			return;
		}

		FluidStack used = inputTank.drain(producible, IFluidHandler.FluidAction.EXECUTE);
		outputTank.fill(createSteam(used.getAmount()), IFluidHandler.FluidAction.EXECUTE);
		pending -= used.getAmount();
	}

	/**
	 * @return 当前运行效率, 0 表示不产汽
	 */
	protected abstract double getCurrentEfficiency();

	protected FluidStack createSteam(int amount) {
		Fluid fluid = SteamConfig.getOutputSteamFluid();
		return fluid == null ? FluidStack.EMPTY : new FluidStack(fluid, amount);
	}

	protected boolean isOutputSteam(FluidStack stack) {
		Fluid fluid = SteamConfig.getOutputSteamFluid();
		return fluid != null && !stack.isEmpty() && stack.getFluid() == fluid;
	}

	/**
	 * @return 正下方的有效燃烧室, 没有则返回 null
	 */
	@Nullable
	protected IHeatSource getHeatSource() {
		if (level == null) {
			return null;
		}
		BlockEntity below = level.getBlockEntity(worldPosition.below());
		return below instanceof IHeatSource source ? source : null;
	}

	protected IFluidHandler createFluidHandler() {
		return new BoilerFluidHandler(this);
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return fluidCapability.cast();
		}
		return super.getCapability(capability, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		fluidCapability.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		fluidCapability = LazyOptional.of(this::createFluidHandler);
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.put(NBT_INPUT_TANK, inputTank.writeToNBT(new CompoundTag()));
		tag.put(NBT_OUTPUT_TANK, outputTank.writeToNBT(new CompoundTag()));
		tag.putDouble(NBT_PENDING, pending);
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		if (tag.contains(NBT_INPUT_TANK)) {
			inputTank.readFromNBT(tag.getCompound(NBT_INPUT_TANK));
		}
		if (tag.contains(NBT_OUTPUT_TANK)) {
			outputTank.readFromNBT(tag.getCompound(NBT_OUTPUT_TANK));
		}
		pending = tag.getDouble(NBT_PENDING);
	}
}