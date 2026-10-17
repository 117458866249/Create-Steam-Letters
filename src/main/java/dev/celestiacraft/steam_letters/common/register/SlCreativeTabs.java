package dev.celestiacraft.steam_letters.common.register;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.common.item.SteamContainerItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SlCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS;

	public static final RegistryObject<CreativeModeTab>
			BASIC;

	static {
		TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), CreateLettersSteam.MODID);

		BASIC = TABS.register("basic", () -> {
			return CreativeModeTab.builder()
					.icon(SlBlocks.STEAM_HAMMER::asStack)
					.title(Component.translatable(SlTranKeys.BASIC_TAB))
					.displayItems((parameters, output) -> {
						ForgeRegistries.ITEMS.getEntries().forEach((entry) -> {
							ResourceKey<Item> key = entry.getKey();
							Item item = entry.getValue();

							if (!key.location().getNamespace().equals(CreateLettersSteam.MODID)) {
								return;
							}

							if (item instanceof SteamContainerItem container && !container.isEmptied()) {
								ItemStack steam = item.getDefaultInstance();
								SteamContainerItem.writeFluid(steam, new FluidStack(
										SlFluids.STEAM.get(),
										SteamContainerItem.CAPACITY
								));
								output.accept(steam);

								ItemStack superheatedSteam = item.getDefaultInstance();
								SteamContainerItem.writeFluid(superheatedSteam, new FluidStack(
										SlFluids.SUPERHEATED_STEAM.get(),
										SteamContainerItem.CAPACITY
								));
								output.accept(superheatedSteam);

								return;
							}

							output.accept(item);
						});
					})
					.build();
		});
	}

	public static void init(IEventBus bus) {
		TABS.register(bus);
	}
}