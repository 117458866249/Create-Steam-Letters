package dev.celestiacraft.steam_letters.api;

import com.jesz.createdieselgenerators.CreateDieselGenerators;
import com.simibubi.create.Create;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class SlApis {
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateLettersSteam.MODID)
			.setTooltipModifierFactory((item) -> {
				return new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
						.andThen(TooltipModifier.mapNull(KineticStats.create(item)));
			});

	public static ResourceLocation loadResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(CreateLettersSteam.MODID, path);
	}

	public static ResourceLocation loadCreate(String path) {
		return Create.asResource(path);
	}

	public static ResourceLocation loadCDG(String path) {
		return CreateDieselGenerators.rl(path);
	}

	public static ResourceLocation laodForge(String path) {
		return ResourceLocation.fromNamespaceAndPath("forge", path);
	}

	public static void require(ProcessingRecipeBuilder<?> builder, TagKey<Item> tag, int count) {
		for (int i = 0; i < count; i++) {
			builder.require(tag);
		}
	}
}