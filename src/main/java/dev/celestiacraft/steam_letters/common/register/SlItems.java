package dev.celestiacraft.steam_letters.common.register;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.client.models.ItemModelGen;
import dev.celestiacraft.steam_letters.common.item.SteamMechanismItem;

public class SlItems {
	public static final ItemEntry<SteamMechanismItem> STEAM_MECHANISM;
	public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_STEAM_MECHANISM;

	static {
		STEAM_MECHANISM = SlApis.REGISTRATE.item("steam_mechanism", SteamMechanismItem::new)
				.model(ItemModelGen.generated("item/steam_mechanism"))
				.lang("Steam Mechanism")
				.register();

		INCOMPLETE_STEAM_MECHANISM = SlApis.REGISTRATE.item("incomplete_steam_mechanism", SequencedAssemblyItem::new)
				.model(ItemModelGen.generated("item/incomplete_steam_mechanism"))
				.lang("Incomplete Steam Mechanism")
				.register();
	}

	public static void init() {
	}
}