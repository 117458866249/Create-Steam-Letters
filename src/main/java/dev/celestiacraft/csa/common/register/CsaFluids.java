package dev.celestiacraft.csa.common.register;

import com.tterrag.registrate.util.entry.FluidEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.common.fluid.SteamFluid;
import dev.celestiacraft.csa.tags.CsaFluidTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.fluids.ForgeFlowingFluid;

public class CsaFluids {
	public static final FluidEntry<ForgeFlowingFluid.Flowing> STEAM;
	public static final FluidEntry<ForgeFlowingFluid.Flowing> SUPERHEATED_STEAM;

	static {
		STEAM = CsaApis.REGISTRATE.fluid(
						"steam",
						CsaApis.loadResource("fluid/steam_still"),
						CsaApis.loadResource("fluid/steam_flow"),
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
				.tag(CsaFluidTags.STEAM)
				.source(ForgeFlowingFluid.Source::new)
				.bucket()
				.tag(ItemTags.create(ResourceLocation.parse("forge:buckets/steam")))
				.tag(ItemTags.create(ResourceLocation.parse("forge:buckets")))
				.model(NonNullBiConsumer.noop())
				.build()
				.lang("Steam")
				.register();

		SUPERHEATED_STEAM = CsaApis.REGISTRATE.fluid(
						"superheated_steam",
						CsaApis.loadResource("fluid/superheated_steam_still"),
						CsaApis.loadResource("fluid/superheated_steam_flow"),
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
				.tag(CsaFluidTags.SUPERHEATED_STEAM)
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