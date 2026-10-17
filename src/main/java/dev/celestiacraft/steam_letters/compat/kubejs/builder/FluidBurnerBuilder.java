package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.common.block.burner.FluidBurnerBlock;
import dev.celestiacraft.steam_letters.common.block.burner.FluidBurnerBlockEntity;
import dev.celestiacraft.steam_letters.common.block.burner.AbstractBurnerBlockEntity;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class FluidBurnerBuilder extends BurnerDeviceBuilder<FluidBurnerBlock, FluidBurnerBlockEntity> {
	public FluidBurnerBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected String devicePath() {
		return "block/steam/fluid_burner";
	}

	@Info("内部流体容量, 单位 mB.")
	public FluidBurnerBuilder capacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	@Override
	protected FluidBurnerBlock createBlock(BlockBehaviour.Properties properties) {
		SteamEngineSpec spec = SteamEngineSpec.builder()
				.capacity(capacityOr(() -> FluidBurnerBlockEntity.DEFAULT_CAPACITY))
				.maxHeat(maxHeatOr(() -> AbstractBurnerBlockEntity.DEFAULT_MAX_HEAT))
				.build();
		return new FluidBurnerBlock(properties, spec, () -> blockEntityType);
	}

	@Override
	protected FluidBurnerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new FluidBurnerBlockEntity(blockEntityType, pos, state);
	}
}