package dev.celestiacraft.steam_letters.event.item;

import com.simibubi.create.foundation.item.TooltipHelper;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.client.lang.SlLang;
import dev.celestiacraft.steam_letters.common.item.SteamContainerItem;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.config.common.SteamHammerConfig;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CreateLettersSteam.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ItemTooltip {
	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event) {
		List<Component> tooltip = event.getToolTip();
		ItemStack stack = event.getItemStack();

		if (stack.is(SlBlocks.STEAM_HAMMER.asItem())) {
			addSteamHammerTooltip(tooltip);
		}
		if (stack.is(SlBlocks.BRONZE_STEAM_PUMP.asItem())) {
			addSteamPumpTooltip(tooltip);
		}
		if (stack.is(SlBlocks.BRONZE_BASIN.asItem())) {
			addBronzeBasinTooltip(tooltip);
		}
		if (stack.is(SlItemTags.STEAM_CONTAINER)) {
			addSteamContainerTooltip(tooltip, stack);
		}
	}

	private static void addSteamContainerTooltip(List<Component> tooltip, ItemStack stack) {
		if (!(stack.getItem() instanceof SteamContainerItem item)) {
			return;
		}

		boolean emptied = item.isEmptied();
		FluidStack stored = SteamContainerItem.readFluid(stack);

		// 状态行始终可见: 空容器报容量, 密封容器报当前存量.
		Component status = emptied || stored.isEmpty()
				? SlLang.translateDirect(SlTranKeys.TOOLTIP_SEALED_CONTAINER_EMPTY, SteamContainerItem.CAPACITY)
				: SlLang.translateDirect(
						SlTranKeys.TOOLTIP_SEALED_CONTAINER_FLUID,
						stored.getDisplayName(),
						stored.getAmount(),
						SteamContainerItem.CAPACITY
				);

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				status.getString(),
				FontHelper.Palette.STANDARD_CREATE.primary(),
				FontHelper.Palette.STANDARD_CREATE.highlight(),
				0
		));

		SlLang.isShiftDown(tooltip);

		if (!SlLang.isShiftDown()) {
			return;
		}

		tooltip.add(Component.empty());

		if (emptied) {
			addContainerBehaviour(tooltip, SlTranKeys.TOOLTIP_STEAM_CONTAINER_EXTRACT);
			return;
		}

		addContainerBehaviour(tooltip, SlTranKeys.TOOLTIP_STEAM_CONTAINER_INSERT);
		addContainerBehaviour(tooltip, SlTranKeys.TOOLTIP_STEAM_CONTAINER_EXTRACT_MORE);
	}

	private static void addContainerBehaviour(List<Component> tooltip, String langKey) {
		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(langKey).getString(),
				FontHelper.Palette.STANDARD_CREATE.primary(),
				FontHelper.Palette.STANDARD_CREATE.highlight(),
				1
		));
	}

	private static void addSteamPumpTooltip(List<Component> tooltip) {
		SlLang.isShiftDown(tooltip);

		if (!SlLang.isShiftDown()) {
			return;
		}

		tooltip.add(Component.empty());

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_PUMP_SUMMARY).getString(),
				FontHelper.Palette.STANDARD_CREATE
		));

		tooltip.add(Component.empty());

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_PUMP_BEHAVIOUR1).getString(),
				FontHelper.Palette.STANDARD_CREATE.primary(),
				FontHelper.Palette.STANDARD_CREATE.highlight(),
				1
		));

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_PUMP_BEHAVIOUR2).getString(),
				FontHelper.Palette.STANDARD_CREATE.primary(),
				FontHelper.Palette.STANDARD_CREATE.highlight(),
				1
		));
	}

	private static void addBronzeBasinTooltip(List<Component> tooltip) {
		SlLang.isShiftDown(tooltip);

		if (!SlLang.isShiftDown()) {
			return;
		}

		tooltip.add(Component.empty());

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_BRONZE_BASIN_SUMMARY).getString(),
				FontHelper.Palette.STANDARD_CREATE
		));

		tooltip.add(Component.empty());

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_BRONZE_BASIN_BEHAVIOUR1).getString(),
				FontHelper.Palette.STANDARD_CREATE.primary(),
				FontHelper.Palette.STANDARD_CREATE.highlight(),
				1
		));

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_BRONZE_BASIN_BEHAVIOUR2).getString(),
				FontHelper.Palette.STANDARD_CREATE.primary(),
				FontHelper.Palette.STANDARD_CREATE.highlight(),
				1
		));
	}

	private static void addSteamPipeTooltip(List<Component> tooltip) {
		SlLang.isShiftDown(tooltip);

		if (!SlLang.isShiftDown()) {
			return;
		}

		tooltip.add(Component.empty());

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_PIPE_SUMMARY).getString(),
				FontHelper.Palette.STANDARD_CREATE
		));

		tooltip.add(Component.empty());

		tooltip.addAll(TooltipHelper.cutStringTextComponent(
				SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_PIPE_BEHAVIOUR).getString(),
				FontHelper.Palette.STANDARD_CREATE.primary(),
				FontHelper.Palette.STANDARD_CREATE.highlight(),
				1
		));
	}

	private static void addSteamHammerTooltip(List<Component> tooltip) {
		int steamCost = SteamHammerConfig.STEAM_CONSUMPTION.get();

		SlLang.isShiftDown(tooltip);

		if (SlLang.isShiftDown()) {
			tooltip.add(Component.empty());

			tooltip.addAll(TooltipHelper.cutStringTextComponent(
					SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_HAMMER_SUMMARY).getString(),
					FontHelper.Palette.STANDARD_CREATE
			));

			tooltip.add(Component.empty());

			SlLang.translate(SlTranKeys.TOOLTIP_STEAM_HAMMER_CONDITION)
					.style(ChatFormatting.GRAY)
					.addTo(tooltip);

			tooltip.addAll(TooltipHelper.cutStringTextComponent(
					SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR1, steamCost).getString(),
					FontHelper.Palette.STANDARD_CREATE.primary(),
					FontHelper.Palette.STANDARD_CREATE.highlight(),
					1
			));

			tooltip.addAll(TooltipHelper.cutStringTextComponent(
					SlLang.translateDirect(SlTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR2, steamCost).getString(),
					FontHelper.Palette.STANDARD_CREATE.primary(),
					FontHelper.Palette.STANDARD_CREATE.highlight(),
					1
			));
		}
	}
}