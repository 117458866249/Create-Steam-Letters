package dev.celestiacraft.steam_letters.datagen.tags;

import com.simibubi.create.AllItems;
import com.tterrag.registrate.providers.RegistrateItemTagsProvider;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

public class ItemTagsGen {
	public static void addItemTags(RegistrateItemTagsProvider provider) {
		provider.addTag(Tags.Items.INGOTS)
				.addTag(SlItemTags.ANDESITE_ALLOY_INGOT);

		provider.addTag(SlItemTags.ANDESITE_ALLOY_INGOT)
				.add(AllItems.ANDESITE_ALLOY.asItem());

		// 固体燃烧室
		provider.addTag(SlItemTags.STEAM_SOLID_BURNER)
				.add(SlBlocks.BRONZE_SOLID_BURNER.asItem());

		// 流体燃烧室
		provider.addTag(SlItemTags.STEAM_FLUID_BURNER)
				.add(SlBlocks.BRONZE_FLUID_BURNER.asItem());

		// 燃烧室
		provider.addTag(SlItemTags.STEAM_BURNERS)
				.addTag(SlItemTags.STEAM_SOLID_BURNER)
				.addTag(SlItemTags.STEAM_FLUID_BURNER);

		// 太阳能锅炉
		provider.addTag(SlItemTags.STEAM_SOLAR_BOILER)
				.add(SlBlocks.BRONZE_SOLAR_BOILER.asItem());

		// 锅炉
		provider.addTag(SlItemTags.STEAM_BOILER)
				.add(SlBlocks.BRONZE_BOILER.asItem());

		// 蒸汽生产设备
		provider.addTag(SlItemTags.STEAM_PRODUCTION)
				.addTag(SlItemTags.STEAM_BURNERS)
				.addTag(SlItemTags.STEAM_SOLAR_BOILER)
				.addTag(SlItemTags.STEAM_BOILER);

		// 蒸汽引擎
		provider.addTag(SlItemTags.STEAM_ENGINE)
				.add(SlBlocks.BRONZE_STEAM_ENGINE.asItem());

		// 蒸汽飞轮
		provider.addTag(SlItemTags.STEAM_FLYWHEEL)
				.add(SlBlocks.BRONZE_STEAM_FLYWHEEL.asItem());

		// 深板岩
		provider.addTag(SlItemTags.DEEPSLATE)
				.add(Items.DEEPSLATE)
				.add(Items.COBBLED_DEEPSLATE);
	}
}