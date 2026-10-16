package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

public abstract class SteamDeviceBuilder<B extends Block, E extends BlockEntity> extends BuilderBase<Block> {
	/**
	 * 注册 ID 的 path 段(方块与方块实体共用)
	 */
	protected final String path;
	/**
	 * 内部流体容量(mB), null 表示沿用配置默认值
	 */
	protected Integer capacity;
	/**
	 * 满效率蒸汽产出速率(mB/tick), null 表示沿用配置默认值
	 */
	protected Double production;
	/**
	 * 最高热量, null 表示沿用配置默认值
	 */
	protected Double maxHeat;
	/**
	 * 材质目录名, 决定默认模型路径 block/steam/<设备>/<材质>
	 */
	protected String material = "bronze";
	/**
	 * 完整模型路径覆盖, 非空时忽略 material
	 */
	protected String model;
	protected BlockEntry<B> blockEntry;
	protected BlockEntityEntry<E> blockEntityEntry;

	protected SteamDeviceBuilder(ResourceLocation id) {
		super(id);
		path = id.getPath();
	}

	/**
	 * KubeJS 把方块与方块实体的命名空间固定为本模组 ID(steam_letters),
	 * 因此脚本里 ID 的命名空间部分不会生效, 只有 path 段被使用.
	 */
	@Override
	public RegistryInfo getRegistryType() {
		return RegistryInfo.BLOCK;
	}

	/**
	 * 本类型不走 KubeJS 的对象创建路径(见 {@link #createAdditionalObjects()}),
	 * 因此这里不需要、也无法提供实例.
	 */
	@Override
	public Block createObject() {
		throw new IllegalStateException("steam_letters: KubeJS 设备 '" + path + "' 由 Registrate 注册, 不应走到 KubeJS 的对象创建路径");
	}

	@Override
	public void createAdditionalObjects() {
		if (blockEntry != null) {
			return;
		}

		registerDevice();
		// 已由 Registrate 排队注册, 让 KubeJS 跳过自己的注册流程, 否则同一个 ID 会被注册两次
		dummyBuilder = true;
	}

	/**
	 * 用 Registrate 排队注册方块、物品与方块实体类型
	 */
	protected abstract void registerDevice();

	/**
	 * 默认模型路径所在的目录, 例如 {@code block/steam/boiler}
	 */
	protected abstract String devicePath();

	@Info("方块模型与贴图所在的子目录名, 默认 bronze. 例如 material('steel') 会使用 block/steam/<设备>/steel.")
	public SteamDeviceBuilder<B, E> material(String value) {
		material = value;
		return this;
	}

	@Info("完整模型路径覆盖, 例如 block/steam/boiler/bronze. 设置后忽略 material.")
	public SteamDeviceBuilder<B, E> model(String value) {
		model = value;
		return this;
	}

	@Info("内部流体容量, 单位 mB.")
	public SteamDeviceBuilder<B, E> capacity(int value) {
		capacity = value;
		return this;
	}

	@Info("满效率下的基础蒸汽产出速率, 单位 mB/tick.")
	public SteamDeviceBuilder<B, E> production(double value) {
		production = value;
		return this;
	}

	@Info("最高热量值. 燃烧室升温/降温速率均以它的百分比为基准.")
	public SteamDeviceBuilder<B, E> maxHeat(double value) {
		maxHeat = value;
		return this;
	}

	protected String basePath() {
		return model == null || model.isEmpty() ? devicePath() + "/" + material : model;
	}

	protected IntSupplier capacityOr(IntSupplier fallback) {
		return capacity == null ? fallback : capacity::intValue;
	}

	protected DoubleSupplier productionOr(DoubleSupplier fallback) {
		return production == null ? fallback : production::doubleValue;
	}

	protected DoubleSupplier maxHeatOr(DoubleSupplier fallback) {
		return maxHeat == null ? fallback : maxHeat::doubleValue;
	}

	/**
	 * 由注册 ID 推导显示名, 例如 bronze_boiler -> Bronze Boiler
	 */
	protected String displayName() {
		StringBuilder builder = new StringBuilder();
		for (String part : path.split("_")) {
			if (part.isEmpty()) {
				continue;
			}
			if (!builder.isEmpty()) {
				builder.append(' ');
			}
			builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}
		return builder.isEmpty() ? path : builder.toString();
	}
}