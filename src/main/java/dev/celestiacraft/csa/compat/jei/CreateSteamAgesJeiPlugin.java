package dev.celestiacraft.csa.compat.jei;

import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.common.register.CsaBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
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

		helpers.getRecipeType(Create.asResource("pressing"), PressingRecipe.class)
				.ifPresent((type) -> {
					registration.addRecipeCatalyst(CsaBlocks.STEAM_HAMMER.asStack(), type);
				});
	}
}