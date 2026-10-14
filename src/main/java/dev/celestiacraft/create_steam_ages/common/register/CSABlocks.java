package dev.celestiacraft.create_steam_ages.common.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.celestiacraft.create_steam_ages.api.CSAApis;
import dev.celestiacraft.create_steam_ages.api.CSAStress;
import dev.celestiacraft.create_steam_ages.api.models.ItemModelGen;
import dev.celestiacraft.create_steam_ages.common.block.steam_hammer.SteamHammerBlock;
import net.minecraft.tags.BlockTags;

public class CSABlocks {
	public static final BlockEntry<SteamHammerBlock> STEAM_HAMMER;

	static {
		STEAM_HAMMER = CSAApis.REGISTRATE.block("steam_hammer", SteamHammerBlock::new)
				.initialProperties(SharedProperties::stone)
				.transform(CSAStress.setImpact(16.0))
				.item()
				.model(ItemModelGen.withModel("block/steam_hammer/item"))
				.build()
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(SteamHammerBlock.genBlockState())
				.register();
	}

	public static void init() {
	}
}