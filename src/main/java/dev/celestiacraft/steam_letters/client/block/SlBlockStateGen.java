package dev.celestiacraft.steam_letters.client.block;

import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

import dev.celestiacraft.steam_letters.common.block.burner.AbstractBurnerBlock;
import net.minecraft.world.level.block.Block;
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
	 * 带水平朝向与点燃标记的方块: 前脸在 off / lit 两份模型之间切换.
	 */
	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> facingLit(String offModel, String litModel) {
		return (context, provider) -> {
			ModelFile off = provider.models().getExistingFile(provider.modLoc(offModel));
			ModelFile lit = provider.models().getExistingFile(provider.modLoc(litModel));

			provider.getVariantBuilder(context.get()).forAllStates(state -> ConfiguredModel.builder()
					.modelFile(state.getValue(AbstractBurnerBlock.LIT) ? lit : off)
					.rotationY((int) state.getValue(AbstractBurnerBlock.FACING).toYRot())
					.build());
		};
	}
}
