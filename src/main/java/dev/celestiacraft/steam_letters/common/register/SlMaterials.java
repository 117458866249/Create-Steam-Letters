package dev.celestiacraft.steam_letters.common.register;

import com.simibubi.create.AllTags;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.client.models.ItemModelGen;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.Tags;

public class SlMaterials {
	public static final ItemEntry<Item>
			BRONZE_INGOT,
			BRONZE_PLATE;

	static {
		BRONZE_INGOT = SlApis.REGISTRATE.item("bronze_ingot", Item::new)
				.model(ItemModelGen.generated("item/material/bronze/ingot"))
				.tag(Tags.Items.INGOTS)
				.tag(SlItemTags.BRONZE_INGOT)
				.lang("Bronze Ingot")
				.register();

		BRONZE_PLATE = SlApis.REGISTRATE.item("bronze_plate", Item::new)
				.model(ItemModelGen.generated("item/material/bronze/plate"))
				.tag(AllTags.AllItemTags.PLATES.tag)
				.tag(SlItemTags.BRONZE_PLATES)
				.lang("Bronze Plate")
				.register();
	}

	public static void init() {
	}
}