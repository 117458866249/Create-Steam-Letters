package dev.celestiacraft.create_steam_ages.client.block;

import dev.celestiacraft.create_steam_ages.api.CSAApis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CSABlockPartialModel {
	public static final PartialModel
			STEAM_HAMMER;

	static {
		STEAM_HAMMER = addPartial("steam_hammer/head");
	}

	private static PartialModel addPartial(String path) {
		return PartialModel.of(CSAApis.loadResource("block/" + path));
	}

	public static void init() {
	}
}