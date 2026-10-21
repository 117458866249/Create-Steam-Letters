package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.celestiacraft.steam_letters.common.block.boiler.AbstractBoilerBlockEntity;
import dev.celestiacraft.steam_letters.common.block.burner.AbstractBurnerBlock;
import dev.celestiacraft.steam_letters.common.block.burner.AbstractBurnerBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlBlocks;
import dev.celestiacraft.steam_letters.common.register.SlParticles;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

/**
 * 蒸汽动力机组的 Ponder 场景: 蒸汽锅炉 / 固体燃烧室 / 流体燃烧室 / 蒸汽飞轮 共用.
 * <p>
 * 结构文件: {@code assets/steam_letters/ponder/steam_engine.nbt}
 */
public class SteamEngineScene {
	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		RandomSource random = RandomSource.create();

		scene.title("steam_engine", "蒸汽动力机组");
		scene.configureBasePlate(0, 0, 5);

		BlockPos burnerPos = util.grid().at(2, 1, 1);
		BlockPos boilerPos = util.grid().at(2, 2, 1);
		BlockPos enginePos = util.grid().at(2, 2, 2);
		BlockPos flywheelPos = util.grid().at(2, 2, 4);
		BlockPos wheelShaftPos = util.grid().at(3, 2, 4);
		BlockPos wheelCogwheelPos = util.grid().at(4, 2, 4);

		Selection burner = util.select().position(burnerPos);
		Selection boiler = util.select().position(boilerPos);
		Selection engine = util.select().position(enginePos);
		Selection flywheel = util.select().position(flywheelPos);
		Selection tank = util.select().fromTo(0, 1, 2, 0, 2, 2);
		Selection pipes = util.select().position(0, 2, 1)
				.add(util.select().position(1, 2, 1));
		Selection output = util.select().position(wheelShaftPos)
				.add(util.select().position(wheelCogwheelPos))
				.add(util.select().position(4, 3, 3));

		Vec3 burnerSide = util.vector().blockSurface(burnerPos, Direction.WEST);
		Vec3 boilerSide = util.vector().blockSurface(boilerPos, Direction.WEST);
		Vec3 engineSide = util.vector().blockSurface(enginePos, Direction.SOUTH);
		Vec3 flywheelSide = util.vector().blockSurface(flywheelPos, Direction.SOUTH);

		scene.world().showSection(util.select().layer(0), Direction.UP);
		scene.idle(5);

		scene.world().showSection(burner, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(boiler, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(engine, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(flywheel, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(80)
				.pointAt(util.vector().centerOf(boilerPos))
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽锅炉与燃烧室, 蒸汽引擎, 蒸汽飞轮共同组成一台蒸汽动力机组");
		scene.idle(90);

		scene.overlay().showText(70)
				.pointAt(burnerSide)
				.colored(PonderPalette.RED)
				.placeNearTarget()
				.attachKeyFrame()
				.text("燃烧室必须紧贴在锅炉的正下方, 否则锅炉得不到任何热量");
		scene.idle(80);

		scene.overlay().showText(70)
				.pointAt(burnerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("投入煤炭之类的固体燃料后, 热量会逐渐上升");
		scene.idle(20);
		setHeat(scene, burner, 100.0D, 400);
		puff(scene, util.vector().centerOf(burnerPos), 10, 0.2f, random);
		scene.idle(50);

		scene.overlay().showText(60)
				.pointAt(boilerSide)
				.colored(PonderPalette.GREEN)
				.placeNearTarget()
				.attachKeyFrame()
				.text("热量越高, 锅炉的产出效率越高");
		scene.idle(70);

		scene.overlay().showText(70)
				.pointAt(burnerSide)
				.colored(PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("也可以用流体燃烧室代替固体燃烧室, 燃料从侧面用管道注入");
		scene.idle(20);
		scene.world().setBlock(burnerPos, SlBlocks.BRONZE_FLUID_BURNER.getDefaultState()
				.setValue(AbstractBurnerBlock.FACING, Direction.SOUTH), true);
		setHeat(scene, burner, 100.0D, 400);
		puff(scene, util.vector().centerOf(burnerPos), 10, 0.2f, random);
		scene.idle(60);

		scene.world().showSection(tank, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(pipes, Direction.NORTH);
		scene.idle(15);

		scene.overlay().showText(70)
				.pointAt(boilerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("用管道把水送进锅炉, 水被加热后就会变成蒸汽");
		scene.idle(20);
		scene.world().modifyBlockEntityNBT(boiler, AbstractBoilerBlockEntity.class, (nbt) -> {
			nbt.put("InputTank", new FluidStack(Fluids.WATER, 4000)
					.writeToNBT(new CompoundTag()));
		});
		puff(scene, util.vector().topOf(boilerPos), 10, 0.15f, random);
		scene.idle(60);

		scene.overlay().showText(70)
				.pointAt(engineSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("把蒸汽引擎紧贴在锅炉旁边, 它就能从背后抽取蒸汽");
		scene.idle(80);

		scene.overlay().showText(70)
				.pointAt(util.vector().blockSurface(flywheelPos, Direction.NORTH))
				.colored(PonderPalette.RED)
				.placeNearTarget()
				.attachKeyFrame()
				.text("引擎本身不输出动力: 在它正前方留出一格, 放上蒸汽飞轮");
		scene.idle(80);

		scene.world().showSection(output, Direction.EAST);
		scene.idle(10);
		scene.world().setKineticSpeed(output, 32);
		scene.effects().rotationSpeedIndicator(wheelCogwheelPos);
		scene.idle(20);

		scene.overlay().showText(70)
				.pointAt(util.vector().blockSurface(wheelShaftPos, Direction.UP))
				.placeNearTarget()
				.attachKeyFrame()
				.text("飞轮再把引擎产生的应力送进动力网络");
		scene.idle(80);

		scene.overlay().showText(60)
				.pointAt(flywheelSide)
				.colored(PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("用扳手可以调整飞轮的连接方向, 决定动力从哪一侧取出");
		scene.idle(70);

		scene.overlay().showText(60)
				.pointAt(engineSide)
				.colored(PonderPalette.RED)
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽耗尽后, 引擎会停机, 飞轮也停止输出");
		scene.idle(10);
		setHeat(scene, burner, 0.0D, 0);
		scene.world().setKineticSpeed(output, 0);
		scene.idle(50);

		scene.markAsFinished();
	}

	private static void setHeat(CreateSceneBuilder scene, Selection burner, double heat, int burnTime) {
		scene.world().modifyBlockEntityNBT(burner, AbstractBurnerBlockEntity.class, nbt -> {
			nbt.putDouble("Heat", heat);
			nbt.putInt("BurnTime", burnTime);
		});
	}

	private static void puff(CreateSceneBuilder scene, Vec3 at, int amount, float spread, RandomSource random) {
		for (int i = 0; i < amount; i++) {
			scene.effects().emitParticles(at,
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(),
							VecHelper.offsetRandomly(Vec3.ZERO, random, spread)), 1, 1);
		}
	}
}