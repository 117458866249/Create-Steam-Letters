package dev.celestiacraft.csa.common.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.common.block.advanced_spout.*;
import dev.celestiacraft.csa.common.block.bronze_basin.*;
import dev.celestiacraft.csa.common.block.steam_hammer.*;
import dev.celestiacraft.csa.common.block.steam_mixer.*;

public class CsaBlockEntities {
	public static final BlockEntityEntry<BronzeBasinBlockEntity> BRONZE_BASIN;
	public static final BlockEntityEntry<AdvancedSpoutBlockEntity> ADVANCED_SPOUT;
	public static final BlockEntityEntry<SteamHammerBlockEntity> STEAM_HAMMER;
	public static final BlockEntityEntry<SteamMixerBlockEntity> STEAM_MIXER;

	static {
		BRONZE_BASIN = CsaApis.REGISTRATE.blockEntity("bronze_basin", BronzeBasinBlockEntity::new)
				.validBlock(CsaBlocks.BRONZE_BASIN)
				.renderer(() -> BronzeBasinRenderer::new)
				.register();

		ADVANCED_SPOUT = CsaApis.REGISTRATE.blockEntity("advanced_spout", AdvancedSpoutBlockEntity::new)
				.validBlocks(CsaBlocks.ADVANCED_SPOUT)
				.renderer(() -> AdvancedSpoutRenderer::new)
				.register();

		STEAM_HAMMER = CsaApis.REGISTRATE.blockEntity("steam_hammer", SteamHammerBlockEntity::new)
				.visual(() -> SteamHammerInstance::new)
				.renderer(() -> SteamHammerRenderer::new)
				.validBlock(CsaBlocks.STEAM_HAMMER)
				.register();

		STEAM_MIXER = CsaApis.REGISTRATE.blockEntity("steam_mixer", SteamMixerBlockEntity::new)
				.visual(() -> SteamMixerInstance::new)
				.renderer(() -> SteamMixerRenderer::new)
				.validBlock(CsaBlocks.STEAM_MIXER)
				.register();
	}

	public static void init() {
	}
}