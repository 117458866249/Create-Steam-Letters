package dev.celestiacraft.steam_letters.compat.jei;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.block.burner.SolidBurnerBlock;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
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
		ItemStack saw = SlBlocks.STEAM_SAW.asStack();

		addCatalystsForBlock(RecipeTypes.FUELING, SolidBurnerBlock.class);

		addCatalyst(SlApis.loadCreate("pressing"), hammer);
		addCatalyst(SlApis.loadCreate("packing"), hammer);
		addCatalyst(SlApis.loadCreate("automatic_packing"), hammer);

		addCatalyst(SlApis.loadCreate("mixing"), mixer);
		addCatalyst(SlApis.loadCreate("automatic_shapeless"), mixer);
		addCatalyst(SlApis.loadCreate("automatic_brewing"), mixer);

		addCatalyst(SlApis.loadCreate("spout_filling"), spout);

		addCatalyst(SlApis.loadCreate("sawing"), saw);
		addCatalyst(SlApis.loadCreate("block_cutting"), saw);

		if (ModList.get().isLoaded(CreateDieselGenerators.ID)) {
			addCatalyst(SlApis.loadCDG("compression_molding"), hammer);
			addCatalyst(SlApis.loadCDG("casting"), basin);
		}
	}

	private void addCatalystsForBlock(RecipeType<?> type, Class<? extends Block> blockClass) {
		for (Block block : ForgeRegistries.BLOCKS.getValues()) {
			if (blockClass.isInstance(block)) {
				registration.addRecipeCatalyst(block, type);
			}
		}
	}

	private void addCatalystsForItem(RecipeType<?> type, Class<? extends Item> itemClass) {
		for (Item block : ForgeRegistries.ITEMS.getValues()) {
			if (itemClass.isInstance(block)) {
				registration.addRecipeCatalyst(block, type);
			}
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