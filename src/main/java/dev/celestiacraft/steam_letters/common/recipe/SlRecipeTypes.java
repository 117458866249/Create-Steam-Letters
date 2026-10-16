package dev.celestiacraft.steam_letters.common.recipe;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipe;
import dev.celestiacraft.steam_letters.common.recipe.type.FluidFuelRecipeSerializer;
import dev.celestiacraft.steam_letters.common.recipe.type.SolidFuelRecipe;
import dev.celestiacraft.steam_letters.common.recipe.type.SolidFuelRecipeSerializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SlRecipeTypes {
	public static final DeferredRegister<RecipeType<?>> TYPES;
	public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS;

	public static final RegistryObject<RecipeType<SolidFuelRecipe>> SOLID_FUEL;
	public static final RegistryObject<RecipeSerializer<SolidFuelRecipe>> SOLID_FUEL_SERIALIZER;

	public static final RegistryObject<RecipeType<FluidFuelRecipe>> FLUID_FUEL;
	public static final RegistryObject<RecipeSerializer<FluidFuelRecipe>> FLUID_FUEL_SERIALIZER;

	static {
		TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CreateLettersSteam.MODID);
		SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CreateLettersSteam.MODID);

		SOLID_FUEL = TYPES.register("solid_fuel", () -> {
			return RecipeType.simple(SlApis.loadResource("solid_fuel"));
		});
		SOLID_FUEL_SERIALIZER = SERIALIZERS.register("solid_fuel", SolidFuelRecipeSerializer::new);

		FLUID_FUEL = TYPES.register("fluid_fuel", () -> {
			return RecipeType.simple(SlApis.loadResource("fluid_fuel"));
		});
		FLUID_FUEL_SERIALIZER = SERIALIZERS.register("fluid_fuel", FluidFuelRecipeSerializer::new);
	}

	public static void init(IEventBus bus) {
		TYPES.register(bus);
		SERIALIZERS.register(bus);
	}
}