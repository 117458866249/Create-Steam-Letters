package dev.celestiacraft.steam_letters.common.register;

import dev.celestiacraft.steam_letters.CreateLettersSteam;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SlParticles {
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES;

	public static final RegistryObject<SimpleParticleType> STEAM;

	static {
		PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, CreateLettersSteam.MODID);

		STEAM = PARTICLE_TYPES.register("steam", () -> {
			return new SimpleParticleType(false);
		});
	}

	public static void init(IEventBus bus) {
		PARTICLE_TYPES.register(bus);
	}
}