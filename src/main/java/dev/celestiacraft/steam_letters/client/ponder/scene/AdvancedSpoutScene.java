package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import com.simibubi.create.foundation.ponder.element.BeltItemElement;
import dev.celestiacraft.steam_letters.common.register.SlParticles;
import net.createmod.catnip.math.Pointing;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

/**
 * 高级注液器的 Ponder 场景.
 * <p>
 * 结构文件: {@code assets/steam_letters/ponder/advanced_spout.nbt}
 * 等待重写
 */
public class AdvancedSpoutScene {
	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		RandomSource random = RandomSource.create();

		scene.title("advanced_spout", "Advanced Spout");
		scene.configureBasePlate(0, 0, 5);

		Selection base = util.select().layer(0)
				.substract(util.select().position(3, 0, 5));
		scene.world().showSection(base, Direction.UP);
		scene.idle(5);

		BlockPos spoutPos = util.grid().at(2, 3, 2);
		BlockPos depotPos = util.grid().at(2, 1, 1);

		scene.world().showSection(util.select().position(depotPos), Direction.DOWN);
		scene.idle(10);

		Selection spout = util.select().position(spoutPos);
		scene.world().showSection(spout, Direction.DOWN);
		scene.idle(10);

		Vec3 spoutSide = util.vector().blockSurface(spoutPos, Direction.WEST);

		scene.overlay().showText(70)
				.pointAt(spoutSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("The Advanced Spout injects fluid into the items below it");
		scene.idle(80);

		scene.overlay().showText(60)
				.pointAt(spoutSide)
				.colored(PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("It is far faster than a regular spout: one injection takes only 5 ticks");
		scene.idle(70);

		Selection tank = util.select().fromTo(1, 1, 4, 1, 2, 4);
		Selection pipes = util.select().position(1, 3, 4)
				.add(util.select().position(2, 3, 4));
		Selection pump = util.select().position(2, 3, 3);
		Selection kinetics = util.select().position(2, 1, 3)
				.add(util.select().position(2, 2, 3))
				.add(util.select().position(2, 1, 4))
				.add(util.select().position(2, 1, 5))
				.add(util.select().position(3, 0, 5));

		scene.world().showSection(tank, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(kinetics, Direction.UP);
		scene.world().showSection(pipes.add(pump), Direction.NORTH);
		scene.idle(10);
		scene.world().setKineticSpeed(kinetics, 32);
		scene.idle(20);

		scene.overlay().showText(70)
				.pointAt(util.vector().blockSurface(util.grid().at(2, 3, 4), Direction.NORTH))
				.placeNearTarget()
				.attachKeyFrame()
				.text("Fluid cannot be poured in by hand; it has to be supplied with pumps and pipes");
		scene.idle(20);

		for (int i = 0; i < 10; i++) {
			scene.effects().emitParticles(util.vector().topOf(spoutPos.below()),
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(),
							VecHelper.offsetRandomly(Vec3.ZERO, random, 0.1f)), 1, 1);
		}
		scene.idle(60);

		ItemStack bottle = new ItemStack(Items.GLASS_BOTTLE);
		scene.world().createItemOnBeltLike(depotPos, Direction.NORTH, bottle);
		scene.overlay().showControls(util.vector().centerOf(depotPos.south()), Pointing.UP, 40).withItem(bottle);
		scene.idle(20);

		scene.overlay().showText(60)
				.pointAt(util.vector().topOf(depotPos))
				.placeNearTarget()
				.attachKeyFrame()
				.text("Items placed below or passing below get injected");
		scene.idle(20);

		scene.world().removeItemsFromBelt(depotPos);
		scene.world().createItemOnBeltLike(depotPos, Direction.UP, new ItemStack(Items.HONEY_BOTTLE));
		scene.effects().indicateSuccess(depotPos);
		scene.idle(40);

		scene.overlay().showScrollInput(util.vector().blockSurface(spoutPos, Direction.WEST), Direction.WEST, 80);
		scene.overlay().showText(80)
				.pointAt(spoutSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("Use a wrench on the side panel to change the parallel count, from 1 to 20");
		scene.idle(90);

		Selection belt = util.select().fromTo(0, 1, 2, 4, 1, 2);
		scene.world().showSection(belt, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(60)
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.WEST))
				.placeNearTarget()
				.attachKeyFrame()
				.text("On a belt it automatically stops passing items and processes them");
		scene.idle(15);

		ElementLink<BeltItemElement> item = scene.world()
				.createItemOnBelt(util.grid().at(0, 1, 2), Direction.SOUTH, new ItemStack(Items.GLASS_BOTTLE));
		scene.idle(25);
		scene.world().stallBeltItem(item, true);
		scene.idle(10);
		scene.effects().indicateSuccess(spoutPos);
		scene.idle(20);
		scene.world().removeItemsFromBelt(util.grid().at(2, 1, 2));
		scene.world().createItemOnBelt(util.grid().at(2, 1, 2), Direction.UP, new ItemStack(Items.HONEY_BOTTLE));
		scene.idle(40);

		scene.markAsFinished();
	}
}