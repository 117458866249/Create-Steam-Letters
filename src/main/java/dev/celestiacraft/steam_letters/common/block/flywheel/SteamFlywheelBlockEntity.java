package dev.celestiacraft.steam_letters.common.block.flywheel;

import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import dev.celestiacraft.steam_letters.common.block.engine.SteamEngineBlockEntity;
import dev.celestiacraft.steam_letters.utils.InterpolatedChasingValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

/**
 * 蒸汽飞轮方块实体.
 * <p>
 * 它是一台「被驱动的动力源」: 自身不产汽也不耗汽, 转速与应力容量完全由配对飞轮引擎写入,
 * 再借由 Create 的发电机制把旋转推入动力网络.
 * <p>
 * 停机不是立即的: 引擎停止供汽后, 飞轮保留 40 tick 的缓冲, 避免在引擎短暂断汽时
 * 反复拆装动力网络. 如果 40 tick 内没有恢复旋转, 才会真正把自身从动力网络摘出.
 */
public class SteamFlywheelBlockEntity extends GeneratingKineticBlockEntity {
	/**
	 * 引擎断汽后的缓冲时间 (tick).
	 */
	private static final int STOPPING_COOLDOWN = 40;

	private float generatedCapacity;
	private float generatedSpeed;
	private int stoppingCooldown;

	public final InterpolatedChasingValue visualSpeed = new InterpolatedChasingValue();
	public float angle;

	public SteamFlywheelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	/**
	 * 由配对引擎调用, 写入当前转速与应力容量. {@code speed} 为 0 表示引擎已停机.
	 *
	 * @param speed    转速 (RPM), 负值表示反向
	 * @param capacity 对外提供的应力容量 (SU)
	 */
	public void setRotation(float speed, float capacity) {
		boolean changed = generatedSpeed != speed || generatedCapacity != capacity;
		if (speed == 0) {
			if (changed && stoppingCooldown == 0) {
				stoppingCooldown = STOPPING_COOLDOWN;
			}
			return;
		}
		stoppingCooldown = 0;
		if (changed) {
			generatedSpeed = speed;
			generatedCapacity = capacity;
			updateGeneratedRotation();
		}
	}

	/**
	 * 当前是否已经按给定的转速与容量在发电. 引擎用它避免每 tick 重复写入.
	 */
	public boolean isGeneratingAt(float speed, float capacity) {
		return generatedSpeed == speed && generatedCapacity == capacity && stoppingCooldown == 0;
	}

	/**
	 * 引擎被拆除或断汽时调用: 立即停机并解除配对.
	 */
	public void clearState() {
		setRotation(0, 0);
		if (level != null) {
			SteamFlywheelBlock.setConnection(level, worldPosition, getBlockState(), null);
		}
	}

	@Override
	public void applyNewSpeed(float prevSpeed, float speed) {
		if (speed == 0) {
			if (hasSource()) {
				notifyStressCapacityChange(0);
				getOrCreateNetwork().updateStressFor(this, calculateStressApplied());
				return;
			}
			detachKinetics();
			setSpeed(0);
			setNetwork(null);
			return;
		}
		if (prevSpeed == 0) {
			setSpeed(speed);
			setNetwork(createNetworkId());
			attachKinetics();
			return;
		}
		if (hasSource()) {
			// 飞轮是动力源, 减速不需要拆掉整个动力网络
			if (Math.abs(prevSpeed) >= Math.abs(speed)) {
				return;
			}
			detachKinetics();
			setSpeed(speed);
			source = null;
			setNetwork(createNetworkId());
			attachKinetics();
			return;
		}
		detachKinetics();
		setSpeed(speed);
		attachKinetics();
	}

	@Override
	public int getFlickerScore() {
		return 0;
	}

	@Override
	public float getGeneratedSpeed() {
		return convertToDirection(generatedSpeed, getBlockState().getValue(HorizontalKineticBlock.HORIZONTAL_FACING));
	}

	@Override
	public float calculateAddedStressCapacity() {
		return lastCapacityProvided = generatedCapacity;
	}

	@Override
	protected AABB createRenderBoundingBox() {
		return super.createRenderBoundingBox().inflate(2.0D);
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putFloat("GeneratedSpeed", generatedSpeed);
		compound.putFloat("GeneratedCapacity", generatedCapacity);
		compound.putInt("Cooldown", stoppingCooldown);
		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		generatedSpeed = compound.getFloat("GeneratedSpeed");
		generatedCapacity = compound.getFloat("GeneratedCapacity");
		stoppingCooldown = compound.getInt("Cooldown");
		super.read(compound, clientPacket);
		if (clientPacket) {
			visualSpeed.withSpeed(0.03125F).target(getGeneratedSpeed());
		}
	}

	@Override
	public void tick() {
		super.tick();
		if (level == null) {
			return;
		}
		if (level.isClientSide()) {
			float target = isVirtual() ? getSpeed() : getGeneratedSpeed();
			visualSpeed.target(target).tick();
			angle += visualSpeed.value() * 3.0F / 10.0F;
			angle %= 360.0F;
			return;
		}
		if (getGeneratedSpeed() != 0 && getSpeed() == 0) {
			updateGeneratedRotation();
		}
		if (stoppingCooldown != 0) {
			stoppingCooldown--;
			if (stoppingCooldown == 0) {
				generatedSpeed = 0;
				generatedCapacity = 0;
				updateGeneratedRotation();
			}
		}
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (level == null || level.isClientSide()) {
			return;
		}
		Direction connection = SteamFlywheelBlock.getConnection(getBlockState());
		if (connection == null) {
			return;
		}
		BlockPos enginePos = worldPosition.relative(connection, 2);
		if (!(level.getBlockEntity(enginePos) instanceof SteamEngineBlockEntity)) {
			clearState();
		}
	}
}