package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.celestiacraft.steam_letters.common.block.steam_hammer.SteamHammerBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import dev.celestiacraft.steam_letters.common.register.SlParticles;
import net.createmod.catnip.math.VecHelper;
import com.simibubi.create.foundation.ponder.element.BeltItemElement;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

/**
 * 蒸汽锻锤的 Ponder 场景.
 * <p>
 * 结构文件: {@code assets/steam_letters/ponder/steam_hammer.nbt}
 */
public class SteamHammerScene {
	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		RandomSource random = RandomSource.create();

		scene.title("steam_hammer", "蒸汽锻锤");
		scene.configureBasePlate(0, 0, 5);

		Selection base = util.select().layer(0)
				.substract(util.select().position(1, 0, 5));
		scene.world().showSection(base, Direction.UP);
		scene.idle(5);

		Selection belt = util.select().fromTo(0, 1, 2, 4, 1, 2);
		Selection depot = util.select().position(2, 1, 1);
		scene.world().showSection(belt.add(depot), Direction.DOWN);
		scene.idle(10);

		BlockPos hammerPos = util.grid().at(2, 3, 2);
		Selection hammer = util.select().position(hammerPos);
		scene.world().showSection(hammer, Direction.DOWN);
		scene.idle(10);

		Vec3 hammerSide = util.vector().blockSurface(hammerPos, Direction.WEST);

		scene.overlay().showText(70)
				.pointAt(hammerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽锻锤是一台压机, 会把传入的物品压制成型");
		scene.idle(80);

		Selection kinetics = util.select().fromTo(2, 1, 3, 2, 3, 3)
				.add(util.select().position(2, 1, 4))
				.add(util.select().position(2, 1, 5))
				.add(util.select().position(1, 0, 5));
		scene.world().showSection(kinetics, Direction.UP);
		scene.idle(10);
		scene.world().setKineticSpeed(kinetics, 32);
		scene.effects().rotationDirectionIndicator(hammerPos);
		scene.idle(20);

		scene.overlay().showText(60)
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.EAST))
				.colored(net.createmod.ponder.api.PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("它需要旋转动力才能运转");
		scene.idle(70);

		Selection tank = util.select().fromTo(1, 1, 0, 1, 2, 0);
		Selection pump = util.select().position(1, 3, 1);
		Selection pipes = util.select().position(1, 3, 0)
				.add(util.select().position(1, 3, 2));
		scene.world().showSection(tank, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(pipes.add(pump), Direction.NORTH);
		scene.idle(15);

		scene.overlay().showText(70)
				.pointAt(hammerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("它还需要蒸汽: 锻锤内部的蒸汽会在运转时被不断消耗");
		scene.idle(20);

		scene.world().modifyBlockEntityNBT(hammer, SteamHammerBlockEntity.class, (nbt) -> {
			nbt.put("Steam", new FluidStack(SlFluids.STEAM.get(), 1000).writeToNBT(new CompoundTag()));
		});
		for (int i = 0; i < 8; i++) {
			scene.effects().emitParticles(util.vector().topOf(hammerPos),
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(),
							VecHelper.offsetRandomly(Vec3.ZERO, random, 0.1f)), 1, 1);
		}
		scene.idle(60);

		ElementLink<BeltItemElement> item = scene.world()
				.createItemOnBelt(util.grid().at(1, 1, 2), Direction.SOUTH, new ItemStack(Items.COPPER_INGOT));
		scene.idle(25);
		scene.world().stallBeltItem(item, true);
		scene.idle(10);

		scene.overlay().showText(60)
				.pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.WEST))
				.placeNearTarget()
				.attachKeyFrame()
				.text("物品经过锤头下方时会被压成对应的成品");
		scene.idle(10);

		scene.effects().indicateSuccess(hammerPos);
		for (int i = 0; i < 6; i++) {
			scene.effects().emitParticles(util.vector().topOf(util.grid().at(2, 1, 2)),
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(),
							VecHelper.offsetRandomly(Vec3.ZERO, random, 0.15f)), 1, 1);
		}
		scene.idle(15);
		scene.world().removeItemsFromBelt(util.grid().at(2, 1, 2));
		scene.world().createItemOnBelt(util.grid().at(2, 1, 2), Direction.UP, AllItems.COPPER_SHEET.asStack());
		scene.idle(35);

		scene.overlay().showText(60)
				.pointAt(hammerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽充足时, 一次可以并行压制整叠物品");
		scene.idle(15);
		scene.world().createItemOnBelt(util.grid().at(0, 1, 2), Direction.SOUTH, new ItemStack(Items.COPPER_INGOT, 16));
		scene.idle(50);

		scene.overlay().showText(60)
				.pointAt(hammerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽耗尽后, 锻锤会立刻停机");
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(hammer, SteamHammerBlockEntity.class, (nbt) -> {
			nbt.put("Steam", FluidStack.EMPTY.writeToNBT(new CompoundTag()));
		});
		scene.world().setKineticSpeed(kinetics, 0);
		scene.idle(50);

		scene.markAsFinished();
	}
}