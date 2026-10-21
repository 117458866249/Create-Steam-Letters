package dev.celestiacraft.steam_letters.client;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.client.block.SlBlockPartialModel;
import dev.celestiacraft.steam_letters.client.particle.SteamParticle;
import dev.celestiacraft.steam_letters.client.ponder.SlPonderPlugin;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlock;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlockEntity;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelInstance;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelRenderer;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import dev.celestiacraft.steam_letters.common.register.SlParticles;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
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
		event.enqueueWork(() -> {
			SlPonderPlugin.register();
			registerDynamicFlywheelVisuals();
			preloadFlywheelModels();

			applyTranslucentLayer(SlFluids.STEAM);
			applyTranslucentLayer(SlFluids.SUPERHEATED_STEAM);
		});
	}

	@SubscribeEvent
	public static void onRegisterParticleProviders(RegisterParticleProvidersEvent event) {
		event.registerSpriteSet(SlParticles.STEAM.get(), SteamParticle.Factory::new);
	}

	@SubscribeEvent
	public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
		for (BlockEntityType<SteamFlywheelBlockEntity> type : SteamFlywheelBlockEntity.DYNAMIC_TYPES) {
			event.registerBlockEntityRenderer(type, SteamFlywheelRenderer::new);
		}
	}

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