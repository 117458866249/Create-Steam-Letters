package dev.celestiacraft.csa.client;

import com.tterrag.registrate.util.entry.FluidEntry;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.common.register.CsaFluids;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = CreateSteamAges.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CsaClientSetup {
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			applyTranslucentLayer(CsaFluids.STEAM);
			applyTranslucentLayer(CsaFluids.SUPERHEATED_STEAM);
		});
	}

	private static <T extends ForgeFlowingFluid> void applyTranslucentLayer(FluidEntry<T> entry) {
		RenderType translucent = RenderType.translucent();
		Fluid source = entry.getSource();

		ItemBlockRenderTypes.setRenderLayer(entry.get(), translucent);
		ItemBlockRenderTypes.setRenderLayer(source, translucent);
	}
}