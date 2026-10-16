package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.google.gson.JsonObject;
import com.simibubi.create.foundation.data.SharedProperties;
import dev.celestiacraft.steam_letters.api.SlApis;
import dev.celestiacraft.steam_letters.api.item.SteamBlockItem;
import dev.celestiacraft.steam_letters.api.machine.SteamEngineDriveSpec;
import dev.celestiacraft.steam_letters.common.block.engine.SteamEngineBlock;
import dev.celestiacraft.steam_letters.common.block.engine.SteamEngineBlockEntity;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlock;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlockEntity;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 蒸汽飞轮 + 飞轮引擎的 KubeJS 注册器.
 * <p>
 * 文档中这两个设备共用同一条写法, 但落地时必须是两个方块, 因此这里用「容器 builder + 两个子 builder」
 * 的结构: 脚本里创建的 builder 自身被打上 dummy 标记 (KubeJS 会跳过它的注册), 真正的设备由构造器
 * 里追加注册的 {@code <id>_flywheel} 与 {@code <id>_engine} 两个子 builder 产出, 资源与语言也由它们各自生成.
 * <p>
 * 参数全部挂在引擎一侧: 飞轮本身不决定任何数值, 它只是把引擎给出的转速与应力送进 Create 的动力网络.
 */
public class SteamEngineDeviceBuilder extends BuilderBase<Block> {
	public static final String FLYWHEEL_SUFFIX = "_flywheel";
	public static final String ENGINE_SUFFIX = "_engine";

	protected ResourceLocation material = SlApis.loadResource("bronze");
	protected ResourceLocation model;
	protected Double strength;
	protected Double speed;
	protected Double consumption;
	protected Integer storage;

	private final DeviceBuilder<SteamFlywheelBlock, SteamFlywheelBlockEntity> flywheel;
	private final DeviceBuilder<SteamEngineBlock, SteamEngineBlockEntity> engine;

	public SteamEngineDeviceBuilder(ResourceLocation id) {
		super(id);

		dummyBuilder = true;

		flywheel = new FlywheelBuilder(derive(id, FLYWHEEL_SUFFIX));
		engine = new EngineBuilder(derive(id, ENGINE_SUFFIX));

		RegistryInfo.BLOCK.addBuilder(flywheel);
		RegistryInfo.BLOCK.addBuilder(engine);
	}

