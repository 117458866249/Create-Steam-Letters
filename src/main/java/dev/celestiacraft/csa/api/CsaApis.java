package dev.celestiacraft.csa.api;

import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.celestiacraft.csa.CreateSteamAges;
import lombok.experimental.UtilityClass;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

@UtilityClass
public class CsaApis {
	public final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateSteamAges.MODID)
			.setTooltipModifierFactory((item) -> {
				return new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
						.andThen(TooltipModifier.mapNull(KineticStats.create(item)));
			});

	public ResourceLocation loadResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(CreateSteamAges.MODID, path);
	}

	public ResourceLocation laodForge(String path) {
		return ResourceLocation.fromNamespaceAndPath("forge", path);
	}

	public void require(ProcessingRecipeBuilder<?> builder, TagKey<Item> tag, int count) {
		for (int i = 0; i < count; i++) {
			builder.require(tag);
		}
	}
}