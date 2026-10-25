package dev.celestiacraft.steam_letters.common.block.burner;

import dev.celestiacraft.steam_letters.common.recipe.type.SolidFuelRecipe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * 固体燃烧室的方块实体.
 * <p>
 * 燃料匹配顺序: 自定义 {@code steam_letters:solid_fuel} 配方优先, 没命中才回退到原版熔炉燃料表;
 * 两者都不匹配的物品不能作为燃料. 配方只在「点燃下一份燃料」时查一次并缓存,
 * 不会每 tick 扫描配方表.
 */
public class SolidBurnerBlockEntity extends AbstractBurnerBlockEntity {
	private static final String NBT_FUEL = "Fuel";
	/**
	 * 燃料槽容量, 允许玩家一次性囤一小堆.
	 */
	public static final int FUEL_SLOT_LIMIT = 64;

	@Getter
	@Setter
	private ItemStack fuel = ItemStack.EMPTY;

	/**
	 * 上一次解析燃烧时间用的物品与结果, {@code cacheValid} 为 false 时表示还没有可用缓存.
	 */
	private ItemStack burnTimeKey = ItemStack.EMPTY;
	private int burnTimeValue;
	private boolean burnTimeCacheValid;

	private LazyOptional<IItemHandler> itemCapability;

	public SolidBurnerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemCapability = LazyOptional.of(() -> new FuelItemHandler(this));
	}

	@Override
	public void onLoad() {
		super.onLoad();
		invalidateBurnTimeCache();
	}

	@Override
	public void invalidate() {
		super.invalidate();
		invalidateBurnTimeCache();
	}

	@Override
	protected boolean tickFuel() {
		if (burnTime > 0) {
			burnTime--;
			return true;
		}

		if (fuel.isEmpty()) {
			return false;
		}

		int time = resolveBurnTime(fuel);
		if (time <= 0) {
			return false;
		}

		fuel.shrink(1);
		if (fuel.isEmpty()) {
			fuel = ItemStack.EMPTY;
		}
		burnTime = time;
		setChanged();
		return true;
	}

	/**
	 * @return 该物品的燃烧时间 (tick), 0 表示不能作为燃料
	 */
	public int resolveBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		}

		if (burnTimeCacheValid && !burnTimeKey.isEmpty() && ItemStack.isSameItemSameTags(burnTimeKey, stack)) {
			return burnTimeValue;
		}

		int time = 0;
		if (level != null) {
			SolidFuelRecipe recipe = SolidFuelRecipe.find(level, stack);
			if (recipe != null) {
				time = recipe.getTime();
			} else {
				time = ForgeHooks.getBurnTime(stack, RecipeType.SMELTING);
			}
		}

		burnTimeKey = stack.copy();
		burnTimeValue = time;
		burnTimeCacheValid = true;
		return time;
	}

	public boolean isFuel(ItemStack stack) {
		return resolveBurnTime(stack) > 0;
	}

	public void invalidateBurnTimeCache() {
		burnTimeCacheValid = false;
		burnTimeKey = ItemStack.EMPTY;
		burnTimeValue = 0;
	}

	/**
	 * 把玩家手里的物品放进燃料槽.
	 *
	 * @return 是否真的放进去了一些
	 */
	public boolean insertFuel(ItemStack stack) {
		if (stack.isEmpty() || !isFuel(stack)) {
			return false;
		}

		if (fuel.isEmpty()) {
			int moved = Math.min(FUEL_SLOT_LIMIT, stack.getCount());
			fuel = stack.copyWithCount(moved);
			stack.shrink(moved);
		} else {
			if (!ItemStack.isSameItemSameTags(fuel, stack)) {
				return false;
			}
			int space = FUEL_SLOT_LIMIT - fuel.getCount();
			if (space <= 0) {
				return false;
			}
			int moved = Math.min(space, stack.getCount());
			fuel.grow(moved);
			stack.shrink(moved);
		}

		setChanged();
		return true;
	}

	@Override
	public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction direction) {
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			return itemCapability.cast();
		}
		return super.getCapability(capability, direction);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		itemCapability.invalidate();
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		itemCapability = LazyOptional.of(() -> new FuelItemHandler(this));
	}

	@Override
	protected void write(CompoundTag tag, boolean clientPacket) {
		super.write(tag, clientPacket);
		if (!fuel.isEmpty()) {
			tag.put(NBT_FUEL, fuel.save(new CompoundTag()));
		}
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		fuel = tag.contains(NBT_FUEL) ? ItemStack.of(tag.getCompound(NBT_FUEL)) : ItemStack.EMPTY;
	}

	@Override
	public void destroy() {
		super.destroy();
		if (level == null || fuel.isEmpty()) {
			return;
		}

		ItemStack dropped = fuel;
		fuel = ItemStack.EMPTY;
		Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), dropped);
	}
}