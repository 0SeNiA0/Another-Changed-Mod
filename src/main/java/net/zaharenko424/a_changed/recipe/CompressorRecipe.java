package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class CompressorRecipe implements SimpleRecipe<Container> {

    private final Ingredient ingredient;
    private final ItemStack result;

    public CompressorRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return ItemRegistry.COMPRESSOR_ITEM.get().getDefaultInstance();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container) {
        int amount = 0;
        ItemStack item = container.getItem(1);
        for(ItemStack stack : ingredient.getItems()){
            if(!item.equals(stack)) continue;
            amount = stack.getCount();
            break;
        }
        container.removeItem(1, amount);
        return result.copy();
    }

    @Override
    public @NotNull ItemStack getResultItem() {
        return result;
    }

    @Override
    public boolean matches(@NotNull Container container, @NotNull Level level) {
        if(level.isClientSide) return false;
        return Utils.test(container.getItem(1), ingredient);
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<CompressorRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final Codec<CompressorRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, CompressorRecipe::new));

        @Override
        public @NotNull Codec<CompressorRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull CompressorRecipe fromNetwork(@NotNull FriendlyByteBuf friendlyByteBuf) {
            return new CompressorRecipe(Ingredient.fromNetwork(friendlyByteBuf), friendlyByteBuf.readItem());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull CompressorRecipe recipe) {
            recipe.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(recipe.result);
        }
    }

    public static class Type implements RecipeType<CompressorRecipe> {
        public static final Type INSTANCE = new Type();
    }
}