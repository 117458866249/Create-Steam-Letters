package dev.celestiacraft.steam_letters.common.register;

import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.common.fluid.SteamFluid;
import dev.celestiacraft.steam_letters.tags.SlFluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class SlFluids {
	public static final FluidEntry<ForgeFlowingFluid.Flowing> STEAM;
	public static final FluidEntry<ForgeFlowingFluid.Flowing> SUPERHEATED_STEAM;

	static {
		STEAM = SlApis.REGISTRATE.fluid(
						"steam",
						SlApis.loadResource("fluid/steam_still"),
						SlApis.loadResource("fluid/steam_flow"),
						SteamFluid.create(0xFAFAFA, () -> {
							return 1.0f / 8.0f;
						}, 0xFFFAFAFA)
				)
				.properties((properties) -> {
					properties.density(-10)
							.viscosity(1)
							.temperature(473)
							.canPushEntity(false)
							.canConvertToSource(false)
							.canDrown(true);
				})
				.fluidProperties((properties) -> {
					properties.slopeFindDistance(3)
							.explosionResistance(100.0F);
				})
				.tag(SlFluidTags.STEAM)
				.source(ForgeFlowingFluid.Source::new)
				.bucket()
				.tag(ItemTags.create(ResourceLocation.parse("forge:buckets/steam")))
				.tag(ItemTags.create(ResourceLocation.parse("forge:buckets")))
				.model(NonNullBiConsumer.noop())
				.build()
				.lang("Steam")
				.register();

		SUPERHEATED_STEAM = SlApis.REGISTRATE.fluid(
						"superheated_steam",
						SlApis.loadResource("fluid/superheated_steam_still"),
						SlApis.loadResource("fluid/superheated_steam_flow"),
						SteamFluid.create(0xFAFAFA, () -> {
							return 1.0f / 8.0f;
						}, 0xFFFAFAFA)
				)
				.properties((properties) -> {
					properties.density(-10)
							.viscosity(1)
							.temperature(473)
							.canPushEntity(false)
							.canConvertToSource(false)
							.canDrown(true);
				})
				.fluidProperties((properties) -> {
					properties.slopeFindDistance(3)
							.explosionResistance(100.0F);
				})
				.tag(SlFluidTags.SUPERHEATED_STEAM)
				.source(ForgeFlowingFluid.Source::new)
				.bucket()
				.model(NonNullBiConsumer.noop())
				.tag(ItemTags.create(ResourceLocation.parse("forge:buckets/superheated_steam")))
				.tag(ItemTags.create(ResourceLocation.parse("forge:buckets")))
				.build()
				.lang("Superheated Steam")
				.register();
	}

	public static void init() {
	}
}