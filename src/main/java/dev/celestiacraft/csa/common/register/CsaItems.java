package dev.celestiacraft.csa.common.register;

import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.client.models.ItemModelGen;
import dev.celestiacraft.csa.common.item.SteamMechanismItem;

public class CsaItems {
	public static final ItemEntry<SteamMechanismItem> STEAM_MECHANISM;
	public static final ItemEntry<SequencedAssemblyItem> INCOMPLETE_STEAM_MECHANISM;

	static {
		STEAM_MECHANISM = CsaApis.REGISTRATE.item("steam_mechanism", SteamMechanismItem::new)
				.model(ItemModelGen.generated("item/steam_mechanism"))
				.lang("Steam Mechanism")
				.register();

		INCOMPLETE_STEAM_MECHANISM = CsaApis.REGISTRATE.item("incomplete_steam_mechanism", SequencedAssemblyItem::new)
				.model(ItemModelGen.generated("item/incomplete_steam_mechanism"))
				.lang("Incomplete Steam Mechanism")
				.register();
	}

	public static void init() {
	}
}