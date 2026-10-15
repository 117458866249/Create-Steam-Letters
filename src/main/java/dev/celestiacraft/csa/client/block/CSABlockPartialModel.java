package dev.celestiacraft.csa.client.block;

import dev.celestiacraft.csa.api.CsaApis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class CSABlockPartialModel {
	public static final PartialModel
			STEAM_HAMMER,
			STEAM_MIXER_HEAD,
			STEAM_MIXER_POLE,
			SPOUT_TOP,
			SPOUT_MIDDLE,
			SPOUT_BOTTOM;

	static {
		STEAM_HAMMER = addPartial("steam_hammer/head");

		STEAM_MIXER_HEAD = addPartial("steam_mixer/head");
		STEAM_MIXER_POLE = addPartial("steam_mixer/pole");

		SPOUT_TOP = addPartial("advanced_spout/top");
		SPOUT_MIDDLE = addPartial("advanced_spout/middle");
		SPOUT_BOTTOM = addPartial("advanced_spout/bottom");
	}

	private static PartialModel addPartial(String path) {
		return PartialModel.of(CsaApis.loadResource("block/" + path));
	}

	public static void init() {
	}
}