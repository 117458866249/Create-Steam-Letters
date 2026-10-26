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
 * 蒸汽动力机组的 Ponder 场景: 蒸汽锅炉 / 固体燃烧室 / 流体燃烧室 / 蒸汽飞轮 / 飞轮引擎 共用.
 * <p>
 * 结构文件: {@code assets/steam_letters/ponder/steam_engine.nbt}
 * 等待重写
 */
public class SteamEngineScene {
	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		RandomSource random = RandomSource.create();

		scene.title("steam_engine", "Steam Power Unit");
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
				.text("A steam boiler with its burner, a steam engine and a steam flywheel form one steam power unit");
		scene.idle(90);

		scene.overlay().showText(70)
				.pointAt(burnerSide)
				.colored(PonderPalette.RED)
				.placeNearTarget()
				.attachKeyFrame()
				.text("The burner must sit directly below the boiler, otherwise the boiler gets no heat at all");
		scene.idle(80);

		scene.overlay().showText(70)
				.pointAt(burnerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("Feed solid fuel such as coal and the heat gradually rises");
		scene.idle(20);
		setHeat(scene, burner, 100.0D, 400);
		puff(scene, util.vector().centerOf(burnerPos), 10, 0.2f, random);
		scene.idle(50);

		scene.overlay().showText(60)
				.pointAt(boilerSide)
				.colored(PonderPalette.GREEN)
				.placeNearTarget()
				.attachKeyFrame()
				.text("The higher the heat, the faster the boiler produces steam");
		scene.idle(70);

		scene.overlay().showText(70)
				.pointAt(burnerSide)
				.colored(PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("A fluid burner can replace the solid burner, with fuel piped in from the side");
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
				.text("Pipe water into the boiler; once heated it turns into steam");
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
				.text("Place the steam engine against the boiler and it draws steam from the back");
		scene.idle(80);

		scene.overlay().showText(70)
				.pointAt(util.vector().blockSurface(flywheelPos, Direction.NORTH))
				.colored(PonderPalette.RED)
				.placeNearTarget()
				.attachKeyFrame()
				.text("The engine itself outputs no power: leave one block in front of it and place the steam flywheel there");
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
				.text("The flywheel then feeds the stress produced by the engine into the kinetic network");
		scene.idle(80);

		scene.overlay().showText(60)
				.pointAt(flywheelSide)
				.colored(PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("A wrench changes the flywheel facing so power can be taken from either side");
		scene.idle(70);

		scene.overlay().showText(60)
				.pointAt(engineSide)
				.colored(PonderPalette.RED)
				.placeNearTarget()
				.attachKeyFrame()
				.text("Once steam runs out the engine stops and the flywheel stops outputting");
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