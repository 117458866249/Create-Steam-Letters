package dev.celestiacraft.csa.common.register;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.client.models.ItemModelGen;
import dev.celestiacraft.csa.common.item.SteamMechanismItem;
import net.minecraft.world.item.Item;

public class CsaItems {
	public static final ItemEntry<SteamMechanismItem> STEAM_MECHANISM;
	public static final ItemEntry<Item> INCOMPLETE_STEAM_MECHANISM;

	static {
		STEAM_MECHANISM = CsaApis.REGISTRATE.item("steam_mechanism", SteamMechanismItem::new)
				.model(ItemModelGen.generated("item/steam_mechanism"))
				.lang("Steam Mechanism")
				.register();

		INCOMPLETE_STEAM_MECHANISM = CsaApis.REGISTRATE.item("incomplete_steam_mechanism", Item::new)
				.model(ItemModelGen.generated("item/incomplete_steam_mechanism"))
				.lang("Incomplete Steam Mechanism")
				.register();
	}

	public static void init() {
	}
}