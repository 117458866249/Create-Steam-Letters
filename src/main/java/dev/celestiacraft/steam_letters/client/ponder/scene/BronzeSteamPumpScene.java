package dev.celestiacraft.steam_letters.client.ponder.scene;

import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import dev.celestiacraft.steam_letters.common.block.boiler.SolarBoilerBlockEntity;
import dev.celestiacraft.steam_letters.common.register.SlFluids;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * 青铜蒸汽泵 Ponder 场景.
 * <p>
 * 结构文件: {@code assets/steam_letters/ponder/bronze_steam_pump.nbt}
 */
public class BronzeSteamPumpScene {
	public static void usage(SceneBuilder builder, SceneBuildingUtil util) {
		CreateSceneBuilder scene = new CreateSceneBuilder(builder);

		scene.scaleSceneView(0.5F);
		scene.title("bronze_steam_pump", "青铜蒸汽泵");

		BlockPos engineIText = new BlockPos(1, 2, 1);
		BlockPos engineIText2 = new BlockPos(2, 2, 1);
		BlockPos pumpShowText = new BlockPos(2, 2, 2);
		BlockPos engineIIText = new BlockPos(3, 2, 3);
		BlockPos benefitText = new BlockPos(7, 4, 2);
		BlockPos whatCostText = new BlockPos(2, 4, 7);

		Selection engineI = util.select().fromTo(0, 1, 1, 3, 2, 1);
		Selection flyWheelI = util.select().position(0, 2, 1);
		Selection boilerI = util.select().position(3, 2, 1);
		Selection pumpShowSelection = util.select().fromTo(1, 1, 2, 3, 2, 2);
		Selection engineII = util.select().fromTo(0, 1, 3, 4, 2, 3);
		Selection flyWheelII = util.select().position(0, 2, 3);
		Selection boilerII = util.select().position(4, 2, 3);
		Selection benefitSelection = util.select().fromTo(6, 2, 1, 8, 5, 3);
		Selection whatCostSelection = util.select().fromTo(1, 2, 6, 3, 5, 8);

		scene.world().showSection(util.select().layer(0), Direction.UP);
		scene.idle(5);

		ElementLink<WorldSectionElement> engineILink = scene.world().showIndependentSection(engineI, Direction.EAST);
		scene.world().modifyBlockEntityNBT(flyWheelI, KineticBlockEntity.class, (nbt) -> {
			nbt.putFloat("Speed", -16.0F);
		});
		scene.world().modifyBlockEntityNBT(boilerI, SolarBoilerBlockEntity.class, (nbt) -> {
			FluidTank outputTank = new FluidTank(16000);
			outputTank.setFluid(new FluidStack(SlFluids.STEAM.get(), 20000));
			nbt.put("OutputTank", outputTank.writeToNBT(new CompoundTag()));
		});

		scene.idle(10);
		scene.overlay().showText(70)
				.pointAt(util.vector().centerOf(engineIText))
				.placeNearTarget()
				.attachKeyFrame()
				.text("如果将引擎和锅炉紧贴在一起, 将会降低效率");
		scene.idle(80);

		scene.overlay().showText(50)
				.pointAt(util.vector().centerOf(engineIText2))
				.placeNearTarget()
				.text("在这里外接一个动力泵又太麻烦, 该怎么做呢");
		scene.idle(55);

		scene.world().hideIndependentSection(engineILink, Direction.UP);
		scene.idle(10);

		ElementLink<WorldSectionElement> pumpShowLink = scene.world().showIndependentSection(pumpShowSelection, Direction.NORTH);
		scene.idle(10);

		scene.overlay().showText(70)
				.pointAt(util.vector().centerOf(pumpShowText))
				.placeNearTarget()
				.attachKeyFrame()
				.text("这时候就该青铜蒸汽泵出场了");
		scene.idle(80);

		scene.world().hideIndependentSection(pumpShowLink, Direction.WEST);
		scene.idle(10);

		scene.world().showSection(engineII, Direction.NORTH);
		scene.world().modifyBlockEntityNBT(flyWheelII, KineticBlockEntity.class, (nbt) -> {
			nbt.putFloat("Speed", -16.0F);
		});
		scene.world().modifyBlockEntityNBT(boilerII, SolarBoilerBlockEntity.class, (nbt) -> {
			FluidTank outputTank = new FluidTank(16000);
			outputTank.setFluid(new FluidStack(SlFluids.STEAM.get(), 20000));
			nbt.put("OutputTank", outputTank.writeToNBT(new CompoundTag()));
		});

		scene.idle(10);
		scene.overlay().showText(70)
				.pointAt(util.vector().centerOf(engineIIText))
				.placeNearTarget()
				.attachKeyFrame()
				.text("把它放在引擎和锅炉中间即可");
		scene.idle(80);

		ElementLink<WorldSectionElement> benefitShowLink = scene.world().showIndependentSection(benefitSelection, Direction.WEST);
		scene.idle(10);

		scene.overlay().showText(70)
				.pointAt(util.vector().centerOf(benefitText))
				.placeNearTarget()
				.attachKeyFrame()
				.text("它最大的好处是无需任何外接应力");
		scene.idle(80);

		scene.world().hideIndependentSection(benefitShowLink, Direction.EAST);

		ElementLink<WorldSectionElement> whatCostShowLink = scene.world().showIndependentSection(whatCostSelection, Direction.NORTH);
		scene.idle(10);

		scene.overlay().showText(70)
				.pointAt(util.vector().centerOf(whatCostText))
				.placeNearTarget()
				.attachKeyFrame()
				.text("那代价是什么呢? 除了蒸汽什么都运输不了");
		scene.idle(80);

		scene.world().hideIndependentSection(whatCostShowLink, Direction.SOUTH);
		scene.idle(20);

		scene.markAsFinished();
	}
}