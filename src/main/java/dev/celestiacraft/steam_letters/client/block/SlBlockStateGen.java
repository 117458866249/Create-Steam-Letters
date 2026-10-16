package dev.celestiacraft.steam_letters.client.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import dev.celestiacraft.steam_letters.common.block.burner.AbstractBurnerBlock;
import dev.celestiacraft.steam_letters.common.block.flywheel.SteamFlywheelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

/**
 * 新蒸汽设备的 blockstate 生成器.
 * <p>
 * 全部沿用项目既有约定: 旋转角直接用 {@code Direction#toYRot()}, 与 Create 的朝向解析一致.
 */
public final class SlBlockStateGen {
	private SlBlockStateGen() {
	}

	/**
	 * 单一变体方块: 只有一个模型文件, 与状态无关.
	 */
	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> simple(String model) {
		return (context, provider) -> provider.simpleBlock(context.get(), provider.models().getExistingFile(provider.modLoc(model)));
	}

	/**
	 * 只跟着水平朝向旋转的方块 (例如飞轮盘面).
	 */
	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalFacing(String model) {
		return horizontalFacing(BlockStateProperties.HORIZONTAL_FACING, model);
	}

	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> horizontalFacing(DirectionProperty facing, String model) {
		return (context, provider) -> {
			ModelFile file = provider.models().getExistingFile(provider.modLoc(model));

			provider.getVariantBuilder(context.get()).forAllStates(state -> ConfiguredModel.builder()
					.modelFile(file)
					.rotationY((int) state.getValue(facing).toYRot())
					.build());
		};
	}

	/**
	 * 飞轮: 外壳模型按配对方向切换, 三份模型分别是 {@code <basePath>_casing_none/_left/_right}.
	 */
	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> flywheel(String basePath) {
		return flywheel(basePath, 0);
	}

	/**
	 * 同上, 并额外叠加固定的 Y 轴旋转 (俯视顺时针为正).
	 */
	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> flywheel(String basePath, int yOffset) {
		return (context, provider) -> provider.getVariantBuilder(context.get()).forAllStates(state -> {
			String connection = state.getValue(SteamFlywheelBlock.CONNECTION).getSerializedName();
			ModelFile file = provider.models().getExistingFile(provider.modLoc(basePath + "_casing_" + connection));

			return ConfiguredModel.builder()
				.modelFile(file)
				.rotationY((int) state.getValue(SteamFlywheelBlock.HORIZONTAL_FACING).toYRot() + yOffset)
				.build();
		});
	}

	/**
	 * 带水平朝向与点燃标记的方块: 前脸在 off / lit 两份模型之间切换.
	 */
	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> facingLit(String offModel, String litModel) {
		return facingLit(AbstractBurnerBlock.FACING, AbstractBurnerBlock.LIT, offModel, litModel);
	}

	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> facingLit(DirectionProperty facing, BooleanProperty lit, String offModel, String litModel) {
		return facingLit(facing, lit, offModel, litModel, 0);
	}

	/**
	 * 同上, 并额外叠加固定的 Y 轴旋转 (俯视顺时针为正).
	 */
	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> facingLit(DirectionProperty facing, BooleanProperty lit, String offModel, String litModel, int yOffset) {
		return (context, provider) -> {
			ModelFile off = provider.models().getExistingFile(provider.modLoc(offModel));
			ModelFile litFile = provider.models().getExistingFile(provider.modLoc(litModel));

			provider.getVariantBuilder(context.get()).forAllStates(state -> ConfiguredModel.builder()
				.modelFile(state.getValue(lit) ? litFile : off)
				.rotationY((int) state.getValue(facing).toYRot() + yOffset)
				.build());
		};
	}
}
