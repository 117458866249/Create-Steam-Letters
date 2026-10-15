package dev.celestiacraft.steam_letters.common.register;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
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
						output.accept(SlMaterials.BRONZE_INGOT.asItem());
						output.accept(SlMaterials.BRONZE_PLATE.asItem());
						output.accept(SlItems.STEAM_MECHANISM.asItem());
						output.accept(SlItems.INCOMPLETE_STEAM_MECHANISM.asItem());
						output.accept(SlBlocks.BRONZE_BASIN.asItem());
						output.accept(SlBlocks.ADVANCED_SPOUT.asItem());
						output.accept(SlBlocks.STEAM_HAMMER.asItem());
						output.accept(SlBlocks.STEAM_MIXER.asItem());
						output.accept(SlBlocks.STEAM_SAW.asItem());
						output.accept(SlBlocks.BRONZE_CASING.asItem());
						output.accept(SlFluids.STEAM.getBucket().get());
						output.accept(SlFluids.SUPERHEATED_STEAM.getBucket().get());
					})
					.build();
		});
	}

	public static void init(IEventBus bus) {
		TABS.register(bus);
	}
}