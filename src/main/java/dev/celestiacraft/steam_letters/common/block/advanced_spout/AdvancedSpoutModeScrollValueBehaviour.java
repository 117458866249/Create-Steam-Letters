package dev.celestiacraft.steam_letters.common.block.advanced_spout;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * 高级注液器的模式板: 手持扳手在顶面长按打开, 只有 3 格, 一格一种模式.
 * <p>
 * 单独拆一块板的原因: Create 的 {@link ValueSettingsBoard} 一块板只有一个量程和一套刻度,
 * 所有行共用; 想让模式行只有 3 格、数值行有 20 格, 只能分成两块板
 * (数值板见 {@link AdvancedSpoutScrollValueBehaviour}, 占水平四面).
 */
public class AdvancedSpoutModeScrollValueBehaviour extends ScrollValueBehaviour {
	public static final BehaviourType<AdvancedSpoutModeScrollValueBehaviour> TYPE = new BehaviourType<>();

	/**
	 * 第 1 格: 速度与并行都用.
	 */
	public static final int MODE_SPEED_AND_PARALLEL = 0;
	/**
	 * 第 2 格: 只用滑条的注液时间, 每次注一件.
	 */
	public static final int MODE_SPEED = 1;
	/**
	 * 第 3 格: 只整叠注液, 注液时间保持原版 20 刻.
	 */
	public static final int MODE_PARALLEL = 2;
	/**
	 * 模式板一共 3 格.
	 */
	public static final int MAX_MODE = MODE_PARALLEL;

	public static final String LANG_MODE = "steam_letters.gui.advanced_spout.mode";
	public static final String LANG_MODE_BOTH = "steam_letters.gui.advanced_spout.mode.both";
	public static final String LANG_MODE_SPEED = "steam_letters.gui.advanced_spout.mode.speed";
	public static final String LANG_MODE_PARALLEL = "steam_letters.gui.advanced_spout.mode.parallel";
	public static final String LANG_MODE_BOTH_SHORT = "steam_letters.gui.advanced_spout.mode.both.short";
	public static final String LANG_MODE_SPEED_SHORT = "steam_letters.gui.advanced_spout.mode.speed.short";
	public static final String LANG_MODE_PARALLEL_SHORT = "steam_letters.gui.advanced_spout.mode.parallel.short";

	public AdvancedSpoutModeScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
		super(label, be, slot);
		between(MODE_SPEED_AND_PARALLEL, MAX_MODE);
		requiresWrench();
		value = MODE_SPEED_AND_PARALLEL;
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	/**
	 * 数值板占了 0; {@code ValueSettingsPacket} 靠 netId 找回对应的行为.
	 */
	@Override
	public int netId() {
		return 1;
	}

	@Override
	public boolean onlyVisibleWithWrench() {
		return true;
	}

	public int getMode() {
		return modeOf(value);
	}

	public static int modeOf(int value) {
		return Mth.clamp(value, MODE_SPEED_AND_PARALLEL, MAX_MODE);
	}

	/**
	 * 世界里数值框上的完整模式名.
	 */
	public static MutableComponent modeName(int mode) {
		return switch (mode) {
			case MODE_SPEED -> Component.translatable(LANG_MODE_SPEED);
			case MODE_PARALLEL -> Component.translatable(LANG_MODE_PARALLEL);
			default -> Component.translatable(LANG_MODE_BOTH);
		};
	}

	/**
	 * 滑条板光标格里的短名: 太长会撑出 Create 的面板背景.
	 */
	private static MutableComponent shortModeName(int mode) {
		return switch (mode) {
			case MODE_SPEED -> Component.translatable(LANG_MODE_SPEED_SHORT);
			case MODE_PARALLEL -> Component.translatable(LANG_MODE_PARALLEL_SHORT);
			default -> Component.translatable(LANG_MODE_BOTH_SHORT);
		};
	}

	@Override
	public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
		ImmutableList<Component> rows = ImmutableList.of(Component.translatable(LANG_MODE));
		return new ValueSettingsBoard(
				label,
				MAX_MODE,
				1, rows,
				new ValueSettingsFormatter((setting) -> {
					return shortModeName(modeOf(setting.value()));
				}));
	}

	@Override
	public ValueSettings getValueSettings() {
		return new ValueSettings(0, getMode());
	}

	@Override
	public void setValueSettings(Player player, ValueSettings valueSetting, boolean ctrlDown) {
		int mode = modeOf(valueSetting.value());
		if (mode == getMode()) {
			return;
		}
		playFeedbackSound(this);
		setValue(mode);
	}

	@Override
	public String formatValue() {
		return modeName(getMode()).getString();
	}

	/**
	 * 模式板挂在顶面, 值框贴顶面中央稍稍靠外.
	 */
	public static class ModeValueBox extends ValueBoxTransform.Sided {
		@Override
		protected boolean isSideActive(BlockState state, Direction direction) {
			return direction == Direction.UP;
		}

		@Override
		protected Vec3 getSouthLocation() {
			return VecHelper.voxelSpace(8, 8, 16.5);
		}
	}
}
