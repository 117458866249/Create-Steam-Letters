package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.api.item.SteamBlockItem;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

/**
 * 蒸汽方块的注册骨架: 方块 / 方块实体类型 / 方块物品 / 资源与语言.
 * <p>
 * 方块实体类型与方块物品不在方块那张注册表里, 只能等各自的注册事件打开时才能建,
 * 所以这里把两头拆开: 方块本身由 createObject 建, 另外两样挂到 createAdditionalObjects.
 */
public abstract class DeviceBuilder<B extends Block, E extends BlockEntity> extends BuilderBase<Block> {
	protected B block;
	protected Item item;
	protected BlockEntityType<E> blockEntityType;

	protected ResourceLocation material = SlApis.loadResource("bronze");
	protected ResourceLocation model;
	protected double strength;
	protected double speed;
	protected double consumption;
	protected int storage;

	protected DeviceBuilder(ResourceLocation id) {
		super(id);
		tag(BlockTags.MINEABLE_WITH_PICKAXE.location());
		tag(BlockTags.NEEDS_STONE_TOOL.location());
		tag(AllTags.AllBlockTags.WRENCH_PICKUP.tag.location());
	}

	@Override
	public RegistryInfo getRegistryType() {
		return RegistryInfo.BLOCK;
	}

	@Override
	public Block createObject() {
		block = createBlock(createProperties());
		return block;
	}

	@Override
	public void createAdditionalObjects() {
		RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(new DeviceBlockEntityTypeBuilder(id, this::buildBlockEntityType));
		RegistryInfo.ITEM.addBuilder(new DeviceItemBuilder(id, this::buildItem));
	}

	/**
	 * 建方块实体类型, 只由 DeviceBlockEntityTypeBuilder 在方块实体类型注册阶段调用.
	 */
	protected BlockEntityType<E> buildBlockEntityType() {
		BlockEntityType<E> created = BlockEntityType.Builder.of(this::createBlockEntity, block).build(null);
		blockEntityType = created;
		return created;
	}

	/**
	 * 建方块物品, 只由 DeviceItemBuilder 在物品注册阶段调用.
	 */
	protected Item buildItem() {
		item = new SteamBlockItem(block, new Item.Properties());
		return item;
	}

	@Override
	public void generateAssetJsons(AssetJsonGenerator generator) {
		generateBlockModels(generator);
		generateBlockState(generator);
		generateItemModel(generator);
	}

	@Override
	public void generateLang(LangEventJS event) {
		event.add(id.toLanguageKey("block"), displayName(id.getPath()));
	}

	protected abstract String devicePath();

	protected abstract B createBlock(BlockBehaviour.Properties properties);

	protected abstract E createBlockEntity(BlockPos pos, BlockState state);

	protected abstract void generateBlockState(AssetJsonGenerator generator);

	protected String itemModel() {
		return baseModel() + "_item";
	}

	protected void generateItemModel(AssetJsonGenerator generator) {
		JsonObject root = new JsonObject();
		root.addProperty("parent", modelId(itemModel()));
		generator.json(itemModelLocation(), root);
	}

	/**
	 * 材质就是模组自带的 bronze 时, 模型直接复用内置资源, 不再重复生成.
	 */
	protected boolean usesBuiltinModel() {
		return CreateLettersSteam.MODID.equals(material.getNamespace()) && "bronze".equals(material.getPath());
	}

	/**
	 * 按材质自动生成方块模型: 直接内联同名 bronze 模型的造型, 只把贴图换成材质对应的图片.
	 * <p>
	 * 生成物和锅炉一类的设备一样, 是完整的自包含模型, 由 KubeJS 写进虚拟资源包,
	 * 不 parent 到模组内置资源, 所以换材质时不再依赖内置的 bronze 模型文件.
	 * <p>
	 * 贴图路径按 材质命名空间 + 固定部件后缀 推导, 所以脚本只需要写材质名:
	 * {@code material("steam_letters:steel")} 的飞轮轮盘是 {@code steam_letters:block/steam/flywheel/steel_wheel},
	 * 外壳是 {@code steam_letters:block/steam/casing/steel_casing}, 引擎气缸是
	 * {@code steam_letters:block/steam/engine/steel_cylinder}.
	 *
	 * @param path            生成的模型路径(不含命名空间与 models/ 前缀)
	 * @param template        作为造型来源的 bronze 模型路径
	 * @param texturePatterns 贴图键到路径模板的映射, 模板里的 %s 会被替换成材质名
	 */
	protected void generateModel(AssetJsonGenerator generator, String path, String template, Map<String, String> texturePatterns) {
		JsonObject root = loadTemplateModel(template);
		JsonObject textures = new JsonObject();
		texturePatterns.forEach((key, pattern) -> textures.addProperty(key, modelNamespace() + ":" + pattern.replace("%s", material.getPath())));
		root.add("textures", textures);

		generator.json(ResourceLocation.fromNamespaceAndPath(modelNamespace(), "models/" + path), root);
	}

	/**
	 * 读模组内置的模型 json 作为造型骨架, 每次返回一份新的对象, 调用方可以随意改.
	 */
	private static JsonObject loadTemplateModel(String template) {
		String path = "/assets/" + CreateLettersSteam.MODID + "/models/" + template + ".json";

		try (InputStream stream = openTemplateModel(path, template)) {
			JsonElement element = JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
			JsonObject root = element.getAsJsonObject();
			root.remove("credit");
			return root;
		} catch (IOException e) {
			throw new IllegalStateException("Failed to read model template " + path, e);
		}
	}

	/**
	 * 先在类路径里找, 找不到再问模组文件, 保证开发环境与打包后都能读到骨架模型.
	 */
	private static InputStream openTemplateModel(String path, String template) throws IOException {
		InputStream stream = DeviceBuilder.class.getResourceAsStream(path);

		if (stream != null) {
			return stream;
		}

		IModFileInfo modFile = ModList.get().getModFileById(CreateLettersSteam.MODID);

		if (modFile != null) {
			Path file = modFile.getFile().findResource("assets", CreateLettersSteam.MODID, "models", template + ".json");

			if (file != null) {
				return Files.newInputStream(file);
			}
		}

		throw new IllegalStateException("Missing model template " + path);
	}

	/**
	 * 生成方块本体与物品模型, 只在没有显式指定 model 且材质不是 bronze 时才有活儿.
	 */
	protected abstract void generateBlockModels(AssetJsonGenerator generator);

	protected BlockBehaviour.Properties createProperties() {
		return BlockBehaviour.Properties.copy(SharedProperties.softMetal())
				.noOcclusion()
				.sound(SoundType.LANTERN);
	}

	protected String modelNamespace() {
		ResourceLocation source = model != null ? model : material;
		return source.getNamespace();
	}

	protected String baseModel() {
		return model != null ? model.getPath() : devicePath() + "/" + material.getPath();
	}

	protected String modelId(String path) {
		return modelNamespace() + ":" + path;
	}

	protected void writeBlockState(AssetJsonGenerator generator, JsonObject variants) {
		JsonObject root = new JsonObject();
		root.add("variants", variants);
		generator.json(ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "blockstates/" + id.getPath()), root);
	}

	protected ResourceLocation itemModelLocation() {
		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "models/item/" + id.getPath());
	}

	protected String displayName(String path) {
		StringBuilder builder = new StringBuilder();
		for (String part : path.split("_")) {
			if (part.isEmpty()) {
				continue;
			}
			if (!builder.isEmpty()) {
				builder.append(' ');
			}
			builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
		}
		return builder.toString();
	}
}
