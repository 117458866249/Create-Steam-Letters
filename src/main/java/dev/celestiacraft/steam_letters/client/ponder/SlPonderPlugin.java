package dev.celestiacraft.steam_letters.client.ponder;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class SlPonderPlugin implements PonderPlugin {
	@Override
	public @NotNull String getModId() {
		return CreateLettersSteam.MODID;
	}

	@Override
	public void registerScenes(@NotNull PonderSceneRegistrationHelper<ResourceLocation> helper) {
		SlPonderScene.register(helper(helper));
	}

	private static PonderSceneRegistrationHelper<Item> helper(PonderSceneRegistrationHelper<ResourceLocation> helper) {
		return helper.withKeyFunction(ForgeRegistries.ITEMS::getKey);
	}
}