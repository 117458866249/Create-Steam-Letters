package dev.celestiacraft.steam_letters.datagen.language;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import dev.celestiacraft.steam_letters.api.SlTranKeys;
import dev.celestiacraft.steam_letters.client.ponder.SlPonderTags;
import dev.celestiacraft.steam_letters.config.api.ConfigModule;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

/**
 * 中文语言数据生成.
 * <p>
 * 输出 {@code assets/steam_letters/lang/zh_cn.json}, 与 {@link English} 生成的 en_us / en_ud 一一对应:
 * 方块与物品名, 物品提示, 护目镜读数, JEI 分类, Ponder 文本以及配置项的名称与注释都在这里.
 * 配置项的界面名使用 {@link ConfigModule#translationKey}, 注释使用 {@code <键名>.comment},
 * 供 Configured 读取 <code>translationKey + ".comment"</code> 时显示中文.
 */
public class Chinese extends LanguageProvider {
	private static final String MODID = CreateLettersSteam.MODID;

	public Chinese(PackOutput output) {
		super(output, MODID, "zh_cn");
	}

	@Override
	protected void addTranslations() {
		addBlocks();
		addItems();
		addFluids();
		addCreativeTab();
		addTooltips();
		addGoggles();
		addJei();
		addPonder();
		addConfigNames();
		addConfigComments();
	}

	private void addBlocks() {
		addBlock("advanced_spout", "高级注液器");
		addBlock("bronze_basin", "青铜工作盆");
		addBlock("bronze_block", "青铜块");
		addBlock("bronze_boiler", "青铜锅炉");
		addBlock("bronze_casing", "青铜机壳");
		addBlock("bronze_depot", "青铜置物台");
		addBlock("bronze_fluid_burner", "青铜流体燃烧室");
		addBlock("bronze_solar_boiler", "青铜太阳能锅炉");
		addBlock("bronze_solid_burner", "青铜固体燃烧室");
		addBlock("bronze_steam_engine", "青铜飞轮引擎");
		addBlock("bronze_steam_flywheel", "青铜蒸汽飞轮");
		addBlock("bronze_steam_pump", "青铜蒸汽泵");
		addBlock("steam_hammer", "蒸汽锻锤");
		addBlock("steam_mixer", "蒸汽搅拌器");
		addBlock("steam_saw", "蒸汽锯");
		addBlock("tin_block", "锡块");
	}

	private void addItems() {
		addItem("bronze_ingot", "青铜锭");
		addItem("bronze_plate", "青铜板");
		addItem("bronze_whisk", "青铜搅拌器");
		addItem("empty_steam_container", "空的密封蒸汽容器");
		addItem("incomplete_steam_mechanism", "蒸汽构件(半成品)");
		addItem("sealed_steam_container", "密封蒸汽容器");
		addItem("steam_bucket", "蒸汽桶");
		addItem("steam_mechanism", "蒸汽构件");
		addItem("superheated_steam_bucket", "过热蒸汽桶");
		addItem("tin_ingot", "锡锭");
		addItem("tin_nugget", "锡粒");
		addItem("tin_plate", "锡板");
	}

	private void addFluids() {
		add("fluid." + MODID + ".steam", "蒸汽");
		add("fluid." + MODID + ".superheated_steam", "过热蒸汽");
	}

	private void addCreativeTab() {
		add(SlTranKeys.BASIC_TAB, "机械动力: 来自蒸汽的信");
	}

