package dev.celestiacraft.steam_letters.common.block.engine;

import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineDriveSpec;
import dev.celestiacraft.steam_letters.client.lang.SlLang;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlock;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import dev.celestiacraft.steam_letters.config.common.SteamConfig;
import dev.celestiacraft.steam_letters.config.common.SteamEngineConfig;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

/**
 * 飞轮引擎的方块实体: 蒸汽的消费者与飞轮的动力来源.
 * <p>
 * 工作流程 (只在服务端执行):
 * <ol>
 *     <li>确认正前方 {@value #WHEEL_DISTANCE} 格外存在配对飞轮, 否则立刻停机且不消耗蒸汽;</li>
 *     <li>按预热曲线累加本 tick 的蒸汽需求, 先扣自带罐, 再向背面的汽源抽取 (抽多少都要打掉固定的损耗);</li>
 *     <li>按实际到手蒸汽与需求的比值缩放应力, 连同恒定转速一起写进飞轮, 由飞轮推进动力网络.</li>
 * </ol>
 */
public class SteamEngineBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
	public static final String NBT_TANK = "Tank";
	public static final String NBT_HEATUP = "Heatup";
	public static final String NBT_PENDING = "Pending";
	public static final String NBT_STRENGTH = "AppliedStrength";
	public static final String NBT_LIT = "Lit";
	public static final String NBT_WHEEL = "Wheel";

	/**
	 * 引擎与飞轮的间距 (隔一格, 中间允许塞入其他方块).
	 */
	public static final int WHEEL_DISTANCE = 2;

	private static final int STAGES = 4;
	private static final double MIN_SUCK_EFFICIENCY = 0.001D;

	/**
	 * 内置青铜飞轮引擎的默认参数: 16384 SU / 32 RPM / 12 mB/tick, 自带蒸汽罐 32000 mB.
	 */
	public static final double DEFAULT_STRENGTH = 16384.0D;
	public static final double DEFAULT_SPEED = 32.0D;
	public static final double DEFAULT_CONSUMPTION = 12.0D;
	public static final int DEFAULT_STORAGE = 32000;

	@Getter
	private final SteamEngineDriveSpec driveSpec;

	@Getter
	private final FluidTank tank;

	private LazyOptional<IFluidHandler> fluidCapability;

	/**
	 * 已经预热的 tick 数, 归零即停机.
	 */
	@Getter
	private int heatup;

	/**
	 * 小数蒸汽余额, 只有攒够 1 mB 才会真的去抽.
	 */
	private double pending;

	@Getter
	private boolean lit;

	/**
	 * 当前实际写进飞轮的应力.
	 */
	@Getter
	private float appliedStrength;

	@Nullable
	private BlockPos wheelPos;

	private int syncedHeatup = -1;
	private float syncedStrength = -1.0F;

	public SteamEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		driveSpec = ((SteamEngineBlock) state.getBlock()).getDriveSpec();

		int configured = driveSpec.getStorage();
		int capacity = Math.max(configured > 0 ? configured : DEFAULT_STORAGE, 1);

		tank = new FluidTank(capacity) {
			@Override
			protected void onContentsChanged() {
				setChanged();
			}

			@Override
			public boolean isFluidValid(FluidStack stack) {
				return isSteamFluid(stack);
			}
		};

		fluidCapability = LazyOptional.of(() -> new SteamEngineFluidHandler(tank));
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		// 本设备不依赖任何 Create 行为组件
	}

	@Override
	public void tick() {
		super.tick();
		if (level == null) {
			return;
		}
		if (level.isClientSide) {
			clientTick();
			return;
		}
		serverTick();
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (level == null || level.isClientSide) {
			return;
		}

		refreshWheel();

		if (syncedHeatup != heatup || syncedStrength != appliedStrength) {
			syncedHeatup = heatup;
			syncedStrength = appliedStrength;
			sendData();
		}
	}

	// 服务端逻辑

	private void serverTick() {
		if (wheelPos == null) {
			refreshWheel();
		}

		SteamFlywheelBlockEntity wheel = getWheel();
		if (wheel == null) {
			shutdown();
			return;
		}

		double rate = getTargetRate();
		if (rate <= 0.0D) {
			shutdown();
			return;
		}

		pending += rate;
		int request = (int) Math.floor(pending);
		double ratio = 1.0D;

		if (request > 0) {
			int obtained = drawSteam(request);
			if (obtained <= 0) {
				shutdown();
				return;
			}
			ratio = obtained / (double) request;
			pending -= obtained;
			if (pending < 0.0D) {
				pending = 0.0D;
			}
		}

		if (heatup < (int) Math.ceil(getHeatupTicks())) {
			heatup++;
		}

		drive(wheel, ratio);
	}

	private void drive(SteamFlywheelBlockEntity wheel, double ratio) {
		float capacity = (float) (getTargetStrength() * Mth.clamp(ratio, 0.0D, 1.0D));
		appliedStrength = capacity;
		wheel.setRotation((float) getTargetSpeed(), capacity);
		updateLitState(true);
	}

	private void shutdown() {
		heatup = 0;
		pending = 0.0D;
		appliedStrength = 0.0F;

		SteamFlywheelBlockEntity wheel = getWheel();
		if (wheel != null) {
			wheel.setRotation(0.0F, 0.0F);
		}

		updateLitState(false);
	}

	/**
	 * 先扣自带罐, 不够的部分再去背面汽源抽.
	 *
	 * @return 本 tick 真正到手的蒸汽量
	 */
	private int drawSteam(int amount) {
		if (amount <= 0) {
			return 0;
		}

		int fromTank = tank.drain(amount, IFluidHandler.FluidAction.EXECUTE).getAmount();
		int remaining = amount - fromTank;
		if (remaining <= 0) {
			return fromTank;
		}
		return fromTank + drawFromBoiler(remaining);
	}

	/**
	 * 从背面紧贴的那一格抽取蒸汽.
	 * <p>
	 * 抽取速率上限与引擎自身的消耗速率上限一致, 抽上来的蒸汽按 {@code SteamEngineConfig.SUCK_EFFICIENCY}
	 * 打折入账 (默认 0.75, 也就是 25% 的损耗); 因此贴着锅炉直抽最多只能跑出额定的 75% 应力.
	 */
	private int drawFromBoiler(int needed) {
		if (level == null || needed <= 0) {
			return 0;
		}

		Direction back = getFacing().getOpposite();
		BlockEntity source = level.getBlockEntity(worldPosition.relative(back));
		if (source == null) {
			return 0;
		}

		IFluidHandler handler = source.getCapability(ForgeCapabilities.FLUID_HANDLER, back.getOpposite()).orElse(null);
		if (handler == null) {
			return 0;
		}

		double efficiency = Mth.clamp(SteamEngineConfig.SUCK_EFFICIENCY.get(), MIN_SUCK_EFFICIENCY, 1.0D);
		int rawCap = Math.max((int) Math.ceil(getTargetConsumption()), 1);
		int raw = Math.min((int) Math.ceil(needed / efficiency), rawCap);

		if (handler.getTanks() <= 0) {
			return 0;
		}

		// 按汽源罐里实际装的蒸汽流体去抽: 配置里换过蒸汽流体时, 硬编码本模组的蒸汽一滴也抽不上来.
		FluidStack content = handler.getFluidInTank(0);
		if (!isSteamFluid(content)) {
			return 0;
		}

		FluidStack drained = handler.drain(new FluidStack(content.getFluid(), raw), IFluidHandler.FluidAction.EXECUTE);
		if (drained.isEmpty()) {
			return 0;
		}
		return (int) Math.floor(drained.getAmount() * efficiency);
	}

	private void updateLitState(boolean value) {
		if (lit == value) {
			return;
		}
		lit = value;

		if (level == null) {
			return;
		}

		BlockState state = getBlockState();
		if (state.hasProperty(SteamEngineBlock.LIT) && state.getValue(SteamEngineBlock.LIT) != value) {
			level.setBlock(worldPosition, state.setValue(SteamEngineBlock.LIT, value), 3);
		}

		if (value) {
			level.playSound(null, worldPosition, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 0.4F, 0.9F);
		}
	}

	// 绑定

	private void refreshWheel() {
		BlockPos found = findWheelPos();

		if (found == null && wheelPos == null) {
			return;
		}
		if (found != null && found.equals(wheelPos)) {
			return;
		}

		wheelPos = found;

		if (found != null && level != null && level.getBlockEntity(found) instanceof SteamFlywheelBlockEntity wheel) {
			Direction engineSide = getFacing().getOpposite();
			if (SteamFlywheelBlock.getConnection(wheel.getBlockState()) != engineSide) {
				SteamFlywheelBlock.setConnection(level, found, wheel.getBlockState(), engineSide);
			}
		}

		sendData();
	}

	/**
	 * 找出正前方 {@value #WHEEL_DISTANCE} 格外可以驱动的飞轮.
	 * <p>
	 * 一台飞轮只服务一台引擎: 如果它已经连向另一侧的引擎, 而那一侧的引擎仍然在位,
	 * 本引擎就让步 (反过来, 那一侧失效后本引擎可以接管).
	 */
	@Nullable
	private BlockPos findWheelPos() {
		if (level == null) {
			return null;
		}

		Direction facing = getFacing();
		BlockPos candidate = worldPosition.relative(facing, WHEEL_DISTANCE);
		if (!(level.getBlockEntity(candidate) instanceof SteamFlywheelBlockEntity wheel)) {
			return null;
		}

		Direction occupied = SteamFlywheelBlock.getConnection(wheel.getBlockState());
		if (occupied != null && occupied != facing.getOpposite()) {
			BlockPos other = candidate.relative(occupied, WHEEL_DISTANCE);
			if (level.getBlockEntity(other) instanceof SteamEngineBlockEntity) {
				return null;
			}
		}

		return candidate.immutable();
	}

	@Nullable
	private SteamFlywheelBlockEntity getWheel() {
		if (level == null || wheelPos == null) {
			return null;
		}
		return level.getBlockEntity(wheelPos) instanceof SteamFlywheelBlockEntity wheel ? wheel : null;
	}

	// 参数

	private Direction getFacing() {
		BlockState state = getBlockState();
		return state.hasProperty(SteamEngineBlock.FACING) ? state.getValue(SteamEngineBlock.FACING) : Direction.NORTH;
	}

	public boolean isSuperheated() {
		return !tank.isEmpty() && tank.getFluid().getFluid().isSame(SlFluids.SUPERHEATED_STEAM.get());
	}

	public double getTargetSpeed() {
		double speed = driveSpec.getSpeed();
		return speed > 0.0D ? speed : DEFAULT_SPEED;
	}

	public double getTargetConsumption() {
		double configured = driveSpec.getConsumption();
		double consumption = configured > 0.0D ? configured : DEFAULT_CONSUMPTION;
		if (isSuperheated()) {
			consumption *= SteamEngineConfig.SUPERHEATED_CONSUMPTION_MULTIPLIER.get();
		}
		return Math.max(consumption, 0.0D);
	}

	/**
	 * 预热总时长 (tick): 消耗速率上限除以加速度.
	 */
	public double getHeatupTicks() {
		double consumption = getTargetConsumption();
		if (consumption <= 0.0D) {
			return 0.0D;
		}

		double acceleration = SteamEngineConfig.HEATUP_ACCELERATION.get();
		if (isSuperheated()) {
			acceleration *= SteamEngineConfig.SUPERHEATED_ACCELERATION_MULTIPLIER.get();
		}
		return acceleration <= 0.0D ? 0.0D : consumption / acceleration;
	}

	/**
	 * 预热进度 (0~1). 刚通电时也保证非零, 否则速率恒为 0 会让预热永远启动不了.
	 */
	private double getHeatupProgress() {
		double ticks = getHeatupTicks();
		if (ticks <= 0.0D) {
			return 1.0D;
		}
		return Mth.clamp((heatup + 1) / ticks, 0.0D, 1.0D);
	}

	/**
	 * 本 tick 的蒸汽需求 (mB/tick), 从 0 起步线性爬升到消耗速率上限.
	 */
	public double getTargetRate() {
		return getTargetConsumption() * getHeatupProgress();
	}

	/**
	 * 应力阶梯 (1~4), 对应额定的 25% / 50% / 75% / 100%.
	 */
	public int getStressStage() {
		return Mth.clamp((int) Math.ceil(getHeatupProgress() * STAGES), 1, STAGES);
	}

	public double getTargetStrength() {
		double strength = driveSpec.getStrength();
		if (strength <= 0.0D) {
			strength = DEFAULT_STRENGTH;
		}

		double value = strength * getStressStage() / (double) STAGES;
		if (isSuperheated()) {
			value *= SteamEngineConfig.SUPERHEATED_OUTPUT_MULTIPLIER.get();
		}
		return value;
	}

	// 客户端表现

	private void clientTick() {
		if (!lit || level == null || level.getRandom().nextFloat() > 0.35F) {
			return;
		}

		Direction front = getFacing();
		double x = worldPosition.getX() + 0.5D + front.getStepX() * 0.6D + (level.getRandom().nextDouble() - 0.5D) * 0.3D;
		double y = worldPosition.getY() + 0.75D + (level.getRandom().nextDouble() - 0.5D) * 0.3D;
		double z = worldPosition.getZ() + 0.5D + front.getStepZ() * 0.6D + (level.getRandom().nextDouble() - 0.5D) * 0.3D;

		level.addParticle(ParticleTypes.CLOUD, x, y, z, 0.0D, 0.012D, 0.0D);
	}

	// 护目镜

	@Override
	public boolean addToGoggleTooltip(List<Component> tooltip, boolean isPlayerSneaking) {
		SlLang.translate(SlTranKeys.GOGGLE_STEAM_ENGINE)
				.style(ChatFormatting.GRAY)
				.forGoggles(tooltip);

		SlLang.translate(getStatusKey())
				.style(getStatusColor())
				.forGoggles(tooltip, 1);

		addGoggleLine(tooltip, SlTranKeys.GOGGLE_STEAM_ENGINE_HEATUP, Component.literal(
				formatSeconds(heatup) + " / " + formatSeconds((int) Math.ceil(getHeatupTicks()))
		).withStyle(heatup > 0 ? ChatFormatting.GOLD : ChatFormatting.DARK_GRAY));

		addGoggleLine(tooltip, SlTranKeys.GOGGLE_STEAM_ENGINE_STRESS, Component.literal(
				formatNumber(appliedStrength) + " SU"
		).withStyle(appliedStrength > 0.0F ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));

		addGoggleLine(tooltip, SlTranKeys.GOGGLE_STEAM_ENGINE_STEAM, Component.literal(
				formatNumber(tank.getFluidAmount()) + " / " + formatNumber(tank.getCapacity()) + " mB"
		).withStyle(tank.getFluidAmount() > 0 ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY));

		return true;
	}

	private String getStatusKey() {
		if (heatup <= 0 || appliedStrength <= 0.0F) {
			return SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_IDLE;
		}
		if (heatup < (int) Math.ceil(getHeatupTicks())) {
			return SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_HEATUP;
		}
		return SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_RUNNING;
	}

	private ChatFormatting getStatusColor() {
		if (heatup <= 0 || appliedStrength <= 0.0F) {
			return ChatFormatting.RED;
		}
		return heatup < (int) Math.ceil(getHeatupTicks()) ? ChatFormatting.YELLOW : ChatFormatting.GREEN;
	}

	private static void addGoggleLine(List<Component> tooltip, String langKey, Component value) {
		SlLang.translate(langKey)
				.style(ChatFormatting.GRAY)
				.text(ChatFormatting.DARK_GRAY, ": ")
				.add(value)
				.forGoggles(tooltip, 1);
	}

	private static String formatNumber(double value) {
		return String.format(Locale.ROOT, "%.1f", value);
	}

	private static String formatSeconds(int ticks) {
		return String.format(Locale.ROOT, "%.1f", ticks / 20.0D) + "s";
	}

	// 能力与持久化

	private static boolean isSteamFluid(FluidStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		Fluid fluid = stack.getFluid();
		if (fluid.isSame(SlFluids.STEAM.get()) || fluid.isSame(SlFluids.SUPERHEATED_STEAM.get())) {
			return true;
		}

		// 锅炉产出的蒸汽流体可以在配置里换成其他模组的蒸汽, 那种流体同样要能灌进引擎.
		Fluid configured = SteamConfig.getOutputSteamFluid();
		return configured != null && fluid.isSame(configured);
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
		if (capability == ForgeCapabilities.FLUID_HANDLER) {
			return fluidCapability.cast();
		}
		return super.getCapability(capability, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		fluidCapability.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		fluidCapability = LazyOptional.of(() -> new SteamEngineFluidHandler(tank));
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		tag.put(NBT_TANK, tank.writeToNBT(new CompoundTag()));
		tag.putInt(NBT_HEATUP, heatup);
		tag.putDouble(NBT_PENDING, pending);
		tag.putFloat(NBT_STRENGTH, appliedStrength);
		tag.putBoolean(NBT_LIT, lit);
		if (wheelPos != null) {
			tag.putLong(NBT_WHEEL, wheelPos.asLong());
		}
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		if (tag.contains(NBT_TANK)) {
			tank.readFromNBT(tag.getCompound(NBT_TANK));
		}
		heatup = tag.getInt(NBT_HEATUP);
		pending = tag.getDouble(NBT_PENDING);
		appliedStrength = tag.getFloat(NBT_STRENGTH);
		lit = tag.getBoolean(NBT_LIT);
		wheelPos = tag.contains(NBT_WHEEL) ? BlockPos.of(tag.getLong(NBT_WHEEL)) : null;
	}
}
