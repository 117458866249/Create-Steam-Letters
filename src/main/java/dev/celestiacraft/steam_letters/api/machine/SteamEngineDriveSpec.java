package dev.celestiacraft.steam_letters.api.machine;

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

/**
 * 飞轮引擎的动力规格.
 * <p>
 * 与 {@link SteamEngineSpec} (锅炉/燃烧室的罐容量、产汽量、最大热量) 是两套完全独立的参数:
 * 那一套描述「蒸汽产能」, 这一套描述「蒸汽做功」. 飞轮本身不持有任何参数, 全部参数都挂在引擎上.
 * <p>
 * 所有数值都用 Supplier 承载, 因为方块注册发生在配置加载之前.
 */
public class SteamEngineDriveSpec {
	private final DoubleSupplier strength;
	private final DoubleSupplier speed;
	private final DoubleSupplier consumption;
	private final IntSupplier storage;

	private SteamEngineDriveSpec(DoubleSupplier strength, DoubleSupplier speed, DoubleSupplier consumption, IntSupplier storage) {
		this.strength = strength;
		this.speed = speed;
		this.consumption = consumption;
		this.storage = storage;
	}

	/**
	 * 普通蒸汽满负荷时的应力输出 (SU). 过热蒸汽在此基础上再乘过热输出倍率.
	 */
	public double getStrength() {
		return strength.getAsDouble();
	}

	/**
	 * 输出转速 (RPM). 从预热第一 tick 起就是该值, 不随预热进度变化.
	 */
	public double getSpeed() {
		return speed.getAsDouble();
	}

	/**
	 * 普通蒸汽满负荷时每 tick 消耗的蒸汽量 (mB/tick).
	 */
	public double getConsumption() {
		return consumption.getAsDouble();
	}

	/**
	 * 引擎自带蒸汽罐的容量 (mB).
	 */
	public int getStorage() {
		return storage.getAsInt();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private DoubleSupplier strength = () -> 0.0D;
		private DoubleSupplier speed = () -> 0.0D;
		private DoubleSupplier consumption = () -> 0.0D;
		private IntSupplier storage = () -> 0;

		public Builder strength(double value) {
			return strength(() -> value);
		}

		public Builder strength(DoubleSupplier supplier) {
			this.strength = supplier;
			return this;
		}

		public Builder speed(double value) {
			return speed(() -> value);
		}

		public Builder speed(DoubleSupplier supplier) {
			this.speed = supplier;
			return this;
		}

		public Builder consumption(double value) {
			return consumption(() -> value);
		}

		public Builder consumption(DoubleSupplier supplier) {
			this.consumption = supplier;
			return this;
		}

		public Builder storage(int value) {
			return storage(() -> value);
		}

		public Builder storage(IntSupplier supplier) {
			this.storage = supplier;
			return this;
		}

		public SteamEngineDriveSpec build() {
			return new SteamEngineDriveSpec(strength, speed, consumption, storage);
		}
	}
}
