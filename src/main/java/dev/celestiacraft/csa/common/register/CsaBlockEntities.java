package dev.celestiacraft.csa.common.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.common.block.steam_hammer.*;

public class CsaBlockEntities {
	public static final BlockEntityEntry<SteamHammerBlockEntity> STEAM_HAMMER;

	static {
		STEAM_HAMMER = CsaApis.REGISTRATE.blockEntity("steam_hammer", SteamHammerBlockEntity::new)
				.visual(() -> SteamHammerInstance::new)
				.renderer(() -> SteamHammerRenderer::new)
				.validBlocks(CsaBlocks.STEAM_HAMMER)
				.register();
	}

	public static void init() {
	}
}