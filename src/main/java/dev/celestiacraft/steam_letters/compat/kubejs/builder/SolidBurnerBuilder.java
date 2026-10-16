package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.common.block.burner.SolidBurnerBlock;
import dev.celestiacraft.steam_letters.common.block.burner.SolidBurnerBlockEntity;
import dev.celestiacraft.steam_letters.common.block.burner.AbstractBurnerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SolidBurnerBuilder extends BurnerDeviceBuilder<SolidBurnerBlock, SolidBurnerBlockEntity> {
	public SolidBurnerBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected String devicePath() {
		return "block/steam/solid_burner";
	}

	@Override
	protected SolidBurnerBlock createBlock(BlockBehaviour.Properties properties) {
		SteamEngineSpec spec = SteamEngineSpec.builder()
				.maxHeat(maxHeatOr(() -> AbstractBurnerBlockEntity.DEFAULT_MAX_HEAT))
				.build();
		return new SolidBurnerBlock(properties, spec, () -> blockEntityType);
	}

	@Override
	protected SolidBurnerBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SolidBurnerBlockEntity(blockEntityType, pos, state);
	}
}