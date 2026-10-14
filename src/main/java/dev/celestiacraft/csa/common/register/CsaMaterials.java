package dev.celestiacraft.csa.common.register;

import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.client.models.ItemModelGen;
import dev.celestiacraft.csa.tags.CsaItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class CsaMaterials {
	public static final ItemEntry<Item>
			BRONZE_INGOT,
			BRONZE_PLATE;

	static {
		BRONZE_INGOT = CsaApis.REGISTRATE.item("bronze_ingot", Item::new)
				.model(ItemModelGen.generated("item/material/bronze/ingot"))
				.tag(Tags.Items.INGOTS)
				.tag(CsaItemTags.BRONZE_INGOT)
				.lang("Bronze Ingot")
				.register();

		BRONZE_PLATE = CsaApis.REGISTRATE.item("bronze_plate", Item::new)
				.model(ItemModelGen.generated("item/material/bronze/plate"))
				.tag(AllTags.AllItemTags.PLATES.tag)
				.tag(CsaItemTags.BRONZE_PLATES)
				.lang("Bronze Plate")
				.register();
	}

	public static void init() {
	}
}