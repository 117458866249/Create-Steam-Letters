package dev.celestiacraft.csa.common.register;

import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.api.CsaTranKeys;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class CsaCreativeTabs {
	public static final DeferredRegister<CreativeModeTab> TABS;

	public static final RegistryObject<CreativeModeTab>
			BASIC;

	static {
		TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), CreateSteamAges.MODID);

		BASIC = TABS.register("basic", () -> {
			return CreativeModeTab.builder()
					.icon(CsaBlocks.STEAM_HAMMER::asStack)
					.title(Component.translatable(CsaTranKeys.BASIC_TAB))
					.displayItems((parameters, output) -> {
						output.accept(CsaFluids.STEAM.getBucket().get());
						output.accept(CsaFluids.SUPERHEATED_STEAM.getBucket().get());
						output.accept(CsaBlocks.STEAM_HAMMER.asItem());
					})
					.build();
		});
	}

	public static void init(IEventBus bus) {
		TABS.register(bus);
	}
}