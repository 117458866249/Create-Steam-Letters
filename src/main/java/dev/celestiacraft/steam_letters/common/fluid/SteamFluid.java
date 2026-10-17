package dev.celestiacraft.steam_letters.common.fluid;

import com.tterrag.registrate.builders.FluidBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fluids.FluidType;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SteamFluid extends FluidType {
	private Vector3f fogColor;
	private Supplier<Float> fogDistance;
	private final ResourceLocation stillTexture;
	private final ResourceLocation flowingTexture;
	private final int tintColor;
	private Object clientExtensions;

	public SteamFluid(Properties properties, ResourceLocation stillTexture, ResourceLocation flowingTexture, int tintColor) {
		super(properties.density(-10)
				.viscosity(1)
				.canPushEntity(false)
				.canConvertToSource(false)
				.canDrown(true)
				.temperature(114514));
		this.stillTexture = stillTexture;
		this.flowingTexture = flowingTexture;
		this.tintColor = tintColor;
	}

	public static FluidBuilder.FluidTypeFactory create(int fogColor, Supplier<Float> fogDistance, int tintColor) {
		return (properties, still, flowing) -> {
			SteamFluid fluidType = new SteamFluid(properties, still, flowing, tintColor);
			fluidType.fogColor = new Vector3f(
					((fogColor >> 16) & 0xFF) / 255.0f,
					((fogColor >> 8) & 0xFF) / 255.0f,
					(fogColor & 0xFF) / 255.0f);
			fluidType.fogDistance = fogDistance;
			return fluidType;
		};
	}

	@Override
	public Object getRenderPropertiesInternal() {
		if (clientExtensions == null && stillTexture != null) {
			clientExtensions = new SteamFluidClient(stillTexture, flowingTexture, tintColor, fogColor, fogDistance);
		}
		return clientExtensions;
	}
}