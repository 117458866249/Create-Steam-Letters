package dev.celestiacraft.steam_letters.common.block.steam_pump;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.fluids.FluidTransportBehaviour;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.simibubi.create.infrastructure.config.AllConfigs;
import dev.celestiacraft.steam_letters.tags.SlFluidTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class BronzeSteamPumpBlockEntity extends PumpBlockEntity {
	private static final float DEFAULT_PUMP_SPEED = 256.0F;
	private static final int PRESSURE_REFRESH_INTERVAL = 20;

	public BronzeSteamPumpBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void initialize() {
		super.initialize();

		if (level != null && !level.isClientSide()) {
			updatePressureChange();
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (level == null || level.isClientSide()) {
			return;
		}

		if (level.getGameTime() % PRESSURE_REFRESH_INTERVAL == 0) {
			updatePressureChange();
		}
	}

	/**
	 * 青铜蒸汽泵不吃应力. 恒定以机械动力配置允许的最大转速运转
	 * 配置尚未加载时退回默认值
	 */
	public static float getPumpSpeed() {
		try {
			return AllConfigs.server().kinetics.maxRotationSpeed.get();
		} catch (IllegalStateException | NullPointerException e) {
			return DEFAULT_PUMP_SPEED;
		}
	}

	@Override
	public float getSpeed() {
		return getPumpSpeed();
	}

	@Override
	public float getTheoreticalSpeed() {
		return getPumpSpeed();
	}

	@Override
	public float calculateStressApplied() {
		return 0.0F;
	}

	@Override
	protected void distributePressureTo(Direction direction) {
		if (!acceptsSteamOn(direction)) {
			return;
		}

		super.distributePressureTo(direction);
	}

	private boolean acceptsSteamOn(Direction direction) {
		if (level == null) {
			return false;
		}

		BlockPos target = worldPosition.relative(direction);
		FluidTransportBehaviour pipe = FluidPropagator.getPipe(level, target);

		if (pipe != null) {
			return isSteamOrEmpty(pipe.getProvidedOutwardFluid(direction.getOpposite()));
		}

		BlockEntity blockEntity = level.getBlockEntity(target);
		if (blockEntity == null) {
			return false;
		}

		IFluidHandler handler = blockEntity.getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite()).orElse(null);
		if (handler == null) {
			return false;
		}

		return isSteamOrEmpty(handler.getFluidInTank(0));
	}

	private static boolean isSteamOrEmpty(FluidStack stack) {
		return stack.isEmpty()
				|| stack.getFluid().is(SlFluidTags.STEAM)
				|| stack.getFluid().is(SlFluidTags.SUPERHEATED_STEAM);
	}
}