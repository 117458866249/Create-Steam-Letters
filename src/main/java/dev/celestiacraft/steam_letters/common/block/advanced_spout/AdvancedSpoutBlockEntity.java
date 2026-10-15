package dev.celestiacraft.steam_letters.common.block.advanced_spout;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import com.simibubi.create.content.fluids.FluidFX;
import com.simibubi.create.content.fluids.spout.FillingBySpout;
import com.simibubi.create.content.fluids.spout.SpoutBlockEntity;
import com.simibubi.create.content.kinetics.belt.behaviour.BeltProcessingBehaviour;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.fluid.SmartFluidTankBehaviour;
import com.simibubi.create.foundation.fluid.FluidHelper;
import dev.celestiacraft.steam_letters.config.common.AdvancedSpoutConfig;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class AdvancedSpoutBlockEntity extends SpoutBlockEntity {
	public AdvancedSpoutBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);

		for (BlockEntityBehaviour behaviour : behaviours) {
			if (behaviour instanceof SmartFluidTankBehaviour tankBehaviour) {
				tankBehaviour.getPrimaryHandler().setCapacity(AdvancedSpoutConfig.CAPACITY.get());
				break;
			}
		}

		behaviours.add(new AdvancedSpoutScrollValueBehaviour(
				Component.translatable("block.steam_letters.advanced_spout"),
				this,
				new AdvancedSpoutScrollValueBehaviour.SpoutValueBox()
		));

		behaviours.add(new AdvancedSpoutModeScrollValueBehaviour(
				Component.translatable(AdvancedSpoutModeScrollValueBehaviour.LANG_MODE),
				this,
				new AdvancedSpoutModeScrollValueBehaviour.ModeValueBox()
		));
	}

	/**
	 * 注液时间来自数值板 (由行为持有), 行为缺失时退回原版 20 刻.
	 * 模式板选「并行」时忽略滑条值, 保持原版 20 刻.
	 */
	public int getFillingTime() {
		if (getMode() == AdvancedSpoutModeScrollValueBehaviour.MODE_PARALLEL) {
			return SpoutBlockEntity.FILLING_TIME;
		}
		AdvancedSpoutScrollValueBehaviour behaviour = getBehaviour(AdvancedSpoutScrollValueBehaviour.TYPE);
		if (behaviour == null) {
			return SpoutBlockEntity.FILLING_TIME;
		}
		return behaviour.getFillingTime();
	}

	/**
	 * 当前模式, 来自顶面的模式板.
	 */
	public int getMode() {
		AdvancedSpoutModeScrollValueBehaviour behaviour = getBehaviour(AdvancedSpoutModeScrollValueBehaviour.TYPE);
		if (behaviour == null) {
			return AdvancedSpoutModeScrollValueBehaviour.MODE_SPEED_AND_PARALLEL;
		}
		return behaviour.getMode();
	}

	/**
	 * 真注液发生在倒计时走到这一时刻: 原版周期固定为 5, 周期短于 6 刻时提前到 cycle-1, 下限 0.
	 */
	public int getPourTick() {
		return Math.min(5, Math.max(0, getFillingTime() - 1));
	}

	/**
	 * 单次注液可处理的物品数量, 同样由数值板决定; 模式板选「速度」时固定为 1.
	 */
	public int getParallel() {
		if (getMode() == AdvancedSpoutModeScrollValueBehaviour.MODE_SPEED) {
			return 1;
		}
		AdvancedSpoutScrollValueBehaviour behaviour = getBehaviour(AdvancedSpoutScrollValueBehaviour.TYPE);
		if (behaviour == null) {
			return 1;
		}
		return behaviour.getParallel();
	}

	private SmartFluidTankBehaviour getTank() {
		return getBehaviour(SmartFluidTankBehaviour.TYPE);
	}

	private FluidStack getCurrentFluid() {
		return getTank().getPrimaryHandler().getFluid();
	}

	public boolean isPowered() {
		return level != null && level.hasNeighborSignal(worldPosition);
	}

	@Override
	protected BeltProcessingBehaviour.ProcessingResult whenItemHeld(TransportedItemStack transported, TransportedItemStackHandlerBehaviour handler) {
		if (isPowered()) {
			return BeltProcessingBehaviour.ProcessingResult.HOLD;
		}

		if (processingTicks != -1 && processingTicks != 5) {
			return BeltProcessingBehaviour.ProcessingResult.HOLD;
		}
		if (level == null || !FillingBySpout.canItemBeFilled(level, transported.stack)) {
			return BeltProcessingBehaviour.ProcessingResult.PASS;
		}
		if (getTank().isEmpty()) {
			return BeltProcessingBehaviour.ProcessingResult.HOLD;
		}
		FluidStack fluid = getCurrentFluid();
		int requiredAmountForItem = FillingBySpout.getRequiredAmountForItem(level, transported.stack, fluid.copy());
		if (requiredAmountForItem == -1) {
			return BeltProcessingBehaviour.ProcessingResult.PASS;
		}
		if (requiredAmountForItem > fluid.getAmount()) {
			return BeltProcessingBehaviour.ProcessingResult.HOLD;
		}

		if (processingTicks == -1) {
			processingTicks = getFillingTime();
			notifyUpdate();
			AllSoundEvents.SPOUTING.playOnServer(
					level,
					worldPosition,
					0.75f,
					0.9f + 0.2f * new SecureRandom().nextFloat()
			);
			return BeltProcessingBehaviour.ProcessingResult.HOLD;
		}

		// 并行注液: 一次操作把整叠物品都注满, 每件都会从罐里扣一次流体 (fillItem 内部 shrink)
		int limit = Math.min(transported.stack.getMaxStackSize(), transported.stack.getCount());
		int affordable = requiredAmountForItem <= 0 ? getParallel() : fluid.getAmount() / requiredAmountForItem;
		int total = Math.min(getParallel(), Math.min(limit, affordable));

		List<ItemStack> results = new ArrayList<>();
		ItemStack remaining = transported.stack.copy();
		for (int i = 0; i < total; i++) {
			ItemStack out = FillingBySpout.fillItem(level, requiredAmountForItem, remaining, fluid);
			if (out.isEmpty()) {
				break;
			}
			merge(results, out);
		}

		if (!results.isEmpty()) {
			// 同一配方同一输入, 产物必然一致, 合并后只会剩一个栈
			ItemStack out = results.get(0);
			transported.stack.shrink(out.getCount());

			List<TransportedItemStack> outList = new ArrayList<>();
			TransportedItemStack held = null;
			TransportedItemStack result = transported.copy();
			result.stack = out;
			if (!transported.stack.isEmpty()) {
				held = transported.copy();
			}
			outList.add(result);
			handler.handleProcessingOnItem(transported, TransportedItemStackHandlerBehaviour.TransportedResult.convertToAndLeaveHeld(outList, held));
		}

		getTank().getPrimaryHandler().setFluid(fluid);
		sendSplash = true;
		notifyUpdate();
		return BeltProcessingBehaviour.ProcessingResult.HOLD;
	}

	@Override
	public void tick() {
		if (isPowered()) {
			return;
		}
		tickBehaviours();

		if (level == null) {
			return;
		}

		FluidStack currentFluidInTank = getCurrentFluid();

		if (processingTicks == -1 && (isVirtual() || !level.isClientSide()) && !currentFluidInTank.isEmpty()) {
			BlockPos filling = worldPosition.below(2);
			BlockSpoutingBehaviour behaviour = BlockSpoutingBehaviour.get(level, filling);
			if (customProcess != null) {
				return;
			}
			if (behaviour != null && behaviour.fillBlock(
					level,
					worldPosition.below(2),
					this,
					currentFluidInTank.copy(),
					true
			) > 0) {
				processingTicks = getFillingTime();
				customProcess = behaviour;
				notifyUpdate();
			}
		}

		if (processingTicks >= 0) {
			processingTicks--;
			if (processingTicks == getPourTick() && customProcess != null) {
				int fillBlock = customProcess.fillBlock(
						level,
						worldPosition.below(2),
						this,
						currentFluidInTank.copy(),
						false
				);
				customProcess = null;
				if (fillBlock > 0) {
					getTank().getPrimaryHandler().setFluid(FluidHelper.copyStackWithAmount(
							currentFluidInTank,
							currentFluidInTank.getAmount() - fillBlock)
					);
					sendSplash = true;
					notifyUpdate();
				}
			}
		}

		// 注液时间可短到 1 刻, 滴液阈值必须跟着走, 否则短周期下连粒子都没有
		int dripThreshold = Math.min(8, Math.max(1, getFillingTime() - 5));
		if (processingTicks >= dripThreshold && level.isClientSide()) {
			spawnProcessingParticles(getTank().getPrimaryTank().getRenderedFluid());
		}
	}

	private static void merge(List<ItemStack> stacks, ItemStack stack) {
		for (ItemStack existing : stacks) {
			if (ItemStack.isSameItemSameTags(existing, stack)) {
				existing.grow(stack.getCount());
				return;
			}
		}
		stacks.add(stack.copy());
	}

	private void tickBehaviours() {
		if (level == null) {
			return;
		}
		forEachBehaviour(BlockEntityBehaviour::tick);
	}

	protected void spawnProcessingParticles(FluidStack fluid) {
		if (isVirtual() || level == null) {
			return;
		}
		Vec3 vec = VecHelper.getCenterOf(worldPosition);
		vec = vec.subtract(0, 8 / 16.0f, 0);
		ParticleOptions particle = FluidFX.getFluidParticle(fluid);
		level.addAlwaysVisibleParticle(particle, vec.x, vec.y, vec.z, 0, -0.1f, 0);
	}
}