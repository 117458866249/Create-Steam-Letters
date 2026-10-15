package dev.celestiacraft.steam_letters.compat.cdg;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import com.jesz.createdieselgenerators.content.molds.BasinSpoutCasting;
import com.simibubi.create.api.behaviour.spouting.BlockSpoutingBehaviour;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CreateLettersSteam.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CdgCompat {
	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			if (ModList.get().isLoaded(CreateDieselGenerators.ID)) {
				BlockSpoutingBehaviour.BY_BLOCK.register(SlBlocks.BRONZE_BASIN.get(), new BasinSpoutCasting());
			}
		});
	}
}