package dev.celestiacraft.csa.api;

import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import dev.celestiacraft.csa.CreateSteamAges;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.resources.ResourceLocation;

public class CsaApis {
	public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateSteamAges.MODID)
			.setTooltipModifierFactory((item) -> {
				return new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
						.andThen(TooltipModifier.mapNull(KineticStats.create(item)));
			});

	public static ResourceLocation loadResource(String path) {
		return ResourceLocation.fromNamespaceAndPath(CreateSteamAges.MODID, path);
	}
}