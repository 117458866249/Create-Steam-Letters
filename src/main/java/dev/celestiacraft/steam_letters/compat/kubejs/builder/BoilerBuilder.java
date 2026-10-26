package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.celestiacraft.steam_letters.common.block.boiler.SteamBoilerBlock;
import dev.celestiacraft.steam_letters.common.block.boiler.SteamBoilerBlockEntity;
import dev.celestiacraft.steam_letters.tags.SlBlockTags;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BoilerBuilder extends BoilerDeviceBuilder<SteamBoilerBlock, SteamBoilerBlockEntity, BoilerBuilder> {
	public BoilerBuilder(ResourceLocation id) {
		super(id);
		itemTag(SlItemTags.STEAM_PRODUCTION.location());
		itemTag(SlItemTags.STEAM_BOILER.location());
		blockTag(SlBlockTags.STEAM_PRODUCTION.location());
		blockTag(SlBlockTags.STEAM_BOILER.location());
	}

	@Override
	protected BoilerBuilder self() {
		return this;
	}

	@Override
	protected String devicePath() {
		return "block/steam/boiler";
	}

	@Override
	protected SteamBoilerBlock createBlock(BlockBehaviour.Properties properties) {
		return new SteamBoilerBlock(properties, (builder) -> {
			builder.capacity(capacityOr(() -> SteamBoilerBlockEntity.DEFAULT_CAPACITY))
					.production(productionOr(() -> SteamBoilerBlockEntity.DEFAULT_PRODUCTION));
		}, () -> blockEntityType);
	}

	@Override
	protected SteamBoilerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SteamBoilerBlockEntity(blockEntityType, pos, state);
	}
}