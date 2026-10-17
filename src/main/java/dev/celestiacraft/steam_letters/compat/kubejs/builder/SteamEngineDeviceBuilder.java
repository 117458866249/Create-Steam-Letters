package dev.celestiacraft.steam_letters.compat.kubejs.builder;

import com.google.gson.JsonObject;
import dev.celestiacraft.steam_letters.common.block.engine.SteamEngineBlock;
import dev.celestiacraft.steam_letters.common.block.engine.SteamEngineBlockEntity;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlock;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlockEntity;
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

/**
 * 蒸汽引擎 + 蒸汽飞轮的 KubeJS 注册器.
 * <p>
 * 一次 {@code event.create(id, "steam_letters:steam_engine")} 只产出两个方块: 脚本 id 本身就是引擎方块
 * (脚本 id 为 {@code steel_engine} 时注册 {@code kubejs:steel_engine}), 飞轮由构造器追加注册, id 是把
 * 脚本 id 末尾的 {@code _engine} 换成 {@code _flywheel} ({@code kubejs:steel_flywheel}).
 * <p>
 * 五个参数全部挂在引擎一侧: 飞轮不决定任何数值, 它只把引擎给出的转速与应力送进 Create 的动力网络.
 */
public class SteamEngineDeviceBuilder extends DeviceBuilder<SteamEngineBlock, SteamEngineBlockEntity> {
	public static final String FLYWHEEL_SUFFIX = "_flywheel";
	public static final String ENGINE_SUFFIX = "_engine";

	private final FlywheelBuilder flywheel;
	private boolean engineObjectsCreated;
	private boolean flywheelObjectsCreated;

	public SteamEngineDeviceBuilder(ResourceLocation id) {
		super(id);

		flywheel = new FlywheelBuilder(flywheelId(id));

		RegistryInfo.BLOCK.addBuilder(flywheel);
	}

	/**
	 * {@code steel_engine} 的飞轮叫 {@code steel_flywheel}, 其余 id 直接追加后缀.
	 */
	private static ResourceLocation flywheelId(ResourceLocation id) {
		String path = id.getPath();

		if (path.endsWith(ENGINE_SUFFIX) && path.length() > ENGINE_SUFFIX.length()) {
			path = path.substring(0, path.length() - ENGINE_SUFFIX.length());
		}

		return ResourceLocation.fromNamespaceAndPath(id.getNamespace(), path + FLYWHEEL_SUFFIX);
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
		return new SteamEngineBlock(properties, (builder) -> builder
				.strength(strength)
				.speed(speed)
				.consumption(consumption)
				.storage(storage), () -> blockEntityType);
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

	@Info("材质, 决定默认模型与贴图目录, 默认 steam_letters:bronze; 引擎与配对飞轮同时生效")
	public SteamEngineDeviceBuilder material(ResourceLocation material) {
		this.material = material;
		flywheel.material = material;
		return this;
	}

	@Info("完整模型路径, 不写则按设备目录 + 材质推导; 引擎与配对飞轮同时生效")
	public SteamEngineDeviceBuilder model(ResourceLocation model) {
		this.model = model;
		flywheel.model = model;
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

	@Info("满负荷蒸汽消耗 (mB/tick), 不写时使用引擎类的默认值")
	public SteamEngineDeviceBuilder consumption(double consumption) {
		this.consumption = consumption;
		return this;
	}

	@Info("引擎自带蒸汽罐容量 (mB), 默认 32000")
	public SteamEngineDeviceBuilder storage(int storage) {
		this.storage = storage;
		return this;
	}

	private static class FlywheelBuilder extends DeviceBuilder<SteamFlywheelBlock, SteamFlywheelBlockEntity> {
		private FlywheelBuilder(ResourceLocation id) {
			super(id);
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
