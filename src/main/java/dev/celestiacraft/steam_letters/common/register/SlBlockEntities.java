package dev.celestiacraft.steam_letters.common.register;

import com.simibubi.create.content.kinetics.saw.SawRenderer;
import com.simibubi.create.content.kinetics.saw.SawVisual;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.block.advanced_spout.*;
import dev.celestiacraft.steam_letters.common.block.boiler.*;
import dev.celestiacraft.steam_letters.common.block.bronze_basin.*;
import dev.celestiacraft.steam_letters.common.block.burner.*;
import dev.celestiacraft.steam_letters.common.block.engine.*;
import dev.celestiacraft.steam_letters.common.block.flywheel.*;
import dev.celestiacraft.steam_letters.common.block.steam_hammer.*;
import dev.celestiacraft.steam_letters.common.block.steam_mixer.*;
import dev.celestiacraft.steam_letters.common.block.steam_saw.SteamSawBlockEntity;

public class SlBlockEntities {
	public static final BlockEntityEntry<BronzeBasinBlockEntity> BRONZE_BASIN;
	public static final BlockEntityEntry<AdvancedSpoutBlockEntity> ADVANCED_SPOUT;
	public static final BlockEntityEntry<SteamHammerBlockEntity> STEAM_HAMMER;
	public static final BlockEntityEntry<SteamMixerBlockEntity> STEAM_MIXER;
	public static final BlockEntityEntry<SteamSawBlockEntity> STEAM_SAW;
	public static final BlockEntityEntry<SteamBoilerBlockEntity> STEAM_BOILER;
	public static final BlockEntityEntry<SolarBoilerBlockEntity> SOLAR_BOILER;
	public static final BlockEntityEntry<SolidBurnerBlockEntity> SOLID_BURNER;
	public static final BlockEntityEntry<FluidBurnerBlockEntity> FLUID_BURNER;
	public static final BlockEntityEntry<SteamFlywheelBlockEntity> STEAM_FLYWHEEL;
	public static final BlockEntityEntry<SteamEngineBlockEntity> STEAM_ENGINE;

	static {
		BRONZE_BASIN = SlApis.REGISTRATE.blockEntity("bronze_basin", BronzeBasinBlockEntity::new)
				.validBlock(SlBlocks.BRONZE_BASIN)
				.renderer(() -> BronzeBasinRenderer::new)
				.register();

		ADVANCED_SPOUT = SlApis.REGISTRATE.blockEntity("advanced_spout", AdvancedSpoutBlockEntity::new)
				.validBlocks(SlBlocks.ADVANCED_SPOUT)
				.renderer(() -> AdvancedSpoutRenderer::new)
				.register();

		STEAM_HAMMER = SlApis.REGISTRATE.blockEntity("steam_hammer", SteamHammerBlockEntity::new)
				.visual(() -> SteamHammerInstance::new)
				.renderer(() -> SteamHammerRenderer::new)
				.validBlock(SlBlocks.STEAM_HAMMER)
				.register();

		STEAM_MIXER = SlApis.REGISTRATE.blockEntity("steam_mixer", SteamMixerBlockEntity::new)
				.visual(() -> SteamMixerInstance::new)
				.renderer(() -> SteamMixerRenderer::new)
				.validBlock(SlBlocks.STEAM_MIXER)
				.register();

		STEAM_SAW = SlApis.REGISTRATE.blockEntity("steam_saw", SteamSawBlockEntity::new)
				.visual(() -> SawVisual::new)
				.renderer(() -> SawRenderer::new)
				.validBlock(SlBlocks.STEAM_SAW)
				.register();

		STEAM_BOILER = SlApis.REGISTRATE.blockEntity("steam_boiler", SteamBoilerBlockEntity::new)
				.validBlock(SlBlocks.BRONZE_BOILER)
				.register();

		SOLAR_BOILER = SlApis.REGISTRATE.blockEntity("solar_boiler", SolarBoilerBlockEntity::new)
				.renderer(() -> SolarBoilerRenderer::new)
				.validBlock(SlBlocks.BRONZE_SOLAR_BOILER)
				.register();

		SOLID_BURNER = SlApis.REGISTRATE.blockEntity("solid_burner", SolidBurnerBlockEntity::new)
				.validBlock(SlBlocks.BRONZE_SOLID_BURNER)
				.register();

		FLUID_BURNER = SlApis.REGISTRATE.blockEntity("fluid_burner", FluidBurnerBlockEntity::new)
				.validBlock(SlBlocks.BRONZE_FLUID_BURNER)
				.register();

		STEAM_FLYWHEEL = SlApis.REGISTRATE.blockEntity("steam_flywheel", SteamFlywheelBlockEntity::new)
				.visual(() -> SteamFlywheelInstance::new)
				.renderer(() -> SteamFlywheelRenderer::new)
				.validBlock(SlBlocks.BRONZE_STEAM_FLYWHEEL)
				.register();

		STEAM_ENGINE = SlApis.REGISTRATE.blockEntity("steam_engine", SteamEngineBlockEntity::new)
				.validBlock(SlBlocks.BRONZE_STEAM_ENGINE)
				.register();
	}

	public static void init() {
	}
}
