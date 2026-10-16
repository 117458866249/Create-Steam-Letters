package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.common.block.boiler.SolarBoilerBlock;
import dev.celestiacraft.steam_letters.common.block.boiler.SolarBoilerBlockEntity;
import dev.celestiacraft.steam_letters.config.common.SolarBoilerConfig;
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
		SteamEngineSpec spec = SteamEngineSpec.builder()
				.capacity(capacityOr(SolarBoilerConfig.CAPACITY::get))
				.production(productionOr(SolarBoilerConfig.PRODUCTION::get))
				.build();
		return new SolarBoilerBlock(properties, spec, () -> blockEntityType);
	}

	@Override
	protected SolarBoilerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SolarBoilerBlockEntity(blockEntityType, pos, state);
	}
}