package dev.celestiacraft.create_steam_ages.common.register;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.celestiacraft.create_steam_ages.api.CSAApis;
import dev.celestiacraft.create_steam_ages.common.block.steam_hammer.SteamHammerBlockEntity;

public class CSABlockEntities {
	public static final BlockEntityEntry<SteamHammerBlockEntity> STEAM_HAMMER;

	static {
		STEAM_HAMMER = CSAApis.REGISTRATE.blockEntity("steam_hammer", SteamHammerBlockEntity::new)
				.register();
	}

	public static void init() {
	}
}