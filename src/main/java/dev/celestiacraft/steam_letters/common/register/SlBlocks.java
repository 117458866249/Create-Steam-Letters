package dev.celestiacraft.steam_letters.common.register;

import com.simibubi.create.AllTags;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.content.decoration.encasing.CasingBlock;
import com.simibubi.create.content.kinetics.saw.SawMovementBehaviour;
import com.simibubi.create.content.processing.AssemblyOperatorBlockItem;
import com.simibubi.create.content.processing.basin.BasinMovementBehaviour;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.api.SlStress;
import dev.celestiacraft.steam_letters.api.item.SteamBlockItem;
import dev.celestiacraft.steam_letters.client.models.ItemModelGen;
import dev.celestiacraft.steam_letters.common.block.advanced_spout.AdvancedSpoutBlock;
import dev.celestiacraft.steam_letters.common.block.bronze_basin.BronzeBasinBlock;
import dev.celestiacraft.steam_letters.common.block.steam_hammer.SteamHammerBlock;
import dev.celestiacraft.steam_letters.common.block.steam_mixer.SteamMixerBlock;
import dev.celestiacraft.steam_letters.common.block.steam_saw.SteamSawBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.common.Tags;

public class SlBlocks {
	public static final BlockEntry<BronzeBasinBlock> BRONZE_BASIN;
	public static final BlockEntry<AdvancedSpoutBlock> ADVANCED_SPOUT;
	public static final BlockEntry<SteamHammerBlock> STEAM_HAMMER;
	public static final BlockEntry<SteamMixerBlock> STEAM_MIXER;
	public static final BlockEntry<SteamSawBlock> STEAM_SAW;
	public static final BlockEntry<CasingBlock> BRONZE_CASING;

	static {
		BRONZE_BASIN = SlApis.REGISTRATE.block("bronze_basin", BronzeBasinBlock::new)
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

		ADVANCED_SPOUT = SlApis.REGISTRATE.block("advanced_spout", AdvancedSpoutBlock::new)
				.initialProperties(SharedProperties::copperMetal)
				.item(AssemblyOperatorBlockItem::new)
				.model(ItemModelGen.withModel("block/advanced_spout/item"))
				.build()
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(AdvancedSpoutBlock.genBlockState())
				.register();

		STEAM_HAMMER = SlApis.REGISTRATE.block("steam_hammer", SteamHammerBlock::new)
				.item(SteamBlockItem::new)
				.model(ItemModelGen.withModel("block/steam_hammer/item"))
				.build()
				.initialProperties(SharedProperties::stone)
				.transform(SlStress.setImpact(16))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(SteamHammerBlock.genBlockState())
				.lang("Steam Hammer")
				.register();

		STEAM_MIXER = SlApis.REGISTRATE.block("steam_mixer", SteamMixerBlock::new)
				.item(SteamBlockItem::new)
				.model(ItemModelGen.withModel("block/steam_mixer/item"))
				.build()
				.initialProperties(SharedProperties::stone)
				.transform(SlStress.setImpact(16))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(SteamMixerBlock.genBlockState())
				.lang("Steam Mixer")
				.register();

		STEAM_SAW = SlApis.REGISTRATE.block("steam_saw", SteamSawBlock::new)
				.item(SteamBlockItem::new)
				.model(ItemModelGen.withModel("block/mechanical_saw/item"))
				.tag(AllTags.AllItemTags.CONTRAPTION_CONTROLLED.tag)
				.build()
				.initialProperties(SharedProperties::stone)
				.properties((properties) -> {
					return properties.mapColor(MapColor.PODZOL);
				})
				.transform(SlStress.setImpact(16))
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_STONE_TOOL)
				.tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag)
				.blockstate(SteamSawBlock.genBlockState())
				.onRegister(MovementBehaviour.movementBehaviour(new SawMovementBehaviour()))
				.lang("Steam Saw")
				.register();

		BRONZE_CASING = SlApis.REGISTRATE.block("bronze_casing", CasingBlock::new)
				.transform(BuilderTransformers.casing(() -> SlSpriteShifts.BRONZE_CASING))
				.properties((properties) -> {
					return properties.sound(SoundType.LANTERN);
				})
				.lang("Bronze Casing")
				.register();
	}

	public static void init() {
	}
}