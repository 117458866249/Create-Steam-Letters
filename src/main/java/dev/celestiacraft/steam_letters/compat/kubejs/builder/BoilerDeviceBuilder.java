package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 带内部流体容量的锅炉类设备, 锅炉与太阳能锅炉共用.
 */
public abstract class BoilerDeviceBuilder<B extends Block, E extends BlockEntity> extends SteamDeviceBuilder<B, E> {
	protected BoilerDeviceBuilder(ResourceLocation id) {
		super(id);
	}

	@Info("内部流体容量, 单位 mB.")
	public BoilerDeviceBuilder<B, E> capacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	@Info("满效率下的基础蒸汽产出速率, 单位 mB/tick.")
	public BoilerDeviceBuilder<B, E> production(double production) {
		this.production = production;
		return this;
	}
}
