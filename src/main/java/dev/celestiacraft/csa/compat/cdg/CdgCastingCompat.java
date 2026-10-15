package dev.celestiacraft.csa.compat.cdg;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import com.jesz.createdieselgenerators.content.molds.BasinSpoutCasting;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.common.register.CsaBlocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CreateSteamAges.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CdgCastingCompat {
	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			registerBehaviour();
		});
	}

	private static void registerBehaviour() {
		if (!ModList.get().isLoaded(CreateDieselGenerators.ID)) {
			return;
		}

		BlockSpoutingBehaviour.BY_BLOCK.register(CsaBlocks.BRONZE_BASIN.get(), new BasinSpoutCasting());
	}
}