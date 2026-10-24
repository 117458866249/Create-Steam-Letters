# Create: Letter's in Steam

给 [机械动力 (Create)](https://github.com/Creators-of-Create/Create) 补上一整套 **蒸汽体系**: 用水和燃料生产蒸汽,
再用蒸汽换取应力 (SU)与加工能力.

蒸汽在这里不是装饰性的粒子效果, 而是一条完整的生产链: **燃料 -> 热量 -> 蒸汽 -> 动力 / 加工**. 所有设备都是普通方块,
可以接入
Create 的应力网络, 传送带, 漏斗, 工作盆, 护目镜信息与 Ponder 教学.

| 项目                 | 说明                                                             |
|----------------------|------------------------------------------------------------------|
| 模组 ID              | `steam_letters`                                                  |
| 环境                 | Minecraft 1.20.1 / Forge 47.x / Create 6.0+                      |
| 许可证               | GNU LGPL 3.0                                                     |
| 版本号与全部依赖版本 | 以 [`gradle.properties`](gradle.properties) 为准, 本文不重复抄写 |

---

## 一. 内容概览

按 **职能**分类, 而不是逐个列举方块 -- 清单本身会变, 职能不会:

| 分类       | 作用                                                                   | 权威清单                                                                                                                                                                                            |
|------------|------------------------------------------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 蒸汽生产   | 锅炉, 太阳能锅炉: 把水加热成蒸汽, 热量来自下方燃烧室或自然光           | [`SlBlocks.java`](src/main/java/dev/celestiacraft/steam_letters/common/register/SlBlocks.java)                                                                                                      |
| 燃料       | 固体燃烧室, 流体燃烧室: 为锅炉提供热量                                 | [`SlRecipeTypes.java`](src/main/java/dev/celestiacraft/steam_letters/common/recipe/SlRecipeTypes.java)                                                                                              |
| 动力       | 蒸汽飞轮 + 飞轮引擎: 把蒸汽转成转速与应力输出                          | [`common/block/engine/`](src/main/java/dev/celestiacraft/steam_letters/common/block/engine/)                                                                                                        |
| 加工       | 蒸汽锻锤, 蒸汽搅拌器, 蒸汽锯, 高级注液器: 以蒸汽驱动的 Create 式加工机 | [`common/block/`](src/main/java/dev/celestiacraft/steam_letters/common/block/)                                                                                                                      |
| 工作盆系   | 青铜工作盆: 多流体罐 + 并行处理的盆, 兼容搅拌 / 压缩 / 注液等盆类配方  | [`common/block/bronze_basin/`](src/main/java/dev/celestiacraft/steam_letters/common/block/bronze_basin/)                                                                                            |
| 流体输运   | 青铜蒸汽泵: 管网里有蒸汽才加压泵送                                     | [`common/block/steam_pump/`](src/main/java/dev/celestiacraft/steam_letters/common/block/steam_pump/)                                                                                                |
| 储运与材料 | 蒸汽容器(空 / 封), 蒸汽机构, 青铜与锡的材料, 机壳                      | [`SlItems.java`](src/main/java/dev/celestiacraft/steam_letters/common/register/SlItems.java) / [`SlMaterials.java`](src/main/java/dev/celestiacraft/steam_letters/common/register/SlMaterials.java) |
| 流体       | 蒸汽与过热蒸汽两种流体, 带桶与流体方块                                 | [`SlFluids.java`](src/main/java/dev/celestiacraft/steam_letters/common/register/SlFluids.java)                                                                                                      |

> 这张表只写"有哪些职能". 新增一台同类设备时, 把它注册进对应的注册类即可, 本文不需要改.

---

## 二. 蒸汽体系

### 2.1 链路

```
燃料 --> 燃烧室(热量) --> 锅炉(水 + 热量) --> 蒸汽
自然光 --> 太阳能锅炉(水) -----------> 蒸汽

蒸汽 --> 飞轮引擎(SU / RPM) --> 应力输出
     --> 加工机器 / 蒸汽泵
```

### 2.2 全模组通用约定

- 流体容量以 `mB` 计, 生产与消耗速率以 `mB/tick` 计; `20 tick = 1 秒`.
- 水与蒸汽按 `1:1` 换算, 即消耗 `1 mB` 水产出 `1 mB` 蒸汽.
- 燃料消耗, 流体消耗与产出计算一律只在 **服务端**执行; 客户端只负责渲染与提示.
- 所有需要跨区块卸载, 重载, 重启保留的状态都要持久化.
- 锅炉默认产出本模组的蒸汽流体, 产出流体 ID 可由配置替换成任意流体; **修改配置不会转换, 清空或销毁世界里已有的流体**.

### 2.3 几个跨设备的机制

| 机制     | 说明                                                                                                                                   | 实现位置                                                                                                                                                                                                                      |
|----------|----------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 热量来源 | 锅炉不维护自己的热量数据, 只读取正下方燃烧室的状态(只有一格位移, 因此不走 Capability)                                                  | [`IHeatSource.java`](src/main/java/dev/celestiacraft/steam_letters/api/machine/heat/IHeatSource.java)                                                                                                                         |
| 过热蒸汽 | 使用过热蒸汽的机器按一个全局倍率减少蒸汽消耗; 引擎则在应力输出上乘额外倍率                                                             | [`SteamConfig.java`](src/main/java/dev/celestiacraft/steam_letters/config/common/SteamConfig.java)                                                                                                                            |
| 并行     | 工作盆系机器按"并行数"连续应用配方: 原料不足或产物放不下就停, 蒸汽按实际操作扣减                                                       | [`ISteamBasinMachine.java`](src/main/java/dev/celestiacraft/steam_letters/api/machine/ISteamBasinMachine.java)                                                                                                                |
| 预热     | 引擎在预热进度走完前不写应力, 飞轮也不转; 预热只看有没有汽, 与档位无关                                                                 | [`SteamEngineBlockEntity.java`](src/main/java/dev/celestiacraft/steam_letters/common/block/engine/SteamEngineBlockEntity.java)                                                                                                |
| 注册参数 | 设备的容量, 产出, 热量上限等参数由注册时写入的 spec 承载, 用 `IntSupplier` / `DoubleSupplier` 而非裸数值 -- 因为注册发生时配置还没加载 | [`SteamEngineSpec.java`](src/main/java/dev/celestiacraft/steam_letters/api/machine/SteamEngineSpec.java) / [`SteamEngineDriveSpec.java`](src/main/java/dev/celestiacraft/steam_letters/api/machine/SteamEngineDriveSpec.java) |

具体数值一律不写在这里: 要么在配置里, 要么在注册类里.

---

## 三. 配置

| 文件                               | 类型   | 内容                                         |
|------------------------------------|--------|----------------------------------------------|
| `nebula/steam_letters/common.toml` | COMMON | 蒸汽与各设备的机制参数                       |
| `nebula/steam_letters/server.toml` | SERVER | 各设备的应力影响(impact)与应力容量(capacity) |

- 配置实现是 **模块化**的: 每个设备一个 `ConfigModule` 子类, 在 [
  `CommonConfigs.java`](src/main/java/dev/celestiacraft/steam_letters/config/CommonConfigs.java) 里挂载一次即生效.
  新增模块不需要改动别处.
- 每个配置项都带 `.comment()` 与界面翻译键, 形如 `steam_letters.config.<模块>.<键>`, 中英翻译在 [
  `zh_cn.json`](src/main/resources/assets/steam_letters/lang/zh_cn.json).
- 改动需要重启服务端或客户端后生效 (文件头部已有说明).
- 应力值走 Create 的 `BlockStressValues` 提供器, 所以能被护目镜和 Create 自己的配置体系一致地读到.

**想知道某个数字是多少, 看 toml 或注册类, 不要看这份 README.**

---

## 四. 数据驱动: 标签, 配方与 JEI

| 层             | 内容                                                                                                              | 位置                                                                                                   |
|----------------|-------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------|
| 物品标签       | `steam_letters:*` 描述设备族(蒸汽飞轮 / 引擎 / 锅炉 / 各类燃烧室 / 蒸汽容器...), `forge:*` 描述青铜, 锡等通用材料 | [`SlItemTags.java`](src/main/java/dev/celestiacraft/steam_letters/tags/SlItemTags.java)                |
| 流体标签       | `forge:steam`, `forge:superheated_steam` -- 判断"这是不是蒸汽"永远查标签, 不查具体流体 ID                         | [`SlFluidTags.java`](src/main/java/dev/celestiacraft/steam_letters/tags/SlFluidTags.java)              |
| 自定义配方类型 | `steam_letters:solid_fuel`, `steam_letters:fluid_fuel`: 声明什么东西能烧, 烧多久, 给多少热                        | [`SlRecipeTypes.java`](src/main/java/dev/celestiacraft/steam_letters/common/recipe/SlRecipeTypes.java) |
| Create 配方    | 压片, 搅拌, 粉碎, 注液, 序列组装等走 Create 原生配方类型                                                          | [`datagen/recipe/`](src/main/java/dev/celestiacraft/steam_letters/datagen/recipe/)                     |
| 数据生成       | 方块状态, 模型, 标签, 配方, 语言全部由 `runData` 生成, 手写 JSON 只是例外                                         | [`DataGenerators.java`](src/main/java/dev/celestiacraft/steam_letters/datagen/DataGenerators.java)     |
| JEI            | 燃料配方的自定义类别                                                                                              | [`compat/jei/`](src/main/java/dev/celestiacraft/steam_letters/compat/jei/)                             |

---

## 五. 兼容

- **Create 6**(必需) - 应力网络, Ponder, Flywheel, Registrate, 护目镜信息全部沿用 Create 的实现.
- **JEI** - 燃料配方类别.
- **KubeJS** - 注册自定义设备与燃料配方 (见 [7.2](#72-扩展点)).
- **Create: Diesel Generators** - 青铜工作盆接入其注液铸造行为 (软依赖, 运行时检测).
- **Jade** - 方块信息.
- **Oculus / Embeddium** - 渲染.
- **Ponder** - 自带场景与自定义 Ponder 标签.

---

## 六. 安装

1. 安装 Minecraft 1.20.1 与对应的 Forge (47.x).
2. 安装 Create (6.0+)及其依赖.
3. 把构建出的 `Create-Steam-Letters-<版本>-1.20.1.jar` 丢进 `mods/`.

可选联动 (JEI, KubeJS 等)缺失不会导致崩溃, 相关逻辑会自动跳过.

---

## 七. 开发

### 7.1 目录结构

| 目录                   | 职责                                                                                                                             |
|------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| `api/`                 | 公共契约: Registrate 入口, 应力, 翻译键, 机器接口, 粒子基类                                                                      |
| `common/block/<设备>/` | 每个设备一个包: Block, BlockEntity, 渲染器, ValueBox / 值行为                                                                    |
| `common/register/`     | 注册入口: `SlBlocks`, `SlItems`, `SlBlockEntities`, `SlFluids`, `SlMaterials`, `SlParticles`, `SlCreativeTabs`, `SlSpriteShifts` |
| `common/recipe/`       | 配方类型, 序列化器与示例生成器                                                                                                   |
| `config/`              | `ConfigModule` 基类 + 每个设备一个模块                                                                                           |
| `client/`              | 客户端专用: 模型 / 方块状态生成, Ponder, 粒子, 流体渲染, 语言键                                                                  |
| `compat/`              | JEI, KubeJS, CDG, 着色器联动                                                                                                     |
| `datagen/`             | 数据生成提供器                                                                                                                   |
| `tags/`                | 物品与流体标签                                                                                                                   |
| `utils/`               | 流体交互, 插值跟随值, 按面 Capability 缓存等小工具                                                                               |

### 7.2 扩展点

| 想做的事                         | 用这个                                                                                                                                                                                                           |
|----------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| 新的蒸汽机器(储汽 / 抽汽 / 耗汽) | 实现 [`ISteamStorage`](src/main/java/dev/celestiacraft/steam_letters/api/machine/ISteamStorage.java)                                                                                                             |
| 会应用 Create 盆类配方的机器     | 实现 [`ISteamBasinMachine`](src/main/java/dev/celestiacraft/steam_letters/api/machine/ISteamBasinMachine.java)(取并行数, 放宽配方匹配)                                                                           |
| 按压床 / 传送带逻辑处理的机器    | 实现 [`ISteamPressMachine`](src/main/java/dev/celestiacraft/steam_letters/api/machine/ISteamPressMachine.java)                                                                                                   |
| 新的热源                         | 实现 [`IHeatSource`](src/main/java/dev/celestiacraft/steam_letters/api/machine/heat/IHeatSource.java)                                                                                                            |
| 方块携带自己的机制参数           | 实现 [`ISteamEngineBlock`](src/main/java/dev/celestiacraft/steam_letters/api/machine/ISteamEngineBlock.java); 参数放在方块而非 BE 上 -- 同一个 BE 类型会服务多个方块, 放在方块上才不会互相覆盖, 也不需要额外同步 |
| 新的配置分组                     | 继承 [`ConfigModule`](src/main/java/dev/celestiacraft/steam_letters/config/api/ConfigModule.java), 在 `CommonConfigs` 挂一行                                                                                     |
| 资源 ID / Registrate 短写        | [`SlApis`](src/main/java/dev/celestiacraft/steam_letters/api/SlApis.java): `loadResource` / `loadCreate` / `loadForge` / `loadCDG`                                                                               |
| 整合包注册自定义设备             | [`compat/kubejs/builder/`](src/main/java/dev/celestiacraft/steam_letters/compat/kubejs/builder/): 锅炉, 太阳能锅炉, 固体 / 流体燃烧室, 引擎(与飞轮成对注册)各有 builder                                          |

### 7.3 新增一台设备的最小步骤

1. `common/block/<名字>/` 下建 Block + BlockEntity (需要的话再加渲染器 / 值行为).
2. 实现所需接口 (多数机器只需 `ISteamStorage`).
3. 在 `SlBlocks` / `SlBlockEntities` 注册, 标签跟着 `.tag(...)` 走.
4. 需要配置就加一个 `ConfigModule` 子类, 在 `CommonConfigs` 挂载.
5. 在 `datagen` 里补模型, 配方, 语言 (中英各一份), 或直接复用 `ItemModelGen`.
6. 想教玩家就加一个 Ponder 场景.

这套流程不触碰本文任何章节 -- **README 描述的是结构与机制, 不是库存清单.**

### 7.4 构建

```bash
./gradlew build        # 构建 jar
./gradlew runClient    # 启动客户端
./gradlew runServer    # 启动服务端
./gradlew runData      # 重新生成资源与数据
```

---

## 八. 文档与许可

- 逐设备的机制规范与设计稿: [`docs/蒸汽训练词.md`](docs/蒸汽训练词.md).
- 许可证: GNU LGPL 3.0 (见 [`gradle.properties`](gradle.properties) 与 `META-INF/mods.toml`).

---

## 附: 这份 README 的边界

为了让文档不用跟着代码一起改, 这里明确三条规矩:

1. **只写机制与结构, 不写清单.** "有哪些方块" 指向注册类, "有哪些配置项" 指向 toml.
2. **不抄数值.** 默认值只在配置与代码里出现一次, 本文再写一次就是第二个需要同步的源.
3. **分类而非枚举.** 表格每一行是一个长期存在的职能, 新增同类内容不会让它过期.

要给某个东西补大量细节, 写进 `docs/`, 别塞进这里; 本文保持在 "读完知道这个 Mod 是什么, 东西放在哪, 怎么往下加" 的粒度.
