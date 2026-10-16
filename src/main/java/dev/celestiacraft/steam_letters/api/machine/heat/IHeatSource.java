package dev.celestiacraft.steam_letters.api.machine.heat;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * 热源接口 —— 由燃烧室实现.
 * <p>
 * 锅炉不维护任何重复的热量数据, 只通过 {@link Level#getBlockEntity(BlockPos)}
 * 读取正下方燃烧室的当前状态. 只有一格位移, 因此不走 Capability 通道.
 */
public interface IHeatSource {
	/** 当前热量, 刻度与 {@link #getMaxHeat()} 一致. */
	double getHeat();

	/** 热量上限, 由注册参数 maxHeat 决定. */
	double getMaxHeat();

	/** 是否正在消耗燃料. 燃料耗尽后仍可能有残余热量, 但此时不再算作燃烧中. */
	boolean isBurningFuel();

	/** 当前热量占上限的比例, 已钳位到 [0, 1]. 上限非法时返回 0. */
	default double getHeatRatio() {
		double max = getMaxHeat();
		if (max <= 0.0D) {
			return 0.0D;
		}
		return Math.min(1.0D, Math.max(0.0D, getHeat() / max));
	}
}