	private void addTooltips() {
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_SUMMARY), "单次工作便可处理_整组_物品, 覆盖_压印_, _压缩_与_自动打包_配方");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_CONDITION), "工作时:");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR1), "仅当机器内有 _%s mB_ 蒸汽时才能工作");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_HAMMER_BEHAVIOUR2), "每次工作消耗 _%s mB_ 蒸汽");

		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PUMP_SUMMARY), "只输送蒸汽, 且只有当管道中的介质是蒸汽时才会工作");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PUMP_BEHAVIOUR1), "不需要旋转动力, 也不消耗应力");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PUMP_BEHAVIOUR2), "接上流体管道或流体容器后便会自行开始工作");

		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PIPE_SUMMARY), "只允许蒸汽通过这条管道");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_PIPE_BEHAVIOUR), "与青铜蒸汽泵搭配使用");

		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_BRONZE_BASIN_SUMMARY), "每个流体槽都能容纳普通工作盆 16 倍的流体");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_BRONZE_BASIN_BEHAVIOUR1), "可以容纳多种流体, 供搅拌与压块配方使用");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_BRONZE_BASIN_BEHAVIOUR2), "一次可以处理更多配方, 不必频繁补充流体");

		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_SEALED_CONTAINER_EMPTY), "可容纳 _%s mB_ 蒸汽");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_SEALED_CONTAINER_FLUID), "%s: _%s_ / %s mB");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_CONTAINER_EXTRACT), "▸ 右键锅炉, 流体储罐或飞轮引擎以抽取蒸汽");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_CONTAINER_INSERT), "▸ 右键流体储罐或飞轮引擎以注入蒸汽");
		add(SlTranKeys.langKey(SlTranKeys.TOOLTIP_STEAM_CONTAINER_EXTRACT_MORE), "▸ 潜行右键以继续抽取");
	}

	private void addGoggles() {
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER), "蒸汽燃烧室");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER_TEMPERATURE), "当前温度");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER_FUEL_TIME), "剩余燃烧时间");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_BURNER_EFFICIENCY), "生产效率");

		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER), "太阳能锅炉");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_WORKING), "正在产出蒸汽");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_IDLE), "无光照, 未产出");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_SUNLIGHT), "光源: 太阳光");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_RAIN), "光源: 太阳光 (雨天)");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_THUNDER), "光源: 太阳光 (雷雨)");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_ARTIFICIAL), "光源: 人造光");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_LIGHT_NONE), "光源: 无");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_PRODUCTION), "蒸汽生产速率");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_SOLAR_BOILER_RATE), "占满速比例");

		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE), "飞轮引擎");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_IDLE), "未运行");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_HEATUP), "预热中");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STATUS_RUNNING), "运行中");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_HEATUP), "预热进度");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STRESS), "应力输出");
		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_STEAM_ENGINE_STEAM), "蒸汽储量");

		add(SlTranKeys.langKey(SlTranKeys.GOGGLE_BRONZE_DEPOT_CONTENTS), "青铜置物台内容");

		add("steam_letters.gui.advanced_spout.parallel", "并行");
		add("steam_letters.gui.advanced_spout.value.parallel", "×%s");
	}

	private void addJei() {
		add(SlTranKeys.langKey(SlTranKeys.JEI_SOLID_FUEL), "固体燃料");
		add(SlTranKeys.langKey(SlTranKeys.JEI_FLUID_FUEL), "流体燃料");
	}

	private void addPonder() {
		ponderTag(SlPonderTags.SL.getPath(), "机械动力: 来自蒸汽的信", "设备");

		ponder("advanced_spout", "高级注液器",
				"高级注液器可以为其下方的物品注入流体",
				"它比普通注液器快得多: 5 tick 就能完成一次注液",
				"注液器不能手动倒入流体, 必须用泵和管道为它输送",
				"物品被放在下方或经过下方时, 就会被注入流体",
				"用扳手打开侧面的面板, 可以调整并行数量, 范围是 1 到 20",
				"放在传送带上时, 它会自动拦下经过的物品并处理");

		ponder("bronze_steam_pump", "青铜蒸汽泵",
				"如果将引擎和锅炉紧贴在一起, 将会降低效率",
				"在这里外接一个动力泵又太麻烦, 该怎么做呢",
				"这时候就该青铜蒸汽泵出场了",
				"把它放在引擎和锅炉中间即可",
				"它最大的好处是无需任何外接应力",
				"那代价是什么呢? 除了蒸汽什么都运输不了");

		ponder("solar_boiler", "太阳能锅炉",
				"太阳能锅炉带来了一种廉价的蒸汽获取方式",
				"向锅炉里通入水",
				"当光照充足时...(光照强度越大越好)",
				"...最好是真正的日光",
				"便能产生蒸汽",
				"放上与其它锅炉一样的飞轮结构, 便能输出应力");

		ponder("steam_engine", "蒸汽动力机组",
				"蒸汽锅炉与燃烧室, 蒸汽引擎, 蒸汽飞轮共同组成一台蒸汽动力机组",
				"燃烧室必须紧贴在锅炉的正下方, 否则锅炉得不到任何热量",
				"投入煤炭之类的固体燃料后, 热量会逐渐上升",
				"热量越高, 锅炉的产出效率越高",
				"也可以用流体燃烧室代替固体燃烧室, 燃料从侧面用管道注入",
				"用管道把水送进锅炉, 水被加热后就会变成蒸汽",
				"把蒸汽引擎紧贴在锅炉旁边, 它就能从背后抽取蒸汽",
				"引擎本身不输出动力: 在它正前方留出一格, 放上蒸汽飞轮",
				"飞轮再把引擎产生的应力送进动力网络",
				"用扳手可以调整飞轮的连接方向, 决定动力从哪一侧取出",
				"蒸汽耗尽后, 引擎会停机, 飞轮也停止输出");

		ponder("steam_hammer", "蒸汽锻锤",
				"蒸汽锻锤是一台压机, 会把传入的物品压制成型",
				"它不仅需要更高的应力维持工作...",
				"...还需要源源不断的蒸汽供应",
				"作为回报，它可以一次处理多个物品",
				"当蒸汽断供时，锻锤将无法处理物品");

		ponder("steam_mixer", "蒸汽搅拌机",
				"蒸汽搅拌机需要架在青铜工作盆的正上方",
				"它需要旋转动力才能运转",
				"它同样需要蒸汽, 运转时会不断消耗内部的蒸汽",
				"投进工作盆的物品会被同时搅拌",
				"蒸汽充足时, 一次可以并行处理多份搅拌配方",
				"蒸汽耗尽后, 搅拌机会立刻停机");

		ponder("steam_saw", "蒸汽锯",
				"蒸汽锯是一台动力锯, 会把送入的物品切开",
				"它需要旋转动力才能运转",
				"它还需要蒸汽, 运转时会不断消耗内部的蒸汽",
				"以原木为例: 送进锯口的物品会被切成对应的产出",
				"蒸汽充足时, 一次可以并行处理整叠物品",
				"蒸汽耗尽后, 锯会立刻停机");
	}

	private void addConfigNames() {
		add(ConfigModule.translationKey("general"), "通用");

		add(ConfigModule.translationKey("steam"), "蒸汽");
		add(ConfigModule.translationKey("steam", "superheated_steam_consumption_multiplier"), "机器使用过热蒸汽时的蒸汽消耗倍率");
		add(ConfigModule.translationKey("steam", "output_steam_fluid"), "锅炉产出的流体 ID。修改后机器内已有的流体会被原样保留, 不会被转换");

		add(ConfigModule.translationKey("steam_hammer"), "蒸汽锻锤");
		add(ConfigModule.translationKey("steam_hammer", "steam_hammer_parallel"), "蒸汽锻锤一次工作的配方并行数");
		add(ConfigModule.translationKey("steam_hammer", "steam_consumption"), "蒸汽锻锤每次工作的蒸汽消耗(mB)");
		add(ConfigModule.translationKey("steam_hammer", "steam_capacity"), "蒸汽锻锤的蒸汽容量(mB)");

		add(ConfigModule.translationKey("steam_mixer"), "蒸汽搅拌器");
		add(ConfigModule.translationKey("steam_mixer", "steam_mixer_parallel"), "蒸汽搅拌器一次工作的配方并行数");
		add(ConfigModule.translationKey("steam_mixer", "steam_consumption"), "蒸汽搅拌器每次工作的蒸汽消耗(mB)");
		add(ConfigModule.translationKey("steam_mixer", "steam_capacity"), "蒸汽搅拌器的蒸汽容量(mB)");

		add(ConfigModule.translationKey("steam_saw"), "蒸汽锯");
		add(ConfigModule.translationKey("steam_saw", "steam_saw_parallel"), "蒸汽锯一次工作的配方并行数");
		add(ConfigModule.translationKey("steam_saw", "steam_consumption"), "蒸汽锯每次工作的蒸汽消耗(mB)");
		add(ConfigModule.translationKey("steam_saw", "steam_capacity"), "蒸汽锯的蒸汽容量(mB)");
		add(ConfigModule.translationKey("steam_saw", "processing_time_multiplier"), "蒸汽锯的工作时间倍率");

		add(ConfigModule.translationKey("steam_boiler"), "锅炉");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_1"), "锅炉进入第 1 档效率所需的热量百分比");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_2"), "锅炉进入第 2 档效率所需的热量百分比");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_3"), "锅炉进入第 3 档效率所需的热量百分比");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_4"), "锅炉进入第 4 档效率所需的热量百分比");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_1"), "锅炉第 1 档运行效率的产出倍率");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_2"), "锅炉第 2 档运行效率的产出倍率");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_3"), "锅炉第 3 档运行效率的产出倍率");
		add(ConfigModule.translationKey("steam_boiler", "efficiency_4"), "锅炉第 4 档运行效率的产出倍率");

		add(ConfigModule.translationKey("solar_boiler"), "太阳能锅炉");
		add(ConfigModule.translationKey("solar_boiler", "render_fluid"), "是否渲染太阳能锅炉内部的流体");
		add(ConfigModule.translationKey("solar_boiler", "sunlight_efficiency"), "太阳能锅炉无遮挡时的产出效率倍率");
		add(ConfigModule.translationKey("solar_boiler", "artificial_light_efficiency"), "人造光下太阳能锅炉的产出效率倍率");
		add(ConfigModule.translationKey("solar_boiler", "artificial_light_threshold"), "判定为人造光所需的最低方块光照等级");
		add(ConfigModule.translationKey("solar_boiler", "require_daylight"), "太阳光路是否额外要求处于白天(原版天空光等级在夜间仍为满值)");
		add(ConfigModule.translationKey("solar_boiler", "rain_efficiency"), "降雨天气的效率倍率");
		add(ConfigModule.translationKey("solar_boiler", "thunder_efficiency"), "雷暴天气的效率倍率");

		add(ConfigModule.translationKey("steam_burner"), "燃烧室");
		add(ConfigModule.translationKey("steam_burner", "heatup_seconds"), "烧满热量所需时间(秒)");
		add(ConfigModule.translationKey("steam_burner", "cooldown_seconds"), "热量冷却至 0 所需时间(秒)");

		add(ConfigModule.translationKey("steam_engine"), "飞轮引擎");
		add(ConfigModule.translationKey("steam_engine", "suck_efficiency"), "飞轮引擎从锅炉直抽蒸汽的效率(0.75 即 25% 损耗)");
		add(ConfigModule.translationKey("steam_engine", "superheated_consumption_multiplier"), "使用过热蒸汽时的蒸汽消耗倍率");
		add(ConfigModule.translationKey("steam_engine", "heatup_seconds"), "预热时长(秒)");
		add(ConfigModule.translationKey("steam_engine", "superheated_acceleration_multiplier"), "使用过热蒸汽时预热时长的缩短倍率");
		add(ConfigModule.translationKey("steam_engine", "superheated_output_multiplier"), "使用过热蒸汽时的应力输出倍率");

		add(ConfigModule.translationKey("bronze_basin"), "青铜工作盆");
		add(ConfigModule.translationKey("bronze_basin", "slot_capacity"), "青铜工作盆单个罐的流体容量(mB)");

		add(ConfigModule.translationKey("advanced_spout"), "高级注液器");
		add(ConfigModule.translationKey("advanced_spout", "capacity"), "高级注液器的流体容量(mB)");
		add(ConfigModule.translationKey("advanced_spout", "parallel"), "高级注液器的默认并行数(可用长按滑条调整)");
	}

	private void addConfigComments() {
		addComment(ConfigModule.translationKey("general"), "下方所有设置都需要重启服务端或客户端后才会生效。");

		addComment(ConfigModule.translationKey("steam", "superheated_steam_consumption_multiplier"),
				"所有机器使用过热蒸汽时的全局蒸汽消耗倍率。\n0.25 表示只消耗普通蒸汽的四分之一。");
		addComment(ConfigModule.translationKey("steam", "output_steam_fluid"),
				"蒸汽锅炉与太阳能锅炉产出的流体 ID。修改后储罐内已有的流体不会被转换。");

		addComment(ConfigModule.translationKey("steam_hammer", "steam_hammer_parallel"),
				"蒸汽锻锤同时处理的配方数上限。");
		addComment(ConfigModule.translationKey("steam_hammer", "steam_consumption"),
				"蒸汽锻锤每完成一次配方消耗的蒸汽(mB)。");
		addComment(ConfigModule.translationKey("steam_hammer", "steam_capacity"),
				"蒸汽锻锤内部可缓存的蒸汽量(mB)。");

		addComment(ConfigModule.translationKey("steam_mixer", "steam_mixer_parallel"),
				"蒸汽搅拌器同时处理的配方数上限。");
		addComment(ConfigModule.translationKey("steam_mixer", "steam_consumption"),
				"蒸汽搅拌器每完成一次配方消耗的蒸汽(mB)。");
		addComment(ConfigModule.translationKey("steam_mixer", "steam_capacity"),
				"蒸汽搅拌器内部可缓存的蒸汽量(mB)。");

		addComment(ConfigModule.translationKey("steam_saw", "steam_saw_parallel"),
				"蒸汽锯同时处理的配方数上限。");
		addComment(ConfigModule.translationKey("steam_saw", "steam_consumption"),
				"蒸汽锯每完成一次配方消耗的蒸汽(mB)。");
		addComment(ConfigModule.translationKey("steam_saw", "steam_capacity"),
				"蒸汽锯内部可缓存的蒸汽量(mB)。");
		addComment(ConfigModule.translationKey("steam_saw", "processing_time_multiplier"),
				"蒸汽锯的工作时间倍率。\n0.25 表示比机械锯快 4 倍。");

		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_1"),
				"锅炉开始产出蒸汽所需的最低热量百分比。");
		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_2"),
				"锅炉进入第 2 档效率所需的热量百分比。");
		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_3"),
				"锅炉进入第 3 档效率所需的热量百分比。");
		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_threshold_4"),
				"锅炉达到满效率所需的热量百分比。");
		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_1"),
				"第 1 档效率时的蒸汽产出倍率。");
		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_2"),
				"第 2 档效率时的蒸汽产出倍率。");
		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_3"),
				"第 3 档效率时的蒸汽产出倍率。");
		addComment(ConfigModule.translationKey("steam_boiler", "efficiency_4"),
				"第 4 档效率时的蒸汽产出倍率, 即锅炉的最大产出。");

		addComment(ConfigModule.translationKey("solar_boiler", "render_fluid"),
				"是否渲染太阳能锅炉内部的流体。\n"
						+ "关闭后不影响蒸汽产出。");
		addComment(ConfigModule.translationKey("solar_boiler", "sunlight_efficiency"),
				"锅炉直接被天空光照射(且无天气惩罚)时的效率倍率。");
		addComment(ConfigModule.translationKey("solar_boiler", "artificial_light_efficiency"),
				"锅炉被方块光照亮而非天空光时的效率倍率。");
		addComment(ConfigModule.translationKey("solar_boiler", "artificial_light_threshold"),
				"人造光路可用所需的最低方块光照等级。");
		addComment(ConfigModule.translationKey("solar_boiler", "require_daylight"),
				"天空光路是否额外要求处于白天。\n原版天空光等级在夜间仍为 15, 不开启该选项时锅炉会在完全黑暗中满速产出。");
		addComment(ConfigModule.translationKey("solar_boiler", "rain_efficiency"),
				"锅炉所在位置降雨时的天气倍率。");
		addComment(ConfigModule.translationKey("solar_boiler", "thunder_efficiency"),
				"锅炉所在位置雷暴时的天气倍率(雷暴中的降雪同样计入)。");

		addComment(ConfigModule.translationKey("steam_burner", "heatup_seconds"),
				"燃烧燃料时, 从 0 升到最高热量所需的秒数。0 表示瞬间烧满。");
		addComment(ConfigModule.translationKey("steam_burner", "cooldown_seconds"),
				"没有燃料燃烧时, 从最高热量降到 0 所需的秒数。0 表示瞬间冷却。");

		addComment(ConfigModule.translationKey("steam_engine", "suck_efficiency"),
				"从相邻锅炉直抽蒸汽的效率(0~1)。\n每 tick 从锅炉抽出 4n mB, 实际只有 3n mB 进入引擎,\n因此直连供气的引擎应力输出上限为 75%。");
		addComment(ConfigModule.translationKey("steam_engine", "superheated_consumption_multiplier"),
				"飞轮引擎使用过热蒸汽时的蒸汽消耗倍率。\n0.5 表示只需普通蒸汽的一半。");
		addComment(ConfigModule.translationKey("steam_engine", "heatup_seconds"),
				"飞轮引擎从开始供汽到输出满档应力所需的秒数。\n预热时长四等分, 蒸汽消耗依次为满负荷的 25%, 50%, 75%, 100%。\n0 表示无需预热, 立刻达到满档。");
		addComment(ConfigModule.translationKey("steam_engine", "superheated_acceleration_multiplier"),
				"使用过热蒸汽时, 预热时长按该倍率缩短。\n2.0 表示预热时间减半。");
		addComment(ConfigModule.translationKey("steam_engine", "superheated_output_multiplier"),
				"使用过热蒸汽时的应力输出倍率。");

		addComment(ConfigModule.translationKey("bronze_basin", "slot_capacity"),
				"青铜工作盆每个流体罐的容量(mB)。\n工作盆有多个流体罐, 总容量是这个值的数倍。");

		addComment(ConfigModule.translationKey("advanced_spout", "capacity"),
				"高级注液器的流体缓存容量(mB)。");
		addComment(ConfigModule.translationKey("advanced_spout", "parallel"),
				"新放置的高级注液器的默认并行数,\n之后仍可用扳手逐台调整。");
	}

	private void addBlock(String id, String name) {
		add("block." + MODID + "." + id, name);
	}

	private void addItem(String id, String name) {
		add("item." + MODID + "." + id, name);
	}

	private void addComment(String translationKey, String comment) {
		add(translationKey + ".comment", comment);
	}

	private void ponder(String scene, String header, String... texts) {
		add(SlTranKeys.langKey("ponder." + scene + ".header"), header);
		for (int i = 0; i < texts.length; i++) {
			add(SlTranKeys.langKey("ponder." + scene + ".text_" + (i + 1)), texts[i]);
		}
	}

	private void ponderTag(String tag, String title, String description) {
		add(SlTranKeys.langKey("ponder.tag." + tag), title);
		add(SlTranKeys.langKey("ponder.tag." + tag + ".description"), description);
	}
}
