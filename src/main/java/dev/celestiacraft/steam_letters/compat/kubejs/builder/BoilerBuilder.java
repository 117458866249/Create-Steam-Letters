package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.api.item.SteamBlockItem;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.client.block.SlBlockStateGen;
import dev.celestiacraft.steam_letters.client.models.ItemModelGen;
import dev.celestiacraft.steam_letters.common.block.boiler.SteamBoilerBlock;
import dev.celestiacraft.steam_letters.common.block.boiler.SteamBoilerBlockEntity;
import dev.celestiacraft.steam_letters.config.common.SteamBoilerConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;

public class BoilerBuilder extends SteamDeviceBuilder<SteamBoilerBlock, SteamBoilerBlockEntity> {
	public BoilerBuilder(ResourceLocation id) {
		super(id);
	}

	@Override
	protected String devicePath() {
		return "block/steam/boiler";
	}

	@Override
	protected void registerDevice() {
		String basePath = basePath();
		SteamEngineSpec spec = SteamEngineSpec.builder()
				.capacity(capacityOr(SteamBoilerConfig.CAPACITY::get))
				.production(productionOr(SteamBoilerConfig.PRODUCTION::get))
				.build();

		blockEntry = SlApis.REGISTRATE.block(path, (properties) -> {
					return new SteamBoilerBlock(properties, spec, () -> {
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
				.blockstate(SlBlockStateGen.simple(basePath))
				.lang(displayName())
				.register();

		blockEntityEntry = SlApis.REGISTRATE.blockEntity(path, SteamBoilerBlockEntity::new)
				.validBlock(blockEntry)
				.register();
	}
}