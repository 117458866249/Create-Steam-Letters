package dev.celestiacraft.csa.event.item;

import com.simibubi.create.foundation.item.TooltipHelper;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.api.CsaTranKeys;
import dev.celestiacraft.csa.api.lang.CsaLang;
import dev.celestiacraft.csa.common.register.CsaBlocks;
import dev.celestiacraft.csa.config.common.SteamHammerConfig;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CreateSteamAges.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemTooltip {
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		List<Component> tooltip = event.getToolTip();
		ItemStack stack = event.getItemStack();

		if (stack.is(CsaBlocks.STEAM_HAMMER.asItem())) {
			addSteamHammerTooltip(tooltip);
		}
	}

	private static void addSteamHammerTooltip(List<Component> tooltip) {
		int steamCost = SteamHammerConfig.STEAM_CONSUMPTION.get();

		CsaLang.isShiftDown(tooltip);

		if (CsaLang.isShiftDown()) {
			tooltip.add(Component.empty());

			tooltip.addAll(TooltipHelper.cutStringTextComponent(
					CsaLang.translateDirect(CsaTranKeys.TOOLTIP_STEAM_HAMMER_SUMMARY).getString(),
					FontHelper.Palette.STANDARD_CREATE
			));

			tooltip.add(Component.empty());

			CsaLang.translate(CsaTranKeys.TOOLTIP_STEAM_HAMMER_CONDITION)
					.style(ChatFormatting.GRAY)
					.addTo(tooltip);

			tooltip.addAll(TooltipHelper.cutStringTextComponent(
					CsaLang.translateDirect(CsaTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR1, steamCost).getString(),
					FontHelper.Palette.STANDARD_CREATE.primary(),
					FontHelper.Palette.STANDARD_CREATE.highlight(),
					1
			));

			tooltip.addAll(TooltipHelper.cutStringTextComponent(
					CsaLang.translateDirect(CsaTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR2, steamCost).getString(),
					FontHelper.Palette.STANDARD_CREATE.primary(),
					FontHelper.Palette.STANDARD_CREATE.highlight(),
					1
			));
		}
	}
}