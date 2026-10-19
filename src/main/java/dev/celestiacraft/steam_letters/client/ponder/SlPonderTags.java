package dev.celestiacraft.steam_letters.client.ponder;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class SlPonderTags {
	public static final ResourceLocation MOD;

	static {
		MOD = SlApis.loadResource(CreateLettersSteam.MODID);
	}

	public static void register(@NotNull PonderTagRegistrationHelper<ResourceLocation> helper) {
		helper.registerTag(MOD)
				.addToIndex()
				.item(SlBlocks.BRONZE_STEAM_FLYWHEEL, true, false)
				.title("机械动力: 来自蒸汽的信")
				.description("设备")
				.register();
	}
}