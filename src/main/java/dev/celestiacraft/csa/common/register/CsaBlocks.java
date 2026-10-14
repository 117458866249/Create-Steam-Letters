package dev.celestiacraft.csa.common.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.processing.basin.BasinMovementBehaviour;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.api.CsaStress;
import dev.celestiacraft.csa.api.item.SteamBlockItem;
import dev.celestiacraft.csa.client.models.ItemModelGen;
import dev.celestiacraft.csa.common.block.bronze_basin.BronzeBasinBlock;
import dev.celestiacraft.csa.common.block.steam_hammer.SteamHammerBlock;
import dev.celestiacraft.csa.common.block.steam_mixer.SteamMixerBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.Tags;

public class CsaBlocks {
	public static final BlockEntry<BronzeBasinBlock> BRONZE_BASIN;
	public static final BlockEntry<SteamHammerBlock> STEAM_HAMMER;
	public static final BlockEntry<SteamMixerBlock> STEAM_MIXER;
	public static final BlockEntry<CasingBlock> BRONZE_CASING;

	static {
		BRONZE_BASIN = CsaApis.REGISTRATE.block("bronze_basin", BronzeBasinBlock::new)
				.item()
				.model(ItemModelGen.withModel("block/bronze_basin/block"))
				.build()
				.initialProperties(SharedProperties::stone)
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(Tags.Blocks.NEEDS_WOOD_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(BronzeBasinBlock.genBlockState())
				.onRegister(MovementBehaviour.movementBehaviour(new BasinMovementBehaviour()))
				.lang("Bronze Basin")
				.register();

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

		STEAM_MIXER = CsaApis.REGISTRATE.block("steam_mixer", SteamMixerBlock::new)
				.item(SteamBlockItem::new)
				.model(ItemModelGen.withModel("block/steam_mixer/item"))
				.build()
				.initialProperties(SharedProperties::stone)
				.transform(CsaStress.setImpact(16))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(SteamMixerBlock.genBlockState())
				.lang("Steam Mixer")
				.register();

		BRONZE_CASING = CsaApis.REGISTRATE.block("bronze_casing", CasingBlock::new)
				.transform(BuilderTransformers.casing(() -> CsaSpriteShifts.BRONZE_CASING))
				.properties((properties) -> {
					return properties.sound(SoundType.LANTERN);
				})
				.lang("Bronze Casing")
				.register();
	}

	public static void init() {
	}
}