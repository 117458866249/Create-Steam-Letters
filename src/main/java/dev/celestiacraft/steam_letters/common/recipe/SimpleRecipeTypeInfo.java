package dev.celestiacraft.steam_letters.common.recipe;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.function.Supplier;

/**
 * 一份配方注册的三视图: id, 配方类型与序列化器.
 * <p>
 * 一个配方要同时占用 RECIPE_TYPE 与 RECIPE_SERIALIZER 两个注册表, Registrate 的 RegistryEntry 只能承载其中一个,
 * 所以把两个 RegistryObject 收进同一个句柄, 由 {@link dev.celestiacraft.steam_letters.api.register.builder.RecipeTypeBuilder}
 * 产出. 本类本身就是 {@link IRecipeTypeInfo}, 可直接交给 ProcessingRecipe 的构造, 也可直接交给原版的 datagen 生成器.
 *
 * @param <R> 该类型下的配方实现
 */
@AllArgsConstructor
public class SimpleRecipeTypeInfo<R extends Recipe<?>> implements IRecipeTypeInfo {
	@Getter
	private final ResourceLocation id;
	private final Supplier<? extends RecipeType<R>> type;
	private final Supplier<? extends RecipeSerializer<R>> serializer;

	/**
	 * 类型化取用, 与 Registrate 的 RegistryEntry 同名; 免去调用处落在 {@link #getType()} 的泛型推断上.
	 */
	public RecipeType<R> get() {
		return type.get();
	}

	@Override
	public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializer.get();
	}

	@Override
	public <T extends RecipeType<?>> T getType() {
		return (T) type.get();
	}

	/**
	 * 需要 {@link IRecipeTypeInfo} 的地方(ProcessingRecipe 的构造)显式取用, 免得读代码时还要确认本类实现了它.
	 */
	public IRecipeTypeInfo getInfo() {
		return this;
	}
}