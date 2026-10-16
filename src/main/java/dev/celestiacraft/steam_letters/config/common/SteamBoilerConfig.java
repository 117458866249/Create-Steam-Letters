package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraftforge.common.ForgeConfigSpec;

/**
 * 锅炉效率阶梯配置.
 * <p>
 * 阈值以「热量占燃烧室最高热量的百分比」表达, 默认 25/50/75/100 即文档中的 25/50/75/100 刻度
 * (默认 maxHeat = 100 时两者等价). 热量低于最低阈值时锅炉不产汽.
 */
public class SteamBoilerConfig extends ConfigModule {
	public static ForgeConfigSpec.IntValue
			EFFICIENCY_THRESHOLD_1, EFFICIENCY_THRESHOLD_2,
			EFFICIENCY_THRESHOLD_3, EFFICIENCY_THRESHOLD_4;

	public static ForgeConfigSpec.DoubleValue
			EFFICIENCY_1, EFFICIENCY_2, EFFICIENCY_3, EFFICIENCY_4;

	public SteamBoilerConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam_boiler");
	}

	@Override
	protected void addConfigs() {
		EFFICIENCY_THRESHOLD_1 = builder.translation(key("efficiency_threshold_1"))
				.comment("Lowest heat percentage at which the boiler starts producing steam.")
				.defineInRange("efficiency_threshold_1", 25, 0, 100);
		EFFICIENCY_THRESHOLD_2 = builder.translation(key("efficiency_threshold_2"))
				.defineInRange("efficiency_threshold_2", 50, 0, 100);
		EFFICIENCY_THRESHOLD_3 = builder.translation(key("efficiency_threshold_3"))
				.defineInRange("efficiency_threshold_3", 75, 0, 100);
		EFFICIENCY_THRESHOLD_4 = builder.translation(key("efficiency_threshold_4"))
				.comment("Heat percentage required for the boiler to run at full efficiency.")
				.defineInRange("efficiency_threshold_4", 100, 0, 100);

		EFFICIENCY_1 = builder.translation(key("efficiency_1"))
				.defineInRange("efficiency_1", 0.25D, 0.0D, 1.0D);
		EFFICIENCY_2 = builder.translation(key("efficiency_2"))
				.defineInRange("efficiency_2", 0.5D, 0.0D, 1.0D);
		EFFICIENCY_3 = builder.translation(key("efficiency_3"))
				.defineInRange("efficiency_3", 0.75D, 0.0D, 1.0D);
		EFFICIENCY_4 = builder.translation(key("efficiency_4"))
				.defineInRange("efficiency_4", 1.0D, 0.0D, 1.0D);
	}

	/**
	 * 按热量百分比取得当前运行效率.
	 *
	 * @param heatPercent 热量占燃烧室最高热量的百分比
	 * @return 0.0 表示不产汽
	 */
	public static double getEfficiency(double heatPercent) {
		double value = Math.min(100.0D, Math.max(0.0D, heatPercent));
		if (value >= EFFICIENCY_THRESHOLD_4.get()) {
			return EFFICIENCY_4.get();
		}
		if (value >= EFFICIENCY_THRESHOLD_3.get()) {
			return EFFICIENCY_3.get();
		}
		if (value >= EFFICIENCY_THRESHOLD_2.get()) {
			return EFFICIENCY_2.get();
		}
		if (value >= EFFICIENCY_THRESHOLD_1.get()) {
			return EFFICIENCY_1.get();
		}
		return 0.0D;
	}
}