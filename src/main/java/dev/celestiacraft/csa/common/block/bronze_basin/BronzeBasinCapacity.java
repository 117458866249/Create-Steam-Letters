package dev.celestiacraft.csa.common.block.bronze_basin;

import dev.celestiacraft.csa.config.common.BronzeBasinConfig;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BronzeBasinCapacity {
	public int slotCapacity() {
		return BronzeBasinConfig.SLOT_CAPACITY.get();
	}

	public int totalCapacity() {
		return 4 * slotCapacity();
	}

	public int renderFullCapacity() {
		return 2 * slotCapacity();
	}
}