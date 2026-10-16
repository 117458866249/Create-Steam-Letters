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
 * 蒸汽锅炉: 与正下方的燃烧室配对工作, 消耗水产出蒸汽.
 * <p>
 * 锅炉自身不保存热量, 用水量/产汽量均由注册参数与配置决定.
 */
public class SteamBoilerBlock extends Block implements IBE<SteamBoilerBlockEntity>, ISteamEngineBlock {
	private final SteamEngineSpec spec;
	private final Supplier<BlockEntityType<? extends SteamBoilerBlockEntity>> customBlockEntityType;

	public SteamBoilerBlock(Properties properties, SteamEngineSpec spec) {
		this(properties, spec, null);
	}

	public SteamBoilerBlock(Properties properties, SteamEngineSpec spec, Supplier<BlockEntityType<? extends SteamBoilerBlockEntity>> customBlockEntityType) {
		super(properties);
		this.spec = spec;
		this.customBlockEntityType = customBlockEntityType;
	}

	@Override
	public SteamEngineSpec getEngineSpec() {
		return spec;
	}

	@Override
	public Class<SteamBoilerBlockEntity> getBlockEntityClass() {
		return SteamBoilerBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SteamBoilerBlockEntity> getBlockEntityType() {
		return customBlockEntityType != null ? customBlockEntityType.get() : SlBlockEntities.STEAM_BOILER.get();
	}

	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, BlockHitResult result) {
		return BlockFluidInteractions.fillOnly(level, pos, player, hand, result.getDirection());
	}

	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> genBlockState(String material) {
		return SlBlockStateGen.simple("block/steam/boiler/" + material);
	}
}