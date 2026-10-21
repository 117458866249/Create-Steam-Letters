package dev.celestiacraft.steam_letters.boiler;

import dev.celestiacraft.steam_letters.mixin.boiler.BoilerDataMixin;

/**
 * {@link BoilerDataMixin}与蒸汽流体处理器之间的桥接接口
 * <p>
 * 由{@link BoilerDataMixin}实现, 供蒸汽流体处理器将蒸汽输入信息回传给锅炉
 */
public interface SteamBoilerData {
	/**
	 * @param level  普通蒸汽为 9, 过热蒸汽为 18
	 * @param amount 本次调用接受的蒸汽流体量(mB)
	 */
	void sl$notifySteamInput(int level, int amount);
}