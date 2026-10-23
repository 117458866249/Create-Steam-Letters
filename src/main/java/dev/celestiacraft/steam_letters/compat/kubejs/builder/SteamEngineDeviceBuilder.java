package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.google.gson.JsonObject;
import dev.celestiacraft.steam_letters.common.block.engine.SteamEngineBlock;
import dev.celestiacraft.steam_letters.common.block.engine.SteamEngineBlockEntity;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlock;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlockEntity;
import dev.celestiacraft.steam_letters.tags.SlItemTags;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import java.util.LinkedHashMap;
import java.util.Map;

public class SteamEngineDeviceBuilder extends DeviceBuilder<SteamEngineBlock, SteamEngineBlockEntity, SteamEngineDeviceBuilder> {
	public static final String ENGINE_SUFFIX = "_steam_engine";
	public static final String FLYWHEEL_SUFFIX = "_steam_flywheel";

	private final FlywheelBuilder flywheel;
	private boolean engineObjectsCreated;
	private boolean flywheelObjectsCreated;

	public SteamEngineDeviceBuilder(ResourceLocation id) {
		super(id);

		flywheel = new FlywheelBuilder(flywheelId(id));

		RegistryInfo.BLOCK.addBuilder(flywheel);

		itemTag(SlItemTags.STEAM_ENGINE.location());
		flywheel.itemTag(SlItemTags.STEAM_FLYWHEEL.location());
	}

	private static ResourceLocation flywheelId(ResourceLocation id) {
		String path = id.getPath();

		if (path.endsWith(ENGINE_SUFFIX) && path.length() > ENGINE_SUFFIX.length()) {
			path = path.substring(0, path.length() - ENGINE_SUFFIX.length());
		}

		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path + FLYWHEEL_SUFFIX);
	}

	public static ResourceLocation engineIdOf(ResourceLocation id) {
		String path = id.getPath();

		if (path.endsWith(ENGINE_SUFFIX) && path.length() > ENGINE_SUFFIX.length()) {
			return id;
		}

		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path + ENGINE_SUFFIX);
	}

	@Override
	protected SteamEngineDeviceBuilder self() {
		return this;
	}

	/**
	 * 引擎与配对飞轮共用同一份材质和模型, 任何一个变了都要一起改.
	 */
	@Override
	protected void onAppearanceChanged() {
		flywheel.material = material;
		flywheel.model = model;
	}

	@Override
	protected String devicePath() {
		return "block/steam/engine";
	}

	@Override
	protected SteamEngineBlock createBlock(BlockBehaviour.Properties properties) {
		/*
		 * KubeJS 的 startup 脚本在配置文件加载之前执行, 注册阶段不读取任何配置.
		 * 脚本没写的参数保持 SteamEngineDriveSpec.builder() 的默认值(0),
		 * SteamEngineBlockEntity 在世界里运行时回退到该设备类的默认常量.
		 */
		return new SteamEngineBlock(properties, (builder) -> {
			builder.strength(strength)
					.speed(speed)
					.consumption(consumption)
					.capacity(capacityOr(() -> 0));
		}, () -> blockEntityType);
	}

	@Override
	protected SteamEngineBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new SteamEngineBlockEntity(blockEntityType, pos, state);
	}

	@Override
	protected void generateBlockState(AssetJsonGenerator generator) {
		JsonObject variants = new JsonObject();

		for (Direction direction : Direction.Plane.HORIZONTAL) {
			for (boolean lit : new boolean[] {false, true}) {
				JsonObject variant = new JsonObject();
				variant.addProperty("model", modelId(baseModel()));
				variant.addProperty("y", (int) direction.toYRot() + 180);
				variants.add("facing=" + direction.getSerializedName() + ",lit=" + lit, variant);
			}
		}

		writeBlockState(generator, variants);
	}

	@Override
	protected void generateBlockModels(AssetJsonGenerator generator) {
		if (model != null || usesBuiltinModel()) {
			return;
		}

		generateModel(generator, baseModel(), "block/steam/engine/bronze", engineTextures());
		generateModel(generator, baseModel() + "_item", "block/steam/engine/bronze_item", engineTextures());
	}

	@Override
	protected String itemModel() {
		return baseModel() + "_item";
	}

	private static Map<String, String> engineTextures() {
		Map<String, String> textures = new LinkedHashMap<>();
		textures.put("5", "block/steam/casing/%s_casing");
		textures.put("1_1", "block/steam/engine/%s_cylinder");
		textures.put("particle", "block/steam/metal_block/%s_block");
		return textures;
	}

	/**
	 * 引擎自己的方块实体类型与方块物品由这里补注册(KubeJS 只会对脚本创建出来的 builder 调用本方法),
	 * 飞轮是构造器里 addBuilder 追加的, 同样不在事件的 created 列表里, 必须在这里一并转发.
	 */
	@Override
	public void createAdditionalObjects() {
		if (!engineObjectsCreated) {
			engineObjectsCreated = true;
			super.createAdditionalObjects();
		}

		if (!flywheelObjectsCreated) {
			flywheelObjectsCreated = true;
			flywheel.createAdditionalObjects();
		}
	}

	@Info("Flywheel stress capacity (SU). Defaults to 16384")
	public SteamEngineDeviceBuilder strength(double strength) {
		this.strength = strength;
		return this;
	}

	@Info("Flywheel rotation speed (RPM). Defaults to 32")
	public SteamEngineDeviceBuilder speed(double speed) {
		this.speed = speed;
		return this;
	}

	@Info("Steam consumption at full load (mB/tick). When omitted the engine class default is used")
	public SteamEngineDeviceBuilder consumption(double consumption) {
		this.consumption = consumption;
		return this;
	}

	private static class FlywheelBuilder extends DeviceBuilder<SteamFlywheelBlock, SteamFlywheelBlockEntity, FlywheelBuilder> {
		private FlywheelBuilder(ResourceLocation id) {
			super(id);
		}

		@Override
		protected FlywheelBuilder self() {
			return this;
		}

		@Override
		protected String devicePath() {
			return "block/steam/flywheel";
		}

		@Override
		protected SteamFlywheelBlock createBlock(BlockBehaviour.Properties properties) {
			return new SteamFlywheelBlock(properties, () -> blockEntityType, material);
		}

		@Override
		protected BlockEntityType<SteamFlywheelBlockEntity> buildBlockEntityType() {
			BlockEntityType<SteamFlywheelBlockEntity> created = super.buildBlockEntityType();
			SteamFlywheelBlockEntity.DYNAMIC_TYPES.add(created);
			return created;
		}

		@Override
		protected SteamFlywheelBlockEntity createBlockEntity(BlockPos pos, BlockState state) {
			return new SteamFlywheelBlockEntity(blockEntityType, pos, state);
		}

		@Override
		protected void generateBlockState(AssetJsonGenerator generator) {
			JsonObject variants = new JsonObject();

			for (Direction direction : Direction.Plane.HORIZONTAL) {
				for (String connection : new String[] {"none", "left", "right"}) {
					JsonObject variant = new JsonObject();
					variant.addProperty("model", modelId(baseModel() + "_casing_" + connection));
					variant.addProperty("y", (int) direction.toYRot() + 90);
					variants.add("facing=" + direction.getSerializedName() + ",connection=" + connection, variant);
				}
			}

			writeBlockState(generator, variants);
		}

		@Override
		protected void generateBlockModels(AssetJsonGenerator generator) {
			if (model != null || usesBuiltinModel()) {
				return;
			}

			for (String connection : new String[] {"none", "left", "right"}) {
				generateModel(generator, baseModel() + "_casing_" + connection, "block/steam/flywheel/bronze_casing_" + connection, flywheelTextures());
			}

			generateModel(generator, baseModel() + "_wheel", "block/steam/flywheel/bronze_wheel", wheelTextures());

			for (String part : new String[] {"upper_rotating_connector", "lower_rotating_connector", "upper_sliding_connector", "lower_sliding_connector"}) {
				generateModel(generator, baseModel() + "_" + part, "block/steam/flywheel/bronze_" + part, wheelTextures());
			}

			generateModel(generator, baseModel() + "_item", "block/steam/flywheel/bronze_item", flywheelTextures());
		}

		@Override
		protected String itemModel() {
			return baseModel() + "_item";
		}

		private static Map<String, String> wheelTextures() {
			Map<String, String> textures = new LinkedHashMap<>();
			textures.put("0", "block/steam/flywheel/%s_wheel");
			textures.put("particle", "block/steam/flywheel/%s_wheel");
			return textures;
		}

		private static Map<String, String> flywheelTextures() {
			Map<String, String> textures = new LinkedHashMap<>();
			textures.put("0", "block/steam/flywheel/%s_wheel");
			textures.put("5", "block/steam/casing/%s_casing");
			textures.put("7", "block/steam/gearbox/%s_gearbox");
			textures.put("particle", "block/steam/flywheel/%s_wheel");
			return textures;
		}
	}
}