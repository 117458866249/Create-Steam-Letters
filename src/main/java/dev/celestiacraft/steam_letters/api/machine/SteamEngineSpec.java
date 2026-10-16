package dev.celestiacraft.steam_letters.api.machine;

import dev.latvian.mods.rhino.util.RemapForJS;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

/**
 * 蒸汽设备在注册时写入的机制参数.
 * <p>
 * 全部用 {@link IntSupplier} / {@link DoubleSupplier} 承载, 而不是裸数值: 内置设备的数值来自配置文件,
 * 而配置在方块注册时尚未加载, 因此只保存取值方式, 真正读取发生在运行时.
 * KubeJS 等外部注册方则直接闭包成常量.
 * <p>
 * 数值为 0 表示该设备不使用这项参数 (例如固体燃烧室没有流体罐).
 */
public class SteamEngineSpec {
	private final IntSupplier capacity;
	private final DoubleSupplier production;
	private final DoubleSupplier maxHeat;

	private SteamEngineSpec(IntSupplier capacity, DoubleSupplier production, DoubleSupplier maxHeat) {
		this.capacity = capacity;
		this.production = production;
		this.maxHeat = maxHeat;
	}

	/**
	 * @return 单个流体罐的容量 (mB), 0 表示该设备不含流体罐
	 */
	public int getCapacity() {
		return Math.max(0, capacity.getAsInt());
	}

	/**
	 * @return 100% 效率下每 tick 的基础蒸汽产出量 (mB/tick), 0 表示该设备不产汽
	 */
	public double getProduction() {
		return Math.max(0.0D, production.getAsDouble());
	}

	/**
	 * @return 热量上限, 0 表示该设备不产生热量
	 */
	public double getMaxHeat() {
		return Math.max(0.0D, maxHeat.getAsDouble());
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * 逐项写入注册参数. 未显式写入的项保持 0, 即该设备不使用这项参数.
	 */
	public static class Builder {
		private IntSupplier capacity = () -> 0;
		private DoubleSupplier production = () -> 0.0D;
		private DoubleSupplier maxHeat = () -> 0.0D;

		@RemapForJS("capacitySupplier")
		public Builder capacity(IntSupplier value) {
			capacity = value;
			return this;
		}

		public Builder capacity(int value) {
			return capacity(() -> value);
		}

		@RemapForJS("productionSupplier")
		public Builder production(DoubleSupplier value) {
			production = value;
			return this;
		}

		public Builder production(double value) {
			return production(() -> value);
		}

		@RemapForJS("maxHeatSupplier")
		public Builder maxHeat(DoubleSupplier value) {
			maxHeat = value;
			return this;
		}

		public Builder maxHeat(double value) {
			return maxHeat(() -> value);
		}

		public SteamEngineSpec build() {
			return new SteamEngineSpec(capacity, production, maxHeat);
		}
	}
}