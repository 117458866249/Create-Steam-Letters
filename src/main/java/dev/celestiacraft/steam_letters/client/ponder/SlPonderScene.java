package dev.celestiacraft.steam_letters.client.ponder;

import com.simibubi.create.infrastructure.ponder.AllCreatePonderTags;
import dev.celestiacraft.steam_letters.client.ponder.scene.*;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

public class SlPonderScene {
	public static void register(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		steamHammer(helper);
		steamMixer(helper);
		steamSaw(helper);
		advancedSpout(helper);
		steamEngine(helper);
		solarBoiler(helper);
	}

	private static void steamHammer(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		helper.forComponents(SlBlocks.STEAM_HAMMER.asItem())
				.addStoryBoard(
						"steam_hammer",
						SteamHammerScene::usage,
						AllCreatePonderTags.KINETIC_APPLIANCES,
						SlPonderTags.SL
				);
	}

	private static void steamMixer(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		helper.forComponents(SlBlocks.STEAM_MIXER.asItem())
				.addStoryBoard(
						"steam_mixer",
						SteamMixerScene::usage,
						AllCreatePonderTags.KINETIC_APPLIANCES,
						SlPonderTags.SL
				);
	}

	private static void steamSaw(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		helper.forComponents(SlBlocks.STEAM_SAW.asItem())
				.addStoryBoard(
						"steam_saw",
						SteamSawScene::usage,
						AllCreatePonderTags.KINETIC_APPLIANCES,
						SlPonderTags.SL
				);
	}

	private static void advancedSpout(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		helper.forComponents(SlBlocks.ADVANCED_SPOUT.asItem())
				.addStoryBoard(
						"advanced_spout",
						AdvancedSpoutScene::usage,
						AllCreatePonderTags.FLUIDS,
						SlPonderTags.SL
				);
	}

	private static void steamEngine(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		helper.forComponents(
						SlBlocks.BRONZE_BOILER.asItem(),
						SlBlocks.BRONZE_SOLID_BURNER.asItem(),
						SlBlocks.BRONZE_FLUID_BURNER.asItem(),
						SlBlocks.BRONZE_STEAM_FLYWHEEL.asItem(),
						SlBlocks.BRONZE_STEAM_ENGINE.asItem()
				)
				.addStoryBoard(
						"steam_engine",
						SteamEngineScene::usage,
						AllCreatePonderTags.FLUIDS,
						AllCreatePonderTags.KINETIC_SOURCES,
						SlPonderTags.SL
				);
	}

	private static void solarBoiler(@NotNull PonderSceneRegistrationHelper<Item> helper) {
		helper.forComponents(
						SlBlocks.BRONZE_SOLAR_BOILER.asItem()
				)
				.addStoryBoard(
						"solar_boiler",
						SolarBoilerScene::usage,
						AllCreatePonderTags.FLUIDS,
						AllCreatePonderTags.KINETIC_SOURCES,
						SlPonderTags.SL
				);
	}
}