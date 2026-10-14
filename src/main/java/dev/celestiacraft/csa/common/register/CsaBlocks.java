package dev.celestiacraft.csa.common.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.api.CsaStress;
import dev.celestiacraft.csa.api.item.SteamBlockItem;
import dev.celestiacraft.csa.api.models.ItemModelGen;
import dev.celestiacraft.csa.common.block.steam_hammer.SteamHammerBlock;
import net.minecraft.tags.BlockTags;

public class CsaBlocks {
	public static final BlockEntry<SteamHammerBlock> STEAM_HAMMER;

	static {
		STEAM_HAMMER = CsaApis.REGISTRATE.block("steam_hammer", SteamHammerBlock::new)
				.item(SteamBlockItem::new)
				.model(ItemModelGen.withModel("block/steam_hammer/item"))
				.build()
				.initialProperties(SharedProperties::stone)
				.transform(CsaStress.setImpact(16))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(SteamHammerBlock.genBlockState())
				.lang("Steam Hammer")
				.register();
	}

	public static void init() {
	}
}