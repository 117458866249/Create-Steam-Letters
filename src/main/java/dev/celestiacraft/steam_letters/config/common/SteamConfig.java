package dev.celestiacraft.steam_letters.config.common;

import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public class SteamConfig extends ConfigModule {
	public static ForgeConfigSpec.DoubleValue
			SUPERHEATED_STEAM_CONSUMPTION_MULTIPLIER;

	public static ForgeConfigSpec.ConfigValue<String> OUTPUT_STEAM_FLUID;

	public SteamConfig(ForgeConfigSpec.Builder builder) {
		super(builder, "steam");
	}

	@Override
	protected void addConfigs() {
		SUPERHEATED_STEAM_CONSUMPTION_MULTIPLIER = builder.translation(key("superheated_steam_consumption_multiplier"))
				.defineInRange("superheated_steam_consumption_multiplier", 0.25, 0.0, 1.0);

		OUTPUT_STEAM_FLUID = builder.translation(key("output_steam_fluid"))
				.comment(commentKeyOf("output_steam_fluid"))
				.define("output_steam_fluid", "steam_letters:steam");
	}

	/**
	 * 解析配置里声明的输出蒸汽流体.
	 * <p>
	 * 修改该配置不会转换 / 清空 / 销毁任何已在世界里的流体, 只会影响之后新产出的蒸汽.
	 *
	 * @return 配置无效时回退到本模组自带的蒸汽
	 */
	public static Fluid getOutputSteamFluid() {
		return resolveFluid(OUTPUT_STEAM_FLUID.get());
	}

	@Nullable
	private static Fluid resolveFluid(String value) {
		ResourceLocation id = ResourceLocation.tryParse(value);
		if (id == null) {
			return SlApis.loadResource("steam") == null ? null : ForgeRegistries.FLUIDS.getValue(SlApis.loadResource("steam"));
		}
		Fluid fluid = ForgeRegistries.FLUIDS.getValue(id);
		if (fluid == null) {
			return ForgeRegistries.FLUIDS.getValue(SlApis.loadResource("steam"));
		}
		return fluid;
	}
}