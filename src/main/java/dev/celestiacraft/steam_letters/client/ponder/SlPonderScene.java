package dev.celestiacraft.steam_letters.client.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import dev.celestiacraft.steam_letters.client.ponder.scene.SteamHammerScene;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class SlPonderScene {
	public static void register(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		steamHammer(helper);
	}

	private static void steamHammer(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		helper.forComponents(SlBlocks.STEAM_HAMMER.asItem())
				.addStoryBoard(
						"steam_hammer/usage",
						SteamHammerScene::usage,
						AllCreatePonderTags.KINETIC_APPLIANCES
				);
	}
}