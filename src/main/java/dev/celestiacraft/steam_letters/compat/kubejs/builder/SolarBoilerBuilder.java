package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.celestiacraft.steam_letters.common.block.boiler.SolarBoilerBlock;
import dev.celestiacraft.steam_letters.common.block.boiler.SolarBoilerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SolarBoilerBuilder extends BoilerDeviceBuilder<SolarBoilerBlock, SolarBoilerBlockEntity> {
	public SolarBoilerBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected String devicePath() {
		return "block/steam/solar_boiler";
	}

	@Override
	protected SolarBoilerBlock createBlock(BlockBehaviour.Properties properties) {
		return new SolarBoilerBlock(properties, (builder) -> builder
				.capacity(capacityOr(() -> SolarBoilerBlockEntity.DEFAULT_CAPACITY))
				.production(productionOr(() -> SolarBoilerBlockEntity.DEFAULT_PRODUCTION)),
				() -> blockEntityType);
	}

	@Override
	protected SolarBoilerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SolarBoilerBlockEntity(blockEntityType, pos, state);
	}
}