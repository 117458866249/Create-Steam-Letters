package dev.celestiacraft.csa.datagen;

import com.tterrag.registrate.providers.ProviderType;
import dev.celestiacraft.csa.CreateSteamAges;
import dev.celestiacraft.csa.api.CsaApis;
import dev.celestiacraft.csa.datagen.language.English;
import dev.celestiacraft.csa.datagen.recipe.CsaRecipeProvider;
import dev.celestiacraft.csa.datagen.recipe.type.create.CsaItemApplicationRecipeGen;
import dev.celestiacraft.csa.datagen.recipe.type.create.CsaMixingRecipeGen;
import dev.celestiacraft.csa.datagen.recipe.type.create.CsaPressRecipeGen;
import dev.celestiacraft.csa.datagen.recipe.type.create.CsaSequencedAssemblyRecipeGen;
import dev.celestiacraft.csa.datagen.tags.ItemTagsGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CreateSteamAges.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
	@SubscribeEvent
	public static void onDatagen(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		boolean client = event.includeClient();
		boolean server = event.includeServer();

		if (client) {
			CsaApis.REGISTRATE.addDataGenerator(ProviderType.LANG, English::addTranslations);
		}

		if (server) {
			generator.addProvider(server, new CsaRecipeProvider(output));
			generator.addProvider(server, new CsaMixingRecipeGen(output));
			generator.addProvider(server, new CsaPressRecipeGen(output));
			generator.addProvider(server, new CsaItemApplicationRecipeGen(output));
			generator.addProvider(server, new CsaSequencedAssemblyRecipeGen(output));

			CsaApis.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, ItemTagsGen::addItemTags);
		}
	}
}