package dev.celestiacraft.csa.compat.jei;

import com.simibubi.create.Create;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.common.register.CsaBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CreateSteamAgesJeiPlugin implements IModPlugin {
	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return CsaApis.loadResource("jei_plugin");
	}

	@Override
	public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
		IJeiHelpers helpers = registration.getJeiHelpers();
		ItemStack hammer = CsaBlocks.STEAM_HAMMER.asStack();
		ItemStack mixer = CsaBlocks.STEAM_MIXER.asStack();

		addCatalyst("pressing", hammer, helpers, registration);
		addCatalyst("packing", hammer, helpers, registration);
		addCatalyst("automatic_packing", hammer, helpers, registration);

		addCatalyst("mixing", mixer, helpers, registration);
		addCatalyst("automatic_shapeless", mixer, helpers, registration);
		addCatalyst("automatic_brewing", mixer, helpers, registration);
	}

	@SuppressWarnings("removal")
	private void addCatalyst(
			String recipeId,
			ItemStack catalyst,
			IJeiHelpers helpers,
			IRecipeCatalystRegistration registration
	) {
		helpers.getRecipeType(Create.asResource(recipeId))
				.ifPresent((type) -> {
					registration.addRecipeCatalyst(catalyst, type);
				});
	}
}