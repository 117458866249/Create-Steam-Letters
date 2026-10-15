package dev.celestiacraft.steam_letters.common.block.advanced_spout;

import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsBoard;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsFormatter;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.celestiacraft.steam_letters.config.common.AdvancedSpoutConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;

public class AdvancedSpoutScrollValueBehaviour extends ScrollValueBehaviour {
	public static final BehaviourType<AdvancedSpoutScrollValueBehaviour> TYPE = new BehaviourType<>();

	public static final int ROW_FILLING_TIME = 0;
	public static final int ROW_PARALLEL = 1;
	public static final int MAX_VALUE = 20;
	public static final int MIN_FILLING_TIME = 1;
	public static final int MAX_FILLING_TIME = MAX_VALUE;
	public static final int MAX_PARALLEL = MAX_VALUE;
	public static final int MILESTONE_INTERVAL = 1;

	public static final String LANG_FILLING_TIME = "steam_letters.gui.advanced_spout.filling_time";
	public static final String LANG_PARALLEL = "steam_letters.gui.advanced_spout.parallel";
	public static final String LANG_VALUE_SPEED = "steam_letters.gui.advanced_spout.value.speed";
	public static final String LANG_VALUE_PARALLEL = "steam_letters.gui.advanced_spout.value.parallel";

	private int fillingTime;
	private int parallel;
	private ValueSettings hovered;

	public AdvancedSpoutScrollValueBehaviour(Component label, SmartBlockEntity be, ValueBoxTransform slot) {
		super(label, be, slot);
		between(0, MAX_VALUE);
		requiresWrench();
		fillingTime = Mth.clamp(SpoutBlockEntity.FILLING_TIME, MIN_FILLING_TIME, MAX_FILLING_TIME);
		parallel = Mth.clamp(AdvancedSpoutConfig.PARALLEL.get(), 1, MAX_PARALLEL);
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	/**
	 * 模式板占了 1; {@code ValueSettingsPacket} 靠 netId 找回对应的行为.
	 */
	@Override
	public int netId() {
		return 0;
	}

	/**
	 * 整条滑条只在手持扳手时出现, 也只在手持扳手时可调.
	 */
	@Override
	public boolean onlyVisibleWithWrench() {
		return true;
	}

	public int getFillingTime() {
		return Mth.clamp(fillingTime, MIN_FILLING_TIME, MAX_FILLING_TIME);
	}

	public int getParallel() {
		return Mth.clamp(parallel, 1, MAX_PARALLEL);
	}

	@Override
	public String formatValue() {
		ValueSettings setting = hovered;
		if (setting != null) {
			if (setting.row() == ROW_PARALLEL) {
				return "x" + Mth.clamp(setting.value(), 1, MAX_PARALLEL);
			}
			return displayedFillingTime(setting.value()) + "t";
		}

		String time = getFillingTime() + "t";
		String parallel = "x" + getParallel();
		return switch (mode()) {
			case AdvancedSpoutModeScrollValueBehaviour.MODE_SPEED -> time;
			case AdvancedSpoutModeScrollValueBehaviour.MODE_PARALLEL -> parallel;
			default -> time + " " + parallel;
		};
	}

	private int mode() {
		if (blockEntity instanceof AdvancedSpoutBlockEntity spout) {
			return spout.getMode();
		}
		return AdvancedSpoutModeScrollValueBehaviour.MODE_SPEED_AND_PARALLEL;
	}

	@Override
	public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
		ImmutableList<Component> rows = ImmutableList.of(
				Component.translatable(LANG_FILLING_TIME),
				Component.translatable(LANG_PARALLEL)
		);
		return new ValueSettingsBoard(
				label,
				MAX_VALUE,
				MILESTONE_INTERVAL, rows,
				new ValueSettingsFormatter(this::formatSetting)
		);
	}

	@Override
	public ValueSettings getValueSettings() {
		return new ValueSettings(ROW_FILLING_TIME, getFillingTime());
	}

	@Override
	public void setValueSettings(Player player, ValueSettings valueSetting, boolean ctrlDown) {
		ValueSettings previous = getValueSettings();
		boolean changed;

		if (valueSetting.row() == ROW_PARALLEL) {
			int amount = Mth.clamp(valueSetting.value(), 1, MAX_PARALLEL);
			changed = amount != parallel;
			parallel = amount;
		} else {
			int time = displayedFillingTime(valueSetting.value());
			changed = time != fillingTime;
			fillingTime = time;
		}

		if (!changed) {
			return;
		}
		if (!valueSetting.equals(previous)) {
			playFeedbackSound(this);
		}
		sync();
	}

	@Override
	public void newSettingHovered(ValueSettings valueSetting) {
		hovered = valueSetting;
	}

	private MutableComponent formatSetting(ValueSettings valueSetting) {
		if (valueSetting.row() == ROW_PARALLEL) {
			return Component.translatable(LANG_VALUE_PARALLEL, Mth.clamp(
					valueSetting.value(),
					1,
					MAX_PARALLEL
			));
		}
		return Component.translatable(LANG_VALUE_SPEED, displayedFillingTime(valueSetting.value()));
	}

	private static int displayedFillingTime(int cell) {
		return Mth.clamp(cell, MIN_FILLING_TIME, MAX_FILLING_TIME);
	}

	private void sync() {
		blockEntity.setChanged();
		blockEntity.sendData();
	}

	@Override
	public void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);
		nbt.putInt("SpoutFillingTime", fillingTime);
		nbt.putInt("SpoutParallel", parallel);
	}

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		super.read(nbt, clientPacket);
		if (nbt.contains("SpoutFillingTime")) {
			fillingTime = Mth.clamp(nbt.getInt("SpoutFillingTime"), MIN_FILLING_TIME, MAX_FILLING_TIME);
		}
		if (nbt.contains("SpoutParallel")) {
			parallel = Mth.clamp(nbt.getInt("SpoutParallel"), 1, MAX_PARALLEL);
		}
	}
}