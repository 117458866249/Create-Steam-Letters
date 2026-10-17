package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class BoilerDeviceBuilder<B extends Block, E extends BlockEntity> extends SteamDeviceBuilder<B, E> {
	protected BoilerDeviceBuilder(ResourceLocation id) {
		super(id);
	}

	public BoilerDeviceBuilder<B, E> capacity(int capacity) {
		this.capacity = capacity;
		return this;
	}

	public BoilerDeviceBuilder<B, E> production(double production) {
		this.production = production;
		return this;
	}
}