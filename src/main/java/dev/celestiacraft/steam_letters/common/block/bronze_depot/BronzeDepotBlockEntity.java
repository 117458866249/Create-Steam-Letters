package dev.celestiacraft.steam_letters.common.block.bronze_depot;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.content.logistics.depot.DepotBehaviour;
import com.simibubi.create.content.logistics.depot.DepotBlockEntity;
import com.simibubi.create.foundation.item.ItemHelper;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.Random;

public class BronzeDepotBlockEntity extends DepotBlockEntity {
	public static final int SLOT_COUNT = 16;
	public static final int INVENTORY_SLOTS = SLOT_COUNT - 1;
	public static final int STACK_SIZE = 64;

	@Getter
	private final BronzeDepotItemStackHandler inventory;
	private LazyOptional<IItemHandler> itemCapability;
	private int[] renderAngles;
	private long[] renderSeeds;
	private final Random renderRandom = new Random();

	public BronzeDepotBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);

		inventory = new BronzeDepotItemStackHandler(INVENTORY_SLOTS, this);
	}

	public int getRenderAngle(int slot) {
		ensureRenderRandom();

		return renderAngles[Mth.positiveModulo(slot, renderAngles.length)];
	}

	public Random getRenderRandom(int slot) {
		renderRandom.setSeed(getRenderSeed(slot));

		return renderRandom;
	}

	public long getRenderSeed(int slot) {
		ensureRenderRandom();

		return renderSeeds[Mth.positiveModulo(slot, renderSeeds.length)];
	}

	private void ensureRenderRandom() {
		if (renderAngles != null) {
			return;
		}

		SecureRandom random = new SecureRandom();
		renderAngles = new int[SLOT_COUNT];
		renderSeeds = new long[SLOT_COUNT];

		for (int slot = 0; slot < SLOT_COUNT; slot++) {
			renderAngles[slot] = random.nextInt(360);
			renderSeeds[slot] = random.nextLong();
		}
	}

	@Override
	public void tick() {
		super.tick();

		if (level == null || level.isClientSide) {
			return;
		}

		refillFromInventory();
	}

	private void refillFromInventory() {
		DepotBehaviour depot = getBehaviour(DepotBehaviour.TYPE);
		if (depot == null || !depot.isEmpty()) {
			return;
		}

		for (int slot = 0; slot < inventory.getSlots(); slot++) {
			ItemStack stored = inventory.getStackInSlot(slot);
			if (stored.isEmpty()) {
				continue;
			}

			ItemStack taken = inventory.extractItem(slot, stored.getMaxStackSize(), false);
			if (taken.isEmpty()) {
				continue;
			}

			depot.setHeldItem(new TransportedItemStack(taken));
			notifyUpdate();
			return;
		}
	}

	@Override
	public void destroy() {
		super.destroy();

		if (level != null) {
			ItemHelper.dropContents(level, worldPosition, inventory);
		}
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction side) {
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			LazyOptional<IItemHandler> existing = itemCapability;
			if (existing == null) {
				existing = LazyOptional.of(this::createInventoryHandler);
				itemCapability = existing;
			}

			return existing.cast();
		}

		return super.getCapability(capability, side);
	}

	private IItemHandler createInventoryHandler() {
		DepotBehaviour depot = getBehaviour(DepotBehaviour.TYPE);
		IItemHandler surface = depot == null ? new ItemStackHandler(0)
				: depot.getItemCapability(ForgeCapabilities.ITEM_HANDLER, null)
				.orElseGet(() -> new ItemStackHandler(0));

		return new SurfaceAwareItemHandler(inventory, surface);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();

		if (itemCapability != null) {
			itemCapability.invalidate();
			itemCapability = null;
		}
	}

	@Override
	protected void write(CompoundTag compound, boolean clientPacket) {
		super.write(compound, clientPacket);

		compound.put("Inventory", inventory.serializeNBT());
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		super.read(compound, clientPacket);

		if (!compound.contains("Inventory")) {
			return;
		}

		inventory.setSuppressUpdates(true);
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		inventory.setSuppressUpdates(false);
	}

	public InteractionResult use(Player player, InteractionHand hand, BlockHitResult ray) {
		if (level == null) {
			return InteractionResult.PASS;
		}

		boolean emptyHanded = player.getItemInHand(hand).isEmpty();
		if (!emptyHanded && ray.getDirection() != Direction.UP) {
			return InteractionResult.PASS;
		}
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		}

		if (emptyHanded) {
			boolean extracted = takeFromSurface(player);

			if (!extracted || player.isShiftKeyDown()) {
				extracted |= takeFromInventory(player, player.isShiftKeyDown());
			}

			if (!extracted) {
				return InteractionResult.PASS;
			}

			notifyUpdate();
			return InteractionResult.SUCCESS;
		}

		return insert(player, hand, player.getItemInHand(hand));
	}

	private boolean takeFromSurface(Player player) {
		DepotBehaviour depot = getBehaviour(DepotBehaviour.TYPE);
		if (depot == null) {
			return false;
		}

		ItemStack onSurface = depot.getHeldItemStack();
		if (onSurface.isEmpty()) {
			return false;
		}

		player.getInventory().placeItemBackInInventory(onSurface);
		depot.removeHeldItem();
		AllSoundEvents.DEPOT_PLOP.playOnServer(level, worldPosition);

		return true;
	}

	private boolean takeFromInventory(Player player, boolean extractAll) {
		boolean extracted = false;

		for (int slot = 0; slot < inventory.getSlots(); slot++) {
			ItemStack stored = inventory.getStackInSlot(slot);
			if (stored.isEmpty()) {
				continue;
			}

			int count = extractAll ? stored.getCount() : stored.getMaxStackSize();
			ItemStack taken = inventory.extractItem(slot, count, false);
			if (taken.isEmpty()) {
				continue;
			}

			player.getInventory().placeItemBackInInventory(taken);
			extracted = true;

			if (!extractAll) {
				break;
			}
		}

		if (extracted) {
			AllSoundEvents.DEPOT_PLOP.playOnServer(level, worldPosition);
		}

		return extracted;
	}

	private InteractionResult insert(Player player, InteractionHand hand, ItemStack held) {
		ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, held.copy(), false);
		if (remainder.getCount() == held.getCount()) {
			return InteractionResult.PASS;
		}

		player.setItemInHand(hand, remainder);
		AllSoundEvents.DEPOT_PLOP.playOnServer(level, worldPosition);
		notifyUpdate();

		return InteractionResult.SUCCESS;
	}
}