package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.celestiacraft.steam_letters.common.block.steam_mixer.SteamMixerBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.FluidStack;

/**
 * 蒸汽搅拌机的 Ponder 场景.
 * <p>
 * 结构文件: {@code assets/steam_letters/ponder/steam_mixer.nbt}
 * 等待重写
 */
public class SteamMixerScene {
	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);
		RandomSource random = RandomSource.create();

		scene.title("steam_mixer", "蒸汽搅拌机");
		scene.configureBasePlate(0, 0, 5);

		Selection base = util.select().layer(0)
				.substract(util.select().position(2, 0, 5));
		scene.world().showSection(base, Direction.UP);
		scene.idle(5);

		BlockPos basinPos = util.grid().at(1, 2, 2);
		BlockPos mixerPos = util.grid().at(1, 4, 2);

		scene.world().showSection(util.select().position(basinPos), Direction.DOWN);
		scene.idle(10);

		Selection mixer = util.select().position(mixerPos);
		scene.world().showSection(mixer, Direction.DOWN);
		scene.idle(10);

		Vec3 mixerSide = util.vector().blockSurface(mixerPos, Direction.EAST);

		scene.overlay().showText(70)
				.pointAt(mixerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽搅拌机需要架在青铜工作盆的正上方");
		scene.idle(80);

		Selection shaft = util.select().fromTo(1, 1, 3, 1, 4, 3);
		scene.world().showSection(shaft, Direction.UP);
		scene.idle(10);

		Selection kinetics = util.select().fromTo(3, 1, 2, 3, 1, 5)
				.add(util.select().position(2, 1, 4))
				.add(util.select().position(2, 0, 5));
		scene.world().showSection(kinetics, Direction.DOWN);
		scene.idle(10);
		scene.world().setKineticSpeed(kinetics.add(shaft), 32);
		scene.effects().rotationDirectionIndicator(util.grid().at(1, 3, 3));
		scene.idle(20);

		scene.overlay().showText(60)
				.pointAt(util.vector().blockSurface(util.grid().at(1, 3, 3), Direction.EAST))
				.colored(PonderPalette.BLUE)
				.placeNearTarget()
				.attachKeyFrame()
				.text("它需要旋转动力才能运转");
		scene.idle(70);

		Selection tank = util.select().fromTo(0, 1, 2, 0, 2, 2);
		Selection pump = util.select().position(0, 3, 2);
		Selection pipe = util.select().position(0, 4, 2);
		scene.world().showSection(tank, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(pump.add(pipe), Direction.NORTH);
		scene.idle(15);

		scene.overlay().showText(70)
				.pointAt(mixerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("它同样需要蒸汽, 运转时会不断消耗内部的蒸汽");
		scene.idle(20);

		scene.world().modifyBlockEntityNBT(mixer, SteamMixerBlockEntity.class, (nbt) -> {
			nbt.put("Steam", new FluidStack(SlFluids.STEAM.get(), 1000).writeToNBT(new CompoundTag()));
		});
		for (int i = 0; i < 8; i++) {
			scene.effects().emitParticles(util.vector().topOf(mixerPos),
					scene.effects().simpleParticleEmitter(SlParticles.STEAM.get(),
							VecHelper.offsetRandomly(Vec3.ZERO, random, 0.1f)), 1, 1);
		}
		scene.idle(60);

		Selection belt = util.select().fromTo(1, 1, 1, 3, 1, 1);
		scene.world().showSection(belt, Direction.DOWN);
		scene.idle(10);

		scene.overlay().showText(60)
				.pointAt(util.vector().blockSurface(basinPos, Direction.NORTH))
				.placeNearTarget()
				.attachKeyFrame()
				.text("投进工作盆的物品会被同时搅拌");
		scene.idle(15);

		scene.world().createItemOnBelt(util.grid().at(3, 1, 1), Direction.NORTH, new ItemStack(Items.RED_DYE, 4));
		scene.idle(15);
		scene.world().createItemOnBelt(util.grid().at(3, 1, 1), Direction.NORTH, new ItemStack(Items.BLUE_DYE, 4));
		scene.idle(15);
		scene.world().createItemOnBelt(util.grid().at(3, 1, 1), Direction.NORTH, new ItemStack(Items.GUNPOWDER, 4));
		scene.idle(30);

		scene.effects().indicateSuccess(basinPos);
		scene.idle(10);

		scene.overlay().showText(60)
				.pointAt(mixerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽充足时, 一次可以并行处理多份搅拌配方");
		scene.idle(15);
		scene.world().createItemOnBelt(util.grid().at(3, 1, 1), Direction.NORTH, new ItemStack(Items.RED_DYE, 8));
		scene.idle(45);

		scene.overlay().showText(60)
				.pointAt(mixerSide)
				.placeNearTarget()
				.attachKeyFrame()
				.text("蒸汽耗尽后, 搅拌机会立刻停机");
		scene.idle(10);
		scene.world().modifyBlockEntityNBT(mixer, SteamMixerBlockEntity.class, (nbt) -> {
			nbt.put("Steam", FluidStack.EMPTY.writeToNBT(new CompoundTag()));
		});
		scene.world().setKineticSpeed(kinetics.add(shaft), 0);
		scene.idle(50);

		scene.markAsFinished();
	}
}