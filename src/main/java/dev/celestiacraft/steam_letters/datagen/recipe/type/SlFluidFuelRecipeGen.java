package dev.celestiacraft.steam_letters.datagen.recipe.type;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.common.recipe.builder.FluidFuelRecipeGen;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.FluidTags;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.fluids.TinkerFluids;

public class SlFluidFuelRecipeGen extends FluidFuelRecipeGen {
	public SlFluidFuelRecipeGen(PackOutput output) {
		super(output, CreateLettersSteam.MODID);

		recipe("lava", (builder) -> {
			return builder.fluid(FluidTags.LAVA)
					.time(20 * 30);
		});

		recipe("blazing_blood", (builder) -> {
			return builder.fluid(TinkerFluids.blazingBlood.get())
					.time(20 * (30 * 2))
					.superheated()
					.whenModLoaded(TConstruct.MOD_ID);
		});
	}
}