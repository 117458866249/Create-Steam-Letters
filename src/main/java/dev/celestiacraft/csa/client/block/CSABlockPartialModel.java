package dev.celestiacraft.csa.client.block;

import dev.celestiacraft.csa.api.CsaApis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CSABlockPartialModel {
	public static final PartialModel
			STEAM_HAMMER;

	static {
		STEAM_HAMMER = addPartial("steam_hammer/head");
	}

	private static PartialModel addPartial(String path) {
		return PartialModel.of(CsaApis.loadResource("block/" + path));
	}

	public static void init() {
	}
}