package dev.celestiacraft.steam_letters.datagen;

import com.tterrag.registrate.providers.ProviderType;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.datagen.language.English;
import dev.celestiacraft.steam_letters.datagen.recipe.SlRecipeProvider;
import dev.celestiacraft.steam_letters.datagen.recipe.type.create.*;
import dev.celestiacraft.steam_letters.datagen.tags.ItemTagsGen;
import dev.latvian.mods.kubejs.KubeJS;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CreateLettersSteam.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void onDatagen(GatherDataEvent event) {
		stopKubeJSBackgroundThread();

		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		boolean client = event.includeClient();
		boolean server = event.includeServer();

		if (client) {
			SlApis.REGISTRATE.addDataGenerator(ProviderType.LANG, English::addTranslations);
		}

		if (server) {
			generator.addProvider(server, new SlRecipeProvider(output));
			generator.addProvider(server, new SlMixingRecipeGen(output));
			generator.addProvider(server, new SlPressRecipeGen(output));
			generator.addProvider(server, new SlItemApplicationRecipeGen(output));
			generator.addProvider(server, new SlSequencedAssemblyRecipeGen(output));
			generator.addProvider(server, new SlCrushingRecipeGen(output));

			SlApis.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, ItemTagsGen::addItemTags);
		}
	}

	private static void stopKubeJSBackgroundThread() {
		if (!ModList.get().isLoaded(KubeJS.MOD_ID)) {
			return;
		}
		try {
			Class.forName("dev.latvian.mods.kubejs.util.KubeJSBackgroundThread")
					.getField("running")
					.setBoolean(null, false);
		} catch (Throwable ignored) {
		}
	}
}