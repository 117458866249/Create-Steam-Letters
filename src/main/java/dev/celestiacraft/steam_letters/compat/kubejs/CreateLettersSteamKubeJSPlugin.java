package dev.celestiacraft.steam_letters.compat.kubejs;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.register.SlRecipeTypes;
import dev.celestiacraft.steam_letters.compat.kubejs.builder.*;
import dev.celestiacraft.steam_letters.compat.kubejs.schema.BurnerSchema;
import dev.celestiacraft.steam_letters.compat.kubejs.schema.FuelFluid;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

public class CreateLettersSteamKubeJSPlugin extends KubeJSPlugin {
	@Override
	public void init() {
		addType();
	}

	@Override
	public void registerBindings(BindingsEvent event) {
		event.add("CreateLettersSteam", CreateLettersSteam.class);
		event.add("SlApis", SlApis.class);
		event.add("FuelFluid", FuelFluid.class);
	}

	private void addType() {
		RegistryInfo.BLOCK.addType(
				SlApis.loadResource("boiler").toString(),
				BoilerBuilder.class,
				BoilerBuilder::new
		);
		RegistryInfo.BLOCK.addType(
				SlApis.loadResource("solar").toString(),
				SolarBoilerBuilder.class,
				SolarBoilerBuilder::new
		);
		RegistryInfo.BLOCK.addType(
				SlApis.loadResource("solid_burner").toString(),
				SolidBurnerBuilder.class,
				SolidBurnerBuilder::new
		);
		RegistryInfo.BLOCK.addType(
				SlApis.loadResource("fluid_burner").toString(),
				FluidBurnerBuilder.class,
				FluidBurnerBuilder::new
		);
		// 引擎和飞轮永远成对注册, 只留一个类型: 脚本 id 当材质前缀,
		// event.create("cmi:cast_iron") -> cmi:cast_iron_steam_engine + cmi:cast_iron_steam_flywheel
		RegistryInfo.BLOCK.addType(
				SlApis.loadResource("engine_and_flywheel").toString(),
				SteamEngineDeviceBuilder.class,
				(id) -> {
					return new SteamEngineDeviceBuilder(SteamEngineDeviceBuilder.engineIdOf(id));
				}
		);
	}

	@Override
	public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
		event.namespace(CreateLettersSteam.MODID)
				.register(getRecipeType(SlRecipeTypes.SOLID_FUEL), BurnerSchema.SOLID_FUEL)
				.register(getRecipeType(SlRecipeTypes.FLUID_FUEL), BurnerSchema.FLUID_FUEL);
	}

	private String getRecipeType(RegistryObject<? extends RecipeType<?>> recipe) {
		return recipe.getId().getPath();
	}
}