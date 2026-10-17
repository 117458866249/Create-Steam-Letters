package dev.celestiacraft.steam_letters.common.register;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import net.minecraft.world.level.GameRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CreateLettersSteam.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SlGameRules {
	public static final GameRules.Key<GameRules.BooleanValue> STEAM_CAN_CONVERT_TO_SOURCE;

	static {
		STEAM_CAN_CONVERT_TO_SOURCE = GameRules.register(
				"steamCanConvertToSource",
				GameRules.Category.MISC,
				GameRules.BooleanValue.create(false)
		);
	}

	@SubscribeEvent
	public static void onCommonSetup(FMLCommonSetupEvent event) {
		CreateLettersSteam.LOGGER.debug("registered game rule {}", STEAM_CAN_CONVERT_TO_SOURCE);
	}
}