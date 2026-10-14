package dev.celestiacraft.create_steam_ages.common.register;

import com.simibubi.create.AllItems;
import dev.celestiacraft.create_steam_ages.CreateSteamAges;
import dev.celestiacraft.create_steam_ages.api.CSATranKeys;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CSACreativeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS;

	public static final RegistryObject<CreativeModeTab>
			BASIC;

	static {
		TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), CreateSteamAges.MODID);

		BASIC = TABS.register("basic", () -> {
			return CreativeModeTab.builder()
					.icon(AllItems.PRECISION_MECHANISM::asStack)
					.title(Component.translatable(CSATranKeys.BASIC_TAB))
					.displayItems((parameters, output) -> {
						output.accept(CSAFluids.STEAM.getBucket().get());
						output.accept(CSAFluids.HIGH_TEMPERATURE_STEAM.getBucket().get());
						output.accept(CSABlocks.STEAM_HAMMER.asItem());
					})
					.build();
		});
	}

	public static void init(IEventBus bus) {
		TABS.register(bus);
	}
}