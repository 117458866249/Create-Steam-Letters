package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.celestiacraft.steam_letters.common.block.steam_saw.SteamSawBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import dev.celestiacraft.steam_letters.common.register.SlParticles;
import net.createmod.catnip.math.VecHelper;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.EntityElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

/**
 *
 * 结构文件: {@code assets/steam_letters/ponder/steam_saw.nbt}
 * 等待重写
 */
public class SteamSawScene {
	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		RandomSource random = RandomSource.create();

		scene.title("steam_saw", "Steam Saw");
		scene.configureBasePlate(0, 0, 5);

		Selection base = util.select().layer(0)
				.substract(util.select().position(1, 0, 5));
		scene.world().showSection(base, Direction.UP);
		scene.idle(5);

		BlockPos sawPos = util.grid().at(2, 1, 2);

		Selection belts = util.select().fromTo(0, 1, 2, 1, 1, 2)
				.add(util.select().fromTo(3, 1, 2, 4, 1, 2));
		scene.world().showSection(belts, Direction.DOWN);
		scene.idle(10);

		Selection saw = util.select().position(sawPos);
		scene.world().showSection(saw, Direction.DOWN);
		scene.idle(10);

		Vec3 sawSide = util.vector().blockSurface(sawPos, Direction.WEST);

		scene.overlay().showText(70)
				.pointAt(sawSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("The Steam Saw is a mechanical saw that cuts incoming items");
		scene.idle(80);

		Selection kinetics = util.select().fromTo(1, 1, 3, 3, 1, 3)
				.add(util.select().position(2, 1, 4))
				.add(util.select().position(2, 1, 5))
				.add(util.select().position(1, 0, 5));
		scene.world().showSection(kinetics, Direction.UP);
		scene.idle(10);
		scene.world().setKineticSpeed(kinetics, 32);
		scene.effects().rotationDirectionIndicator(sawPos);
		scene.idle(20);

		scene.overlay().showText(60)
				.pointAt(util.vector().blockSurface(util.grid().at(2, 1, 3), Direction.EAST))
				.colored(PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("It needs rotational force to run");
		scene.idle(70);

		Selection tank = util.select().fromTo(0, 1, 0, 0, 2, 0);
		Selection pump = util.select().position(1, 1, 0);
		Selection pipes = util.select().position(2, 1, 0)
				.add(util.select().position(2, 1, 1));
		scene.world().showSection(tank, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(pipes.add(pump), Direction.NORTH);
		scene.idle(15);

		scene.overlay().showText(70)
				.pointAt(sawSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("It also needs steam and keeps consuming its internal steam while running");
		scene.idle(20);

		scene.world().modifyBlockEntityNBT(saw, SteamSawBlockEntity.class, (nbt) -> {
			nbt.put("Steam", new FluidStack(SlFluids.STEAM.get(), 1000)
					.writeToNBT(new CompoundTag()));
		});
		for (int i = 0; i < 8; i++) {
			scene.effects().emitParticles(util.vector().topOf(sawPos),
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(),
							VecHelper.offsetRandomly(Vec3.ZERO, random, 0.1f)), 1, 1);
		}
		scene.idle(60);

		Vec3 sawTop = util.vector().topOf(sawPos);
		ElementLink<EntityElement> log = scene.world()
				.createItemEntity(sawTop.add(0, 2, 0), util.vector().of(0, -0.1, 0), new ItemStack(Items.OAK_LOG, 4));

		scene.overlay().showText(60)
				.pointAt(sawSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("Take logs: whatever enters the saw is cut into the matching output");
		scene.idle(30);

		scene.world().modifyEntity(log, Entity::discard);
		scene.effects().indicateSuccess(sawPos);
		scene.world().createItemEntity(sawTop.add(0, 2, 0), util.vector().of(0, -0.1, 0),
				new ItemStack(Items.OAK_PLANKS, 6));
		scene.idle(50);

		scene.overlay().showText(60)
				.pointAt(sawSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("With enough steam it can process a whole stack in parallel");
		scene.idle(15);
		scene.world().createItemEntity(sawTop.add(0, 2, 0), util.vector().of(0, -0.1, 0),
				new ItemStack(Items.OAK_LOG, 32));
		scene.idle(50);

		scene.overlay().showText(60)
				.pointAt(sawSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("When steam runs out the saw stops immediately");
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(saw, SteamSawBlockEntity.class, (nbt) -> {
			nbt.put("Steam", FluidStack.EMPTY.writeToNBT(new CompoundTag()));
		});
		scene.world().setKineticSpeed(kinetics, 0);
		scene.idle(50);

		scene.markAsFinished();
	}
}