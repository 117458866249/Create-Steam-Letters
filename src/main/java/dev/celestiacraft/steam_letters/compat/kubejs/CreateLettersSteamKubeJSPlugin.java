package dev.celestiacraft.steam_letters.compat.kubejs;

import com.probejs.features.plugin.DocGenerationEventJS;
import com.probejs.features.plugin.ProbeJSEvents;
import com.probejs.jdoc.property.PropertyModify;
import com.probejs.jdoc.property.PropertyType;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.compat.kubejs.builder.*;
import dev.celestiacraft.steam_letters.compat.kubejs.schema.BurnerSchema;
import dev.celestiacraft.steam_letters.compat.kubejs.schema.FuelFluid;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;

public class CreateLettersSteamKubeJSPlugin extends KubeJSPlugin {
	private boolean docGenRegistered;

	@Override
	public void init() {
		addType();
	}

	@Override
	public void registerBindings(BindingsEvent event) {
		event.add("CreateLettersSteam", CreateLettersSteam.class);
		event.add("SlApis", SlApis.class);
		event.add("FuelFluid", FuelFluid.class);

		if (!docGenRegistered && event.getType().equals(ScriptType.SERVER)) {
			docGenRegistered = true;
			ProbeJSEvents.DOC_GEN.listenJava(ScriptType.SERVER, null, this::onDocGeneration);
		}
	}

	private Object onDocGeneration(EventJS event) {
		if (!(event instanceof DocGenerationEventJS docGen)) {
			return null;
		}
		String $FuelFluid = "dev.celestiacraft.steam_letters.compat.kubejs.schema.FuelFluid";
		docGen.transformByName($FuelFluid, (document) -> {
			document.getMethods().forEach((method) -> {
				if (method.getName().equals("ofTag")) {
					method.addProperty(new PropertyModify(
							0,
							new PropertyType.Native("Special.FluidTag"),
							null
					));
				}
			});
		});
		return null;
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
