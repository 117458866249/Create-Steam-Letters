package dev.celestiacraft.steam_letters.client;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.client.block.SlBlockPartialModel;
import dev.celestiacraft.steam_letters.client.particle.SteamParticle;
import dev.celestiacraft.steam_letters.client.ponder.SlPonderPlugin;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlock;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlockEntity;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelInstance;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelRenderer;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import dev.celestiacraft.steam_letters.common.register.SlParticles;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = CreateLettersSteam.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SlClientSetup {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		SlPonderPlugin.register();
		registerDynamicFlywheelVisuals();
		preloadFlywheelModels();

		event.enqueueWork(() -> {
			applyTranslucentLayer(SlFluids.STEAM);
			applyTranslucentLayer(SlFluids.SUPERHEATED_STEAM);
		});
	}

	@SubscribeEvent
	public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(SlParticles.STEAM.get(), SteamParticle.Factory::new);
	}

	/**
	 * KubeJS 动态注册出来的飞轮带着自己的方块实体类型, 走不到 Registrate 的 visual/renderer 链, 在这里补上.
	 */
	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		for (BlockEntityType<SteamFlywheelBlockEntity> type : SteamFlywheelBlockEntity.DYNAMIC_TYPES) {
			event.registerBlockEntityRenderer(type, SteamFlywheelRenderer::new);
		}
	}

	/**
	 * Flywheel 的 PartialModel 只在模型烘焙(RegisterAdditional)那一刻已经在 ALL 里的才会被加载,
	 * KubeJS 注册出来的飞轮材质是运行时才知道的, 所以要在烘焙之前把它们建好, 否则渲染出来是紫黑方块.
	 */
	private static void preloadFlywheelModels() {
		for (Block block : ForgeRegistries.BLOCKS) {
			if (block instanceof SteamFlywheelBlock flywheel) {
				SlBlockPartialModel.preloadFlywheel(flywheel.getMaterial());
			}
		}
	}

	private static void registerDynamicFlywheelVisuals() {
		for (BlockEntityType<SteamFlywheelBlockEntity> type : SteamFlywheelBlockEntity.DYNAMIC_TYPES) {
			SimpleBlockEntityVisualizer.builder(type)
					.factory(SteamFlywheelInstance::new)
					.neverSkipVanillaRender()
					.apply();
		}
	}


	private static <T extends ForgeFlowingFluid> void applyTranslucentLayer(FluidEntry<T> entry) {
		RenderType translucent = RenderType.translucent();
		Fluid flowing = entry.get();
		Fluid source = entry.getSource();

		ItemBlockRenderTypes.setRenderLayer(flowing, translucent);
		ItemBlockRenderTypes.setRenderLayer(source, translucent);
	}
}