package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.celestiacraft.steam_letters.common.block.burner.AbstractBurnerBlockEntity;
import dev.celestiacraft.steam_letters.common.block.burner.FluidBurnerBlock;
import dev.celestiacraft.steam_letters.common.block.burner.FluidBurnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class FluidBurnerBuilder extends BurnerDeviceBuilder<FluidBurnerBlock, FluidBurnerBlockEntity, FluidBurnerBuilder> {
	public FluidBurnerBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected FluidBurnerBuilder self() {
		return this;
	}

	@Override
	protected String devicePath() {
		return "block/steam/fluid_burner";
	}

	@Override
	protected FluidBurnerBlock createBlock(BlockBehaviour.Properties properties) {
		return new FluidBurnerBlock(properties, (builder) -> {
			builder.capacity(capacityOr(() -> FluidBurnerBlockEntity.DEFAULT_CAPACITY))
					.maxHeat(maxHeatOr(() -> AbstractBurnerBlockEntity.DEFAULT_MAX_HEAT));
		}, () -> blockEntityType);
	}

	@Override
	protected FluidBurnerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FluidBurnerBlockEntity(blockEntityType, pos, state);
	}
}