	private static ResourceLocation derive(ResourceLocation id, String suffix) {
		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), id.getPath() + suffix);
	}

	@Override
	public RegistryInfo getRegistryType() {
		return RegistryInfo.BLOCK;
	}

	/**
	 * 容器本身不注册方块, 这里只为了在被别处取用时也有一个合法对象.
	 */
	@Override
	public Block createObject() {
		return new SteamFlywheelBlock(BlockBehaviour.Properties.copy(SharedProperties.softMetal()));
	}

	@Info("材质, 决定默认模型与贴图目录, 默认 steam_letters:bronze")
	public SteamEngineDeviceBuilder material(ResourceLocation material) {
		this.material = material;
		return this;
	}

	@Info("完整模型路径, 不写则按设备目录 + 材质推导")
	public SteamEngineDeviceBuilder model(ResourceLocation model) {
		this.model = model;
		return this;
	}

	@Info("飞轮的应力上限 (SU), 默认 16384")
	public SteamEngineDeviceBuilder strength(double strength) {
		this.strength = strength;
		return this;
	}

	@Info("飞轮转速 (RPM), 默认 32")
	public SteamEngineDeviceBuilder speed(double speed) {
		this.speed = speed;
		return this;
	}

	@Info("满负荷蒸汽消耗 (mB/tick), 默认 12")
	public SteamEngineDeviceBuilder consumption(double consumption) {
		this.consumption = consumption;
		return this;
	}

	@Info("引擎自带蒸汽罐容量 (mB), 默认 32000")
	public SteamEngineDeviceBuilder storage(int storage) {
		this.storage = storage;
		return this;
	}

	private abstract class DeviceBuilder<B extends Block, E extends BlockEntity> extends BuilderBase<Block> {
		protected B block;
		protected Item item;
		protected BlockEntityType<E> blockEntityType;

		protected DeviceBuilder(ResourceLocation id) {
			super(id);
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
			RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(new BlockEntityTypeBuilder(id));
			RegistryInfo.ITEM.addBuilder(new DeviceItemBuilder(id));
		}

		@Override
		public void generateAssetJsons(AssetJsonGenerator generator) {
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
				if (builder.length() > 0) {
					builder.append(' ');
				}
				builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
			}
			return builder.toString();
		}

		private class BlockEntityTypeBuilder extends BuilderBase<BlockEntityType<?>> {
			private BlockEntityTypeBuilder(ResourceLocation id) {
				super(id);
			}

			@Override
			public RegistryInfo getRegistryType() {
				return RegistryInfo.BLOCK_ENTITY_TYPE;
			}

			@Override
			public BlockEntityType<?> createObject() {
				BlockEntityType<E> created = BlockEntityType.Builder.of(DeviceBuilder.this::createBlockEntity, block).build(null);
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
				item = new SteamBlockItem(block, new Item.Properties());
				return item;
			}
		}
	}

	private class FlywheelBuilder extends DeviceBuilder<SteamFlywheelBlock, SteamFlywheelBlockEntity> {
		private FlywheelBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		protected String devicePath() {
			return "block/steam/flywheel";
		}

		@Override
		protected SteamFlywheelBlock createBlock(BlockBehaviour.Properties properties) {
			return new SteamFlywheelBlock(properties);
		}

		@Override
		protected SteamFlywheelBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new SteamFlywheelBlockEntity(blockEntityType, pos, state);
		}

		@Override
		protected void generateBlockState(AssetJsonGenerator generator) {
			JsonObject variants = new JsonObject();
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				for (String connection : new String[]{"none", "left", "right"}) {
					JsonObject variant = new JsonObject();
					variant.addProperty("model", modelId(baseModel() + "_casing_" + connection));
					variant.addProperty("y", (int) direction.toYRot() + 90);
					variants.add("facing=" + direction.getSerializedName() + ",connection=" + connection, variant);
				}
			}
			writeBlockState(generator, variants);
		}
	}

	private class EngineBuilder extends DeviceBuilder<SteamEngineBlock, SteamEngineBlockEntity> {
		private EngineBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		protected String devicePath() {
			return "block/steam/engine";
		}

		@Override
		protected SteamEngineBlock createBlock(BlockBehaviour.Properties properties) {
			// KubeJS 的 startup 脚本在配置文件加载之前执行, 注册阶段不读取任何配置.
			// 脚本没写的参数保持 SteamEngineDriveSpec.builder() 的默认值(0),
			// SteamEngineBlockEntity 在世界里运行时回退到该设备类的默认常量.
			SteamEngineDriveSpec.Builder builder = SteamEngineDriveSpec.builder();

			if (strength != null) {
				builder.strength(strength);
			}
			if (speed != null) {
				builder.speed(speed);
			}
			if (consumption != null) {
				builder.consumption(consumption);
			}
			if (storage != null) {
				builder.storage(storage);
			}
			return new SteamEngineBlock(properties, builder.build());
		}

		@Override
		protected SteamEngineBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new SteamEngineBlockEntity(blockEntityType, pos, state);
		}


		@Override
		protected void generateBlockState(AssetJsonGenerator generator) {
			JsonObject variants = new JsonObject();
			for (Direction direction : Direction.Plane.HORIZONTAL) {
				for (boolean lit : new boolean[]{false, true}) {
					JsonObject variant = new JsonObject();
					variant.addProperty("model", modelId(baseModel()));
					variant.addProperty("y", (int) direction.toYRot() + 180);
					variants.add("facing=" + direction.getSerializedName() + ",lit=" + lit, variant);
				}
			}
			writeBlockState(generator, variants);
		}
	}
}
