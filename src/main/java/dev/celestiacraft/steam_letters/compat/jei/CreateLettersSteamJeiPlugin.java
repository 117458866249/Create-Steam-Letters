package dev.celestiacraft.steam_letters.compat.jei;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class CreateLettersSteamJeiPlugin implements IModPlugin {
	private IJeiHelpers helpers;
	private IRecipeCatalystRegistration registration;

	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return SlApis.loadResource("jei_plugin");
	}

	@Override
	public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
		helpers = registration.getJeiHelpers();
		this.registration = registration;

		ItemStack hammer = SlBlocks.STEAM_HAMMER.asStack();
		ItemStack mixer = SlBlocks.STEAM_MIXER.asStack();
		ItemStack spout = SlBlocks.ADVANCED_SPOUT.asStack();
		ItemStack basin = SlBlocks.BRONZE_BASIN.asStack();

		addCatalyst(SlApis.loadCreate("pressing"), hammer);
		addCatalyst(SlApis.loadCreate("packing"), hammer);
		addCatalyst(SlApis.loadCreate("automatic_packing"), hammer);

		addCatalyst(SlApis.loadCreate("mixing"), mixer);
		addCatalyst(SlApis.loadCreate("automatic_shapeless"), mixer);
		addCatalyst(SlApis.loadCreate("automatic_brewing"), mixer);

		addCatalyst(SlApis.loadCreate("spout_filling"), spout);

		if (ModList.get().isLoaded(CreateDieselGenerators.ID)) {
			addCatalyst(SlApis.loadCDG("compression_molding"), hammer);
			addCatalyst(SlApis.loadCDG("casting"), basin);
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