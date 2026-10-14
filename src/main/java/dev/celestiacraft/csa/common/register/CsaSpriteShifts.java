package dev.celestiacraft.csa.common.register;

import com.simibubi.create.foundation.block.connected.AllCTTypes;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.CTSpriteShifter;
import dev.celestiacraft.csa.api.CsaApis;

public class CsaSpriteShifts {
	public static final CTSpriteShiftEntry BRONZE_CASING = CTSpriteShifter.getCT(
			AllCTTypes.OMNIDIRECTIONAL,
			CsaApis.loadResource("block/bronze_casing"),
			CsaApis.loadResource("block/bronze_casing_connected")
	);
}