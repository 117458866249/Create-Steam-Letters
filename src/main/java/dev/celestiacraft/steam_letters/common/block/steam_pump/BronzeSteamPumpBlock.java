package dev.celestiacraft.steam_letters.common.block.steam_pump;

import com.simibubi.create.content.fluids.pump.PumpBlock;
import com.simibubi.create.content.fluids.pump.PumpBlockEntity;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import dev.celestiacraft.steam_letters.common.register.SlBlockEntities;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import org.jetbrains.annotations.NotNull;

public class BronzeSteamPumpBlock extends PumpBlock {
	public BronzeSteamPumpBlock(Properties properties) {
		super(properties.sound(SoundType.LANTERN)
				.noOcclusion());
	}

	@Override
	public @NotNull BlockEntityType<? extends PumpBlockEntity> getBlockEntityType() {
		return SlBlockEntities.BRONZE_STEAM_PUMP.get();
	}

	public static <T extends Block> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> genBlockState() {
		return (context, provider) -> {
			ModelFile model = provider.models().getExistingFile(provider.modLoc("block/bronze_pump/block"));

			provider.getVariantBuilder(context.get()).forAllStates((state) -> {
				Direction direction = state.getValue(FACING);
				ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(model);

				if (direction == Direction.DOWN) {
					builder.rotationX(180);
				} else if (direction != Direction.UP) {
					builder.rotationX(90);
					builder.rotationY(((int) direction.toYRot() + 180) % 360);
				}

				return builder.build();
			});
		};
	}
}