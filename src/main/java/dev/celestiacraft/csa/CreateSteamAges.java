package dev.celestiacraft.csa;

import com.simibubi.create.api.stress.BlockStressValues;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.api.CsaStress;
import dev.celestiacraft.csa.client.block.CSABlockPartialModel;
import dev.celestiacraft.csa.common.register.*;
import dev.celestiacraft.csa.config.CommonConfigs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(CreateSteamAges.MODID)
public class CreateSteamAges {
	public static final String MODID = "create_steam_ages";
	public static final String NAME = "Create: \"Steam\" Ages";
	public static final Logger LOGGER = LogManager.getLogger(NAME);
	private static CsaStress STRESS_VALUES;
	private static ForgeConfigSpec STRESS_VALUES_SPEC;

	public CreateSteamAges(FMLJavaModLoadingContext context) {
		IEventBus bus = context.getModEventBus();

		CsaApis.REGISTRATE.registerEventListeners(bus);
		CsaMaterials.init();
		CsaItems.init();
		CsaBlocks.init();
		CsaBlockEntities.init();
		CsaFluids.init();
		CsaCreativeTabs.init(bus);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> {
			return () -> {
				CSABlockPartialModel.init();
			};
		});

		registerConfig(context);
	}

	private void registerConfig(FMLJavaModLoadingContext context) {
		context.registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC, "nebula/csa/common.toml");

		if (STRESS_VALUES == null) {
			Pair<CsaStress, ForgeConfigSpec> stressConfig = CsaStress.createConfig();
			STRESS_VALUES = stressConfig.getLeft();
			STRESS_VALUES_SPEC = stressConfig.getRight();

			BlockStressValues.IMPACTS.registerProvider(STRESS_VALUES::getImpact);
			BlockStressValues.CAPACITIES.registerProvider(STRESS_VALUES::getCapacity);
		}
		context.registerConfig(ModConfig.Type.SERVER, STRESS_VALUES_SPEC, "nebula/csa/server.toml");
	}

	private void onConfigLoad(ModConfigEvent.Loading event) {
		if (STRESS_VALUES != null && event.getConfig().getSpec().equals(STRESS_VALUES_SPEC)) {
			STRESS_VALUES.onLoad();
		}
	}

	private void onConfigReload(ModConfigEvent.Reloading event) {
		if (STRESS_VALUES != null && event.getConfig().getSpec().equals(STRESS_VALUES_SPEC)) {
			STRESS_VALUES.onReload();
		}
	}
}