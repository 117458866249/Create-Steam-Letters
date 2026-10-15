package dev.celestiacraft.steam_letters.api.item;

import com.simibubi.create.content.logistics.depot.DepotBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SteamBlockItem extends BlockItem {
	public SteamBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	protected BlockPos placedFromDistance(BlockPlaceContext context) {
		BlockPos hit = clickedBlock(context);
		Level level = context.getLevel();
		BlockState state = level.getBlockState(hit);
		Block block = state.getBlock();

		if (context.getClickedFace() != Direction.UP || !(block instanceof DepotBlock)) {
			return null;
		}
		return hit.above(2);
	}

	/**
	 * 返回右键点击到的方块位置.
	 * <p>
	 * {@link BlockPlaceContext#getClickedPos()} 返回的是实际落点, 只有点击处可替换时才等于点击到的方块位置.
	 */
	protected static BlockPos clickedBlock(BlockPlaceContext context) {
		return context.replacingClickedOnBlock()
				? context.getClickedPos()
				: context.getClickedPos()
				.relative(context.getClickedFace().getOpposite());
	}

	@Override
	public BlockPlaceContext updatePlacementContext(@NotNull BlockPlaceContext context) {
		BlockPos target = placedFromDistance(context);
		if (target == null) {
			return context;
		}

		BlockPlaceContext distant = BlockPlaceContext.at(context, target, Direction.UP);
		return distant.getClickedPos().equals(target) ? distant : context;
	}
}