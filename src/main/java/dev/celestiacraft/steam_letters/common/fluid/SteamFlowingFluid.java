package dev.celestiacraft.steam_letters.common.fluid;

import dev.celestiacraft.steam_letters.common.register.SlGameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import org.jetbrains.annotations.NotNull;

public abstract class SteamFlowingFluid extends ForgeFlowingFluid {
	protected SteamFlowingFluid(Properties properties) {
		super(properties.slopeFindDistance(3)
				.explosionResistance(100.0F));
	}

	@Override
	public @NotNull BlockState createLegacyBlock(FluidState state) {
		return Blocks.AIR.defaultBlockState();
	}

	@Override
	protected boolean canConvertToSource(Level level) {
		return level.getGameRules().getBoolean(SlGameRules.STEAM_CAN_CONVERT_TO_SOURCE);
	}
}