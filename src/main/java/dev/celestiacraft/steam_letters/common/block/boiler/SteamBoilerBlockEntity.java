package dev.celestiacraft.steam_letters.common.block.boiler;

import dev.celestiacraft.steam_letters.api.machine.heat.IHeatSource;
import dev.celestiacraft.steam_letters.config.common.SteamBoilerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 蒸汽锅炉的方块实体.
 * <p>
 * 运行效率来自正下方燃烧室的当前热量: 热量低于第一档阈值时完全不产汽.
 * 连接状态由 {@link #lazyTick()} 维护, 热量值每 tick 现场读取, 所以燃烧室被移除/替换会立刻停产.
 */
public class SteamBoilerBlockEntity extends AbstractBoilerBlockEntity {
	/**
	 * 内置青铜蒸汽锅炉的默认参数: 两个罐各 10000 mB, 100% 效率时产 100 mB/tick 蒸汽.
	 */
	public static final int DEFAULT_CAPACITY = 10000;
	public static final double DEFAULT_PRODUCTION = 100.0D;

	private boolean hasValidBurner;

	public SteamBoilerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (level == null || level.isClientSide()) {
			return;
		}
		hasValidBurner = getHeatSource() != null;
	}

	@Override
	protected double getCurrentEfficiency() {
		if (!hasValidBurner) {
			return 0.0D;
		}
		IHeatSource source = getHeatSource();
		if (source == null) {
			return 0.0D;
		}
		return SteamBoilerConfig.getEfficiency(source.getHeatRatio() * 100.0D);
	}
}