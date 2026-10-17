package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.celestiacraft.steam_letters.common.block.steam_hammer.SteamHammerBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
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
	private static final int STEAM_INJECTED = 10000;

	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.title("steam_hammer", "Steam Hammer");
		scene.configureBasePlate(0, 0, 5);
		scene.showBasePlate();

		BlockPos hammerPos = util.grid().at(2, 3, 2);
		BlockPos depotPos = util.grid().at(2, 1, 1);
		BlockPos drivePos = util.grid().at(2, 1, 3);

		Selection beltLine = util.select().fromTo(0, 1, 2, 4, 1, 2);
		Selection inputEnd = util.select().fromTo(0, 1, 3, 0, 2, 3);
		Selection driveTrain = util.select().fromTo(2, 1, 3, 2, 3, 5);

		scene.world().showSection(util.select().layer(0), Direction.UP);
		scene.idle(5);
		scene.world().showSection(beltLine, Direction.NORTH);
		scene.idle(5);
		scene.world().showSection(inputEnd, Direction.NORTH);
		scene.idle(5);
		scene.world().showSection(driveTrain, Direction.DOWN);
		scene.idle(5);
		scene.world().showSection(util.select().position(depotPos), Direction.DOWN);
		scene.world().showSection(util.select().position(hammerPos), Direction.DOWN);
		scene.idle(15);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("The Steam Hammer is a steam-powered version of the Mechanical Press")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.NORTH))
				.placeNearTarget();
		scene.idle(80);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("Just like the press, it runs on rotational force")
				.pointAt(util.vector().blockSurface(drivePos, Direction.NORTH))
				.placeNearTarget();
		scene.idle(40);

		scene.world().setKineticSpeed(driveTrain, 32);
		scene.effects().rotationSpeedIndicator(drivePos);
		scene.idle(45);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("But rotation alone won't do - it also needs steam")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.NORTH))
				.placeNearTarget();
		scene.idle(60);

		Vec3 steamOrigin = util.vector().topOf(hammerPos).add(0, 0.375, 0);
		scene.effects().emitParticles(
				steamOrigin,
				scene.effects().simpleParticleEmitter(ParticleTypes.CLOUD, Vec3.ZERO),
				1.0F,
				24
		);
		scene.world().modifyBlockEntity(hammerPos, SteamHammerBlockEntity.class, (entity) -> {
			entity.setSteamStack(new FluidStack(SlFluids.STEAM.getSource(), STEAM_INJECTED));
		});
		scene.idle(25);
		scene.effects().indicateSuccess(hammerPos);
		scene.idle(30);

		scene.world().createItemOnBeltLike(depotPos, Direction.UP, new ItemStack(Items.IRON_INGOT, 64));
		scene.idle(25);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("A single strike processes the whole stack - up to 64 items at once")
				.pointAt(util.vector().topOf(depotPos))
				.placeNearTarget();
		scene.idle(90);

		scene.overlay().showText(80)
				.attachKeyFrame()
				.text("Each cycle consumes 1000 mB of steam and stops once dry")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.WEST))
				.placeNearTarget();
		scene.idle(90);

		scene.overlay().showText(70)
				.attachKeyFrame()
				.text("Feed it steam from any side with pipes and it keeps pressing")
				.pointAt(util.vector().blockSurface(hammerPos, Direction.UP))
				.placeNearTarget();
		scene.idle(80);

		scene.markAsFinished();
	}
}
