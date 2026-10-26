package dev.celestiacraft.steam_letters.compat.jei;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.common.block.burner.FluidBurnerBlock;
import dev.celestiacraft.steam_letters.common.block.burner.SolidBurnerBlock;
import dev.celestiacraft.steam_letters.common.register.SlRecipeTypes;
import dev.celestiacraft.steam_letters.common.recipe.type.SolidFuelRecipe;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.compat.jei.api.JeiRecipeType;
import dev.celestiacraft.steam_letters.compat.jei.category.BurnerFuelCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class CreateLettersSteamJeiPlugin implements IModPlugin {
	private IJeiHelpers helpers;
	private IRecipeCatalystRegistration registration;

	@Override
	public @NotNull ResourceLocation getPluginUid() {
		return SlApis.loadResource("jei_plugin");
	}

	@Override
	public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();

		registration.addRecipeCategories(
				new BurnerFuelCategory(
						JeiRecipeType.SOLID_FUEL,
						Component.translatable(SlTranKeys.langKey(SlTranKeys.JEI_SOLID_FUEL)),
						helper.createDrawableItemStack(Items.COAL.getDefaultInstance())
				),
				new BurnerFuelCategory(
						JeiRecipeType.FLUID_FUEL,
						Component.translatable(SlTranKeys.langKey(SlTranKeys.JEI_FLUID_FUEL)),
						helper.createDrawableItemStack(Items.LAVA_BUCKET.getDefaultInstance())
				)
		);
	}

	@Override
	public void registerRecipes(@NotNull IRecipeRegistration registration) {
		MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
		if (server == null) {
			return;
		}

		RecipeManager manager = server.getRecipeManager();

		List<SolidFuelRecipe> solid = new ArrayList<>(manager.getAllRecipesFor(SlRecipeTypes.SOLID_FUEL.get()));
		solid.addAll(collectFurnaceFuels(solid));
		registration.addRecipes(JeiRecipeType.SOLID_FUEL, solid);

		registration.addRecipes(JeiRecipeType.FLUID_FUEL, manager.getAllRecipesFor(SlRecipeTypes.FLUID_FUEL.get()));
	}

	/**
	 * 固体燃烧室在没有命中 {@code steam_letters:solid_fuel} 配方时会回退到原版熔炉燃料表,
	 * 所以 JEI 里必须把这部分也列出来, 否则玩家只看得到自定义配方.
	 * <p>
	 * 已经被自定义配方覆盖的物品跳过, 避免同一个物品出现两页.
	 */
	private static List<SolidFuelRecipe> collectFurnaceFuels(List<SolidFuelRecipe> custom) {
		List<SolidFuelRecipe> derived = new ArrayList<>();
		for (Item item : ForgeRegistries.ITEMS) {
			ItemStack stack = item.getDefaultInstance();
			if (stack.isEmpty() || matchesAny(custom, stack)) {
				continue;
			}

			int time = ForgeHooks.getBurnTime(stack, net.minecraft.world.item.crafting.RecipeType.SMELTING);
			if (time <= 0) {
				continue;
			}

			ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
			if (key == null) {
				continue;
			}
			ResourceLocation id = SlApis.loadResource("solid_fuel/derived/" + key.getNamespace() + "/" + key.getPath());
			derived.add(new ProcessingRecipeBuilder<>(SolidFuelRecipe::new, id)
					.withItemIngredients(Ingredient.of(stack))
					.duration(time)
					.build());
		}
		return derived;
	}

	private static boolean matchesAny(List<SolidFuelRecipe> recipes, ItemStack stack) {
		for (SolidFuelRecipe recipe : recipes) {
			if (recipe.matches(stack)) {
				return true;
			}
		}
		return false;
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

		addCatalystsForBlock(JeiRecipeType.SOLID_FUEL, SolidBurnerBlock.class);
		addCatalystsForBlock(JeiRecipeType.FLUID_FUEL, FluidBurnerBlock.class);

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