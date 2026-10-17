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
import dev.celestiacraft.steam_letters.common.register.SlParticles;
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

public class SteamEngineBlockEntity extends SmartBlockEntity implements IHaveGoggleInformation {
	public static final String NBT_TANK = "Tank";
	public static final String NBT_HEATUP = "Heatup";
	public static final String NBT_PENDING = "Pending";
	public static final String NBT_STRENGTH = "AppliedStrength";
	public static final String NBT_LIT = "Lit";
	public static final String NBT_WHEEL = "Wheel";
	public static final int WHEEL_DISTANCE = 2;
	private static final int STAGES = 4;
	private static final double MIN_SUCK_EFFICIENCY = 0.001D;
	public static final double DEFAULT_STRENGTH = 16384.0D;
	public static final double DEFAULT_SPEED = 32.0D;
	/**
	 * 满负荷蒸汽消耗速率 (mB/tick): 与同材料锅炉的最高产汽速率 (36 mB/tick) 相等.
	 * 预热时长 = consumption / heatup_acceleration, 默认 0.5 时为 72 tick.
	 */
	public static final double DEFAULT_CONSUMPTION = 36.0D;
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
	private int syncedFluidAmount = -1;

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
		if (level.isClientSide()) {
			clientTick();
			return;
		}
		serverTick();
	}

	@Override
	public void lazyTick() {
		super.lazyTick();
		if (level == null || level.isClientSide()) {
			return;
		}

		refreshWheel();

		// 罐里的量也得算进同步条件: 护目镜读的是客户端这边的 tank,
		// 只同步热机进度和应力的话, 罐里进进出出的蒸汽在客户端永远是旧值.
		int fluidAmount = tank.getFluidAmount();
		if (syncedHeatup != heatup || syncedStrength != appliedStrength || syncedFluidAmount != fluidAmount) {
			syncedHeatup = heatup;
			syncedStrength = appliedStrength;
			syncedFluidAmount = fluidAmount;
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
			int fromTank = tank.drain(request, IFluidHandler.FluidAction.EXECUTE).getAmount();
			int remaining = request - fromTank;
			int fromSource = remaining > 0 ? drawFromBoiler(remaining) : 0;
			int drawn = fromTank + fromSource;

			// 以请求量结算: 没抽到的部分不跨 tick 累积, 否则欠账会滚雪球, 应力一路下滑.
			pending -= request;
			if (pending < 0.0D) {
				pending = 0.0D;
			}
			if (drawn <= 0) {
				shutdown();
				return;
			}

			// 应力只按到手蒸汽算: 汽源抽上来的部分只入账 suck_efficiency, 自带罐里的蒸汽全额入账.
			ratio = Mth.clamp((fromTank + fromSource * getSuckEfficiency()) / request, 0.0D, 1.0D);
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
	 * 从背面紧贴的那一格抽取蒸汽.
	 * <p>
	 * 抽取量上限与引擎自身的满负荷消耗速率一致, 返回值是实际从汽源抽走的原始蒸汽量;
	 * 抽上来的蒸汽按 {@code SteamEngineConfig.SUCK_EFFICIENCY} 折算成到手量, 因此贴着锅炉直抽最多跑出额定的 75% 应力.
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

		int rawCap = Math.max((int) Math.ceil(getTargetConsumption()), 1);
		int raw = Math.min(needed, rawCap);

		if (handler.getTanks() <= 0) {
			return 0;
		}

		// 汽源把蒸汽装在哪个罐里由它自己决定 (锅炉的 0 号罐是水, 1 号罐才是蒸汽),
		// 因此逐个罐找一个装着蒸汽的, 再用那种流体构造抽取请求.
		FluidStack steamStack = FluidStack.EMPTY;
		for (int i = 0; i < handler.getTanks(); i++) {
			FluidStack content = handler.getFluidInTank(i);
			if (!content.isEmpty() && isSteamFluid(content)) {
				steamStack = content;
				break;
			}
		}
		if (steamStack.isEmpty()) {
			return 0;
		}

		FluidStack drained = handler.drain(new FluidStack(steamStack.getFluid(), raw), IFluidHandler.FluidAction.EXECUTE);
		return drained.getAmount();
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

	/**
	 * 直抽效率 (0~1): 从汽源抽上来的蒸汽只有这一比例计入引擎.
	 */
	private double getSuckEfficiency() {
		return Mth.clamp(SteamEngineConfig.SUCK_EFFICIENCY.get(), MIN_SUCK_EFFICIENCY, 1.0D);
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
		if (!lit || level == null) {
			return;
		}

		// 固定每 2 tick 一颗, 不走随机概率, 否则疏密不匀
		if (level.getGameTime() % 2L != 0L) {
			return;
		}

		// 端口在模型北侧 (见 block/steam/engine/bronze.json 的 Port 元素, Z 从 -1.8 到 0.2, Y 从 4 到 12),
		// 中心高度约 0.5 格. 不加横向抖动, 让粒子沿着端口中心排成一条竖直的柱.
		Direction front = getFacing();
		double x = worldPosition.getX() + 0.5D + front.getStepX() * 0.6D;
		double y = worldPosition.getY() + 0.5D;
		double z = worldPosition.getZ() + 0.5D + front.getStepZ() * 0.6D;

		level.addParticle(SlParticles.STEAM.get(), x, y, z, 0.0D, 0.02D, 0.0D);
	}

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