package dev.celestiacraft.steam_letters.common.block.engine;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

/**
 * 飞轮引擎的对外流体接口: 只允许灌入蒸汽, 任何抽取请求都返回空.
 * <p>
 * 蒸汽是引擎的燃料而不是库存, 允许外部 drain 会让管道 / 桶把正在工作的引擎抽空,
 * 因此这里把 drain 全部封死; 引擎自己消耗蒸汽时直接操作内部罐, 不经过本接口.
 */
public class SteamEngineFluidHandler implements IFluidHandler {
	private final FluidTank tank;

	public SteamEngineFluidHandler(FluidTank tank) {
		this.tank = tank;
	}

	@Override
	public int getTanks() {
		return 1;
	}

	@Override
	public @NotNull FluidStack getFluidInTank(int tank) {
		return this.tank.getFluid();
	}

	@Override
	public int getTankCapacity(int tank) {
		return this.tank.getCapacity();
	}

	@Override
	public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
		return this.tank.isFluidValid(stack);
	}

	@Override
	public int fill(FluidStack resource, FluidAction action) {
		return tank.fill(resource, action);
	}

	@Override
	public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
		return FluidStack.EMPTY;
	}

	@Override
	public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
		return FluidStack.EMPTY;
	}
}
