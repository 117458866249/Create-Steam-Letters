package dev.celestiacraft.csa.compat.jei;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.common.register.CsaBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CreateSteamAgesJeiPlugin implements IModPlugin {
	private IJeiHelpers helpers;
	private IRecipeCatalystRegistration registration;

	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return CsaApis.loadResource("jei_plugin");
	}

	@Override
	public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
		helpers = registration.getJeiHelpers();
		this.registration = registration;

		ItemStack hammer = CsaBlocks.STEAM_HAMMER.asStack();
		ItemStack mixer = CsaBlocks.STEAM_MIXER.asStack();
		ItemStack spout = CsaBlocks.ADVANCED_SPOUT.asStack();
		ItemStack basin = CsaBlocks.BRONZE_BASIN.asStack();

		addCatalyst(CsaApis.loadCreate("pressing"), hammer);
		addCatalyst(CsaApis.loadCreate("packing"), hammer);
		addCatalyst(CsaApis.loadCreate("automatic_packing"), hammer);

		addCatalyst(CsaApis.loadCreate("mixing"), mixer);
		addCatalyst(CsaApis.loadCreate("automatic_shapeless"), mixer);
		addCatalyst(CsaApis.loadCreate("automatic_brewing"), mixer);

		addCatalyst(CsaApis.loadCreate("spout_filling"), spout);

		if (ModList.get().isLoaded(CreateDieselGenerators.ID)) {
			addCatalyst(CsaApis.loadCDG("compression_molding"), hammer);
			addCatalyst(CsaApis.loadCDG("casting"), basin);
		}
	}

	@SuppressWarnings("removal")
	private void addCatalyst(ResourceLocation recipe, ItemStack catalyst) {
		helpers.getRecipeType(recipe)
				.ifPresent((type) -> {
					registration.addRecipeCatalyst(catalyst, type);
				});
	}
}