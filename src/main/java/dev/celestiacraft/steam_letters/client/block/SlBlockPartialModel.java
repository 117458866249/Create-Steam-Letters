package dev.celestiacraft.steam_letters.client.block;

import dev.celestiacraft.steam_letters.api.SlApis;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class SlBlockPartialModel {
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
		return PartialModel.of(SlApis.loadResource("block/" + path));
	}

	public static void init() {
	}
}