package dev.celestiacraft.steam_letters.common.block.boiler;

import dev.celestiacraft.steam_letters.common.fluid.BlockEntityFluidTank;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Predicate;

public class BoilerFluidTank extends BlockEntityFluidTank {
	public BoilerFluidTank(BlockEntity owner, int capacity, Predicate<FluidStack> validator) {
		super(owner, capacity, validator);
	}

	public static boolean isWater(FluidStack stack) {
		return !stack.isEmpty() && stack.getFluid().is(FluidTags.WATER);
	}
}