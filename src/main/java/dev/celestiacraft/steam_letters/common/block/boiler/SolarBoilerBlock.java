package dev.celestiacraft.steam_letters.common.block.boiler;

import com.simibubi.create.foundation.block.IBE;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import dev.celestiacraft.steam_letters.client.block.SlBlockStateGen;
import dev.celestiacraft.steam_letters.api.machine.ISteamEngineBlock;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineSpec;
import dev.celestiacraft.steam_letters.common.register.SlBlockEntities;
import dev.celestiacraft.steam_letters.utils.BlockFluidInteractions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * 太阳能锅炉: 不需要燃料, 直接靠环境光照把水变成蒸汽.
 * <p>
 * 单方块结构, 没有朝向也没有点燃状态.
 */
public class SolarBoilerBlock extends Block implements IBE<SolarBoilerBlockEntity>, ISteamEngineBlock {
	private final SteamEngineSpec spec;
	private final Supplier<BlockEntityType<? extends SolarBoilerBlockEntity>> customBlockEntityType;

	public SolarBoilerBlock(Properties properties, SteamEngineSpec spec) {
		this(properties, spec, null);
	}

	public SolarBoilerBlock(Properties properties, SteamEngineSpec spec, Supplier<BlockEntityType<? extends SolarBoilerBlockEntity>> customBlockEntityType) {
		super(properties);
		this.spec = spec;
		this.customBlockEntityType = customBlockEntityType;
	}

	@Override
	public SteamEngineSpec getEngineSpec() {
		return spec;
	}

	@Override
	public Class<SolarBoilerBlockEntity> getBlockEntityClass() {
		return SolarBoilerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SolarBoilerBlockEntity> getBlockEntityType() {
		return customBlockEntityType != null ? customBlockEntityType.get() : SlBlockEntities.SOLAR_BOILER.get();
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, BlockHitResult hit) {
		return BlockFluidInteractions.fillOnly(level, pos, player, hand, hit.getDirection());
	}

	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> genBlockState(String material) {
		return SlBlockStateGen.simple("block/steam/solar_boiler/" + material);
	}
}