package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.common.block.boiler.SteamBoilerBlock;
import dev.celestiacraft.steam_letters.common.block.boiler.SteamBoilerBlockEntity;
import dev.celestiacraft.steam_letters.config.common.SteamBoilerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BoilerBuilder extends BoilerDeviceBuilder<SteamBoilerBlock, SteamBoilerBlockEntity> {
	public BoilerBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected String devicePath() {
		return "block/steam/boiler";
	}

	@Override
	protected SteamBoilerBlock createBlock(BlockBehaviour.Properties properties) {
		SteamEngineSpec spec = SteamEngineSpec.builder()
				.capacity(capacityOr(SteamBoilerConfig.CAPACITY::get))
				.production(productionOr(SteamBoilerConfig.PRODUCTION::get))
				.build();
		return new SteamBoilerBlock(properties, spec, () -> blockEntityType);
	}

	@Override
	protected SteamBoilerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SteamBoilerBlockEntity(blockEntityType, pos, state);
	}
}