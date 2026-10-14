package dev.celestiacraft.create_steam_ages.client;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.celestiacraft.create_steam_ages.CreateSteamAges;
import dev.celestiacraft.create_steam_ages.common.register.CSAFluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 客户端专用初始化。
 * <p>
 * 该类被 {@link Mod.EventBusSubscriber} 标记为 {@link Dist#CLIENT}, Forge 的
 * AutomaticEventSubscriber 会先比对当前物理端再决定是否 forName + 注册该类,
 * 因此专用服务端不会加载本类, 也不会触碰 {@link RenderType} 等纯客户端类。
 * <p>
 * 流体的渲染层(RenderType)属于客户端渲染资源, 必须在这里注册;
 * 通用的静态注册链(CSAFluids)中不允许出现返回 RenderType 的 lambda。
 */
@Mod.EventBusSubscriber(
		modid = CreateSteamAges.MODID,
		bus = Mod.EventBusSubscriber.Bus.MOD,
		value = Dist.CLIENT
)
public final class CSAClientSetup {
	private CSAClientSetup() {
	}

	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		// FMLClientSetupEvent 是并行派发的, 渲染层表是普通 HashMap, 必须回到主线程写入
		event.enqueueWork(() -> {
			applyTranslucentLayer(CSAFluids.STEAM);
			applyTranslucentLayer(CSAFluids.HIGH_TEMPERATURE_STEAM);
		});
	}

	private static <T extends ForgeFlowingFluid> void applyTranslucentLayer(FluidEntry<T> entry) {
		RenderType layer = RenderType.translucent();
		// 源流体与流动流体是两个不同的 Fluid 实例, 而渲染层是按 FluidState#getType() 查表的, 两者都要设置
		Fluid source = entry.getSource();
		ItemBlockRenderTypes.setRenderLayer(entry.get(), layer);
		ItemBlockRenderTypes.setRenderLayer(source, layer);
	}
}
