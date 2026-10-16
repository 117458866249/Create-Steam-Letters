package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.google.gson.JsonObject;
import com.simibubi.create.AllTags;
import com.simibubi.create.foundation.data.SharedProperties;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.api.item.SteamBlockItem;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
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

import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

public abstract class SteamDeviceBuilder<B extends Block, E extends BlockEntity> extends BuilderBase<Block> {
	protected static final String MACHINE_BOTTOM = "steam_letters:block/steam/down";
	protected final String path;
	protected Integer capacity;
	protected Double production;
	protected Double maxHeat;
	protected ResourceLocation material = SlApis.loadResource("bronze");
	protected ResourceLocation model;
	protected B block;
	protected Item item;
	protected BlockEntityType<E> blockEntityType;

	protected SteamDeviceBuilder(ResourceLocation id) {
		super(id);
		path = id.getPath();
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
		B created = createBlock(createProperties());
		block = created;
		return created;
	}

	@Override
	public void createAdditionalObjects() {
		RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(new DeviceBlockEntityTypeBuilder(id));
		RegistryInfo.ITEM.addBuilder(new DeviceItemBuilder(id));
	}

	private class DeviceBlockEntityTypeBuilder extends BuilderBase<BlockEntityType<?>> {
		private DeviceBlockEntityTypeBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		public RegistryInfo getRegistryType() {
			return RegistryInfo.BLOCK_ENTITY_TYPE;
		}

		@Override
		public BlockEntityType<?> createObject() {
			BlockEntityType<E> created = BlockEntityType.Builder.of(SteamDeviceBuilder.this::createBlockEntity, block).build(null);
			blockEntityType = created;
			return created;
		}
	}

	private class DeviceItemBuilder extends BuilderBase<Item> {
		private DeviceItemBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		public RegistryInfo getRegistryType() {
			return RegistryInfo.ITEM;
		}

		@Override
		public Item createObject() {
			item = createBlockItem(block);
			return item;
		}
	}

	protected BlockBehaviour.Properties createProperties() {
		return BlockBehaviour.Properties.copy(SharedProperties.softMetal())
				.noOcclusion()
				.sound(SoundType.LANTERN);
	}

	protected Item createBlockItem(B block) {
		return new SteamBlockItem(block, new Item.Properties());
	}

	protected abstract B createBlock(BlockBehaviour.Properties properties);

	protected abstract E createBlockEntity(BlockPos pos, BlockState state);

	protected abstract String devicePath();

	@Info("材质, 默认 steam_letters:bronze. 用命名空间区分来自哪个模组的贴图, 例如 material('steam_letters:steel').")
	public SteamDeviceBuilder<B, E> material(ResourceLocation material) {
		this.material = material;
		return this;
	}

	@Info("完整模型路径覆盖, 例如 steam_letters:block/steam/boiler/bronze. 设置后忽略 material.")
	public SteamDeviceBuilder<B, E> model(ResourceLocation model) {
		this.model = model;
		return this;
	}

	protected String baseModel() {
		if (model != null) {
			return model.getPath();
		}
		return devicePath() + "/" + material.getPath();
	}

	/**
	 * 模型路径所属的命名空间: material 与 model 各自决定, 没给 model 时跟随 material
	 */
	protected String modelNamespace() {
		return model != null ? model.getNamespace() : material.getNamespace();
	}

	/**
	 * 模型的完整 ID, 例如 {@code steam_letters:block/steam/boiler/steel}
	 */
	protected String modelId(String modelPath) {
		return modelNamespace() + ":" + modelPath;
	}

	/**
	 * 材质目录下的部件贴图 ID, 例如 {@code steam_letters:block/steam/boiler/steel/side}
	 */
	protected String textureId(String modelPath, String part) {
		return modelNamespace() + ":" + modelPath + "/" + part;
	}

	@Override
	public void generateAssetJsons(AssetJsonGenerator generator) {
		generateBlockState(generator);
		generateModels(generator);
		generateItemModel(generator);
	}

	protected void generateBlockState(AssetJsonGenerator generator) {
		JsonObject variants = new JsonObject();
		JsonObject variant = new JsonObject();
		variant.addProperty("model", modelId(baseModel()));
		variants.add("", variant);

		JsonObject root = new JsonObject();
		root.add("variants", variants);
		generator.json(stateLocation(), root);
	}

	protected void generateModels(AssetJsonGenerator generator) {
		generator.json(modelLocation(baseModel()), createCubeBottomTopModel(baseModel(), MACHINE_BOTTOM));
	}

	protected void generateItemModel(AssetJsonGenerator generator) {
		JsonObject root = new JsonObject();
		root.addProperty("parent", modelId(baseModel()));
		generator.json(itemModelLocation(), root);
	}

	protected JsonObject createCubeBottomTopModel(String modelPath, String bottom) {
		JsonObject textures = new JsonObject();
		textures.addProperty("particle", textureId(modelPath, "side"));
		textures.addProperty("bottom", bottom);
		textures.addProperty("top", textureId(modelPath, "top"));
		textures.addProperty("side", textureId(modelPath, "side"));

		JsonObject root = new JsonObject();
		root.addProperty("parent", "minecraft:block/cube_bottom_top");
		root.add("textures", textures);
		return root;
	}

	protected ResourceLocation stateLocation() {
		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "blockstates/" + id.getPath());
	}

	protected ResourceLocation modelLocation(String modelPath) {
		return ResourceLocation.fromNamespaceAndPath(modelNamespace(), "models/" + modelPath);
	}

	protected ResourceLocation itemModelLocation() {
		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), "models/item/" + id.getPath());
	}

	@Override
	public void generateLang(LangEventJS event) {
		event.add(id.toLanguageKey("block"), displayName());
	}

	protected IntSupplier capacityOr(IntSupplier fallback) {
		return capacity == null ? fallback : capacity::intValue;
	}

	protected DoubleSupplier productionOr(DoubleSupplier fallback) {
		return production == null ? fallback : production::doubleValue;
	}

	protected DoubleSupplier maxHeatOr(DoubleSupplier fallback) {
		return maxHeat == null ? fallback : maxHeat::doubleValue;
	}

	protected String displayName() {
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
		return builder.isEmpty() ? path : builder.toString();
	}
}