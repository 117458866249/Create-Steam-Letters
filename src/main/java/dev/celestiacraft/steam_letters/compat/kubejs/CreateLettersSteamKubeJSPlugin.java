package dev.celestiacraft.steam_letters.compat.kubejs;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.compat.kubejs.builder.*;
import dev.celestiacraft.steam_letters.compat.kubejs.schema.BurnerSchema;
import dev.celestiacraft.steam_letters.compat.kubejs.schema.FuelFluid;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;

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
		RegistryInfo.BLOCK.addType(
				SlApis.loadResource("steam_engine").toString(),
				SteamEngineDeviceBuilder.class,
				SteamEngineDeviceBuilder::new
		);
	}

	@Override
	public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
		event.namespace(CreateLettersSteam.MODID)
				.register("solid_fuel", BurnerSchema.SOLID_FUEL)
				.register("fluid_fuel", BurnerSchema.FLUID_FUEL);
	}
}