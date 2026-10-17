package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.press.MechanicalPressBlockEntity;
import com.simibubi.create.content.kinetics.press.PressingBehaviour;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.simibubi.create.foundation.ponder.element.BeltItemElement;
import dev.celestiacraft.steam_letters.common.block.steam_hammer.SteamHammerBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import dev.celestiacraft.steam_letters.common.register.SlParticles;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

/**
 * 蒸汽锻锤的 Ponder 场景.
 *
 * <p>schematic: {@code assets/steam_letters/ponder/steam_hammer/usage.nbt}
 * <p>lang 键前缀: {@code steam_letters.ponder.steam_hammer}
 */
public class SteamHammerScene {
	private static final int STEAM_FULL = 10000;
	private static final float SPEED = 32.0F;

	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("steam_hammer", "Steam Hammer");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		BlockPos hammerPos = util.grid().at(2, 3, 2);
		BlockPos depotPos = util.grid().at(2, 1, 1);
		BlockPos drivePos = util.grid().at(2, 1, 3);
		BlockPos beltStart = util.grid().at(0, 1, 2);
		BlockPos beltPulley = util.grid().at(2, 1, 2);
		BlockPos tankPos = util.grid().at(4, 1, 1);

		Selection beltLine = util.select().fromTo(0, 1, 2, 4, 1, 2);
		Selection driveTrain = util.select().fromTo(2, 1, 3, 2, 3, 3)
				.add(util.select().fromTo(2, 1, 4, 2, 1, 5))
				.add(util.select().position(3, 0, 5));
		Selection steamRig = util.select().fromTo(4, 1, 1, 4, 3, 1)
				.add(util.select().fromTo(3, 1, 1, 3, 3, 1))
				.add(util.select().position(3, 3, 2));

		// —— 结构展示 ——
		scene.world().showSection(util.select().layer(0), Direction.UP);
		scene.idle(5);
		scene.world().showSection(beltLine, Direction.NORTH);
		scene.idle(4);
		scene.world().showSection(util.select().fromTo(0, 1, 3, 0, 2, 3), Direction.NORTH);
		scene.idle(4);
		scene.world().showSection(driveTrain, Direction.DOWN);
		scene.idle(4);
		scene.world().showSection(util.select().position(depotPos), Direction.DOWN);
		scene.idle(4);
		scene.world().showSection(util.select().position(hammerPos), Direction.DOWN);
		scene.idle(15);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("The Steam Hammer is the steam-powered counterpart of the Mechanical Press")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.NORTH))
				.placeNearTarget();
		scene.idle(80);

		// —— 旋转动力 ——
		scene.overlay().showText(60)
				.attachKeyFrame()
				.text("It needs rotational force, just like any other kinetic machine")
				.pointAt(util.vector().blockSurface(drivePos, Direction.NORTH))
				.placeNearTarget();
		scene.idle(50);

		scene.world().setKineticSpeed(driveTrain, SPEED);
		scene.world().setKineticSpeed(beltLine, SPEED);
		scene.effects().rotationSpeedIndicator(drivePos);
		scene.idle(50);

		// —— 蒸汽接入 ——
		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("But rotation alone does nothing - it also needs steam")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.UP))
				.placeNearTarget();
		scene.idle(40);

		scene.world().showSection(steamRig, Direction.DOWN);
		scene.idle(15);

		BlockPos[] pipePath = {
				util.grid().at(4, 1, 1),
				util.grid().at(3, 1, 1),
				util.grid().at(3, 2, 1),
				util.grid().at(3, 3, 1),
				util.grid().at(3, 3, 2),
		};
		for (BlockPos pipePos : pipePath) {
			scene.effects().emitParticles(
					util.vector().centerOf(pipePos),
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(), Vec3.ZERO),
					0.5F,
					12
			);
			scene.idle(4);
		}

		scene.effects().indicateSuccess(tankPos);
		scene.world().modifyBlockEntity(hammerPos, SteamHammerBlockEntity.class, (entity) -> {
			entity.setSteamStack(new FluidStack(SlFluids.STEAM.getSource(), STEAM_FULL));
		});
		scene.idle(15);
		scene.effects().indicateSuccess(hammerPos);
		scene.idle(30);

		// —— 传送带送料 + 敲击 ——
		scene.overlay().showText(60)
				.attachKeyFrame()
				.text("Belt-delivered items are pressed in whole stacks")
				.pointAt(util.vector().centerOf(beltPulley).add(0, 0.5, 0))
				.placeNearTarget();
		scene.idle(25);

		ItemStack ingots = new ItemStack(Items.IRON_INGOT, 64);
		ElementLink<BeltItemElement> incoming = scene.world()
				.createItemOnBelt(beltStart, Direction.SOUTH, ingots);
		scene.idle(20);

		// 让传送带把物品送到锻锤正下方后停下
		scene.world().stallBeltItem(incoming, true);
		scene.idle(12);
		startPressing(scene, hammerPos);
		scene.idle(12);
		strikeEffect(scene, util, hammerPos, beltPulley, ingots);
		scene.idle(20);

		scene.world().removeItemsFromBelt(beltPulley);
		scene.world().removeItemsFromBelt(util.grid().at(3, 1, 2));
		scene.idle(5);
		scene.world().createItemOnBelt(beltPulley, Direction.UP, AllItems.IRON_SHEET.asStack(64));
		scene.idle(30);

		scene.overlay().showText(60)
				.attachKeyFrame()
				.text("A single strike handles up to 64 items at once")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.WEST))
				.placeNearTarget();
		scene.idle(70);

		// —— 蒸汽消耗 ——
		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("Each cycle burns 1000 mB of steam; once dry, it stops")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.EAST))
				.placeNearTarget();
		scene.idle(80);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("Pipe steam in from any side and it keeps on pressing")
				.pointAt(util.vector().centerOf(util.grid().at(3, 3, 2)))
				.placeNearTarget();
		for (BlockPos pipePos : new BlockPos[]{util.grid().at(3, 3, 2), util.grid().at(3, 3, 1)}) {
			scene.effects().emitParticles(
					util.vector().centerOf(pipePos),
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(), Vec3.ZERO),
					0.5F,
					12
			);
			scene.idle(4);
		}
		scene.idle(70);

		scene.markAsFinished();
	}

	private static void startPressing(CreateSceneBuilder scene, BlockPos hammerPos) {
		scene.world().modifyBlockEntity(hammerPos, MechanicalPressBlockEntity.class, (entity) -> {
			entity.getPressingBehaviour().start(PressingBehaviour.Mode.BELT);
		});
	}

	private static void strikeEffect(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos hammerPos, BlockPos beltPulley, ItemStack stack) {
		Vec3 impact = util.vector().centerOf(beltPulley).add(0, 0.5, 0);
		scene.world().modifyBlockEntity(hammerPos, MechanicalPressBlockEntity.class, (entity) -> {
			entity.getPressingBehaviour().makePressingParticleEffect(impact, stack);
		});
	}
}
