package dev.celestiacraft.steam_letters.common.block.engine;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineDriveSpec;
import dev.celestiacraft.steam_letters.client.models.SlBlockStateGen;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlBlockEntities;
import dev.celestiacraft.steam_letters.utils.BlockFluidInteractions;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

/**
 * 飞轮引擎: 消耗蒸汽、驱动飞轮做功的设备.
 * <p>
 * 它本身不接入 Create 动力网络, 只是「控制器 + 耗汽方」: 所有动力参数都写在它身上,
 * 再通过 {@link SteamFlywheelBlockEntity} 把转速与应力推进网络.
 * <p>
 * 空间关系: 引擎水平朝向的两格之外是配对飞轮, 背面紧贴的那一格是可选汽源 (锅炉输出罐).
 */
public class SteamEngineBlock extends HorizontalDirectionalBlock implements IBE<SteamEngineBlockEntity>, IWrenchable {
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	@Getter
	private final SteamEngineDriveSpec driveSpec;

	public SteamEngineBlock(Properties properties, SteamEngineDriveSpec driveSpec) {
		super(properties);
		this.driveSpec = driveSpec;
		registerDefaultState(defaultBlockState()
				.setValue(FACING, Direction.NORTH)
				.setValue(LIT, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(FACING, LIT));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection());
	}

	/**
	 * 扳手把引擎转向点击的水平面, 方便对准飞轮.
	 */
	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		Direction face = context.getClickedFace();
		if (!face.getAxis().isHorizontal()) {
			return InteractionResult.PASS;
		}
		Level level = context.getLevel();
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}
		level.setBlock(context.getClickedPos(), state.setValue(FACING, face), 3);
		return InteractionResult.SUCCESS;
	}

	/**
	 * 手持蒸汽容器右键时, 把容器里的蒸汽灌进引擎自带的罐里.
	 */
	@Override
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, BlockHitResult result) {
		return BlockFluidInteractions.fillOnly(level, pos, player, hand, result.getDirection());
	}

	@Override
	public Class<SteamEngineBlockEntity> getBlockEntityClass() {
		return SteamEngineBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SteamEngineBlockEntity> getBlockEntityType() {
		return SlBlockEntities.STEAM_ENGINE.get();
	}

	public static NonNullBiConsumer<DataGenContext<Block, SteamEngineBlock>, RegistrateBlockstateProvider> genBlockState(String material) {
		return SlBlockStateGen.facingLit(FACING, LIT, "block/steam/engine/" + material, "block/steam/engine/" + material, 180);
	}
}