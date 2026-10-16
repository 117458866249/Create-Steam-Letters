package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.api.item.SteamBlockItem;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.client.block.SlBlockStateGen;
import dev.celestiacraft.steam_letters.client.models.ItemModelGen;
import dev.celestiacraft.steam_letters.common.block.burner.SolidBurnerBlock;
import dev.celestiacraft.steam_letters.common.block.burner.SolidBurnerBlockEntity;
import dev.celestiacraft.steam_letters.config.common.SteamBurnerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;

public class SolidBurnerBuilder extends SteamDeviceBuilder<SolidBurnerBlock, SolidBurnerBlockEntity> {
	public SolidBurnerBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected String devicePath() {
		return "block/steam/solid_burner";
	}

	@Override
	protected void registerDevice() {
		String basePath = basePath();
		SteamEngineSpec spec = SteamEngineSpec.builder()
				.maxHeat(maxHeatOr(SteamBurnerConfig.MAX_HEAT::get))
				.build();

		blockEntry = SlApis.REGISTRATE.block(path, (properties) -> {
					return new SolidBurnerBlock(properties, spec, () -> {
						return blockEntityEntry.get();
					});
				})
				.item(SteamBlockItem::new)
				.model(ItemModelGen.withModel(basePath))
				.build()
				.initialProperties(SharedProperties::softMetal)
				.properties((properties) -> {
					return properties.noOcclusion()
							.sound(SoundType.LANTERN);
				})
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(SlBlockStateGen.facingLit(basePath, basePath + "_lit"))
				.lang(displayName())
				.register();

		blockEntityEntry = SlApis.REGISTRATE.blockEntity(path, SolidBurnerBlockEntity::new)
				.validBlock(blockEntry)
				.register();
	}
}