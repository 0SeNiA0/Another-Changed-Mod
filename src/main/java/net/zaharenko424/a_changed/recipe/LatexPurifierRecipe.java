package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierRecipe extends FurnaceLikeRecipe<Container> {

    public LatexPurifierRecipe(Ingredient ingredient, ItemStack result) {
        super(ingredient, result);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<LatexPurifierRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final Codec<LatexPurifierRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
        ).apply(instance, LatexPurifierRecipe::new));

        @Override
        public @NotNull Codec<LatexPurifierRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull LatexPurifierRecipe fromNetwork(@NotNull FriendlyByteBuf friendlyByteBuf) {
            return new LatexPurifierRecipe(Ingredient.fromNetwork(friendlyByteBuf), friendlyByteBuf.readItem());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull LatexPurifierRecipe recipe) {
            recipe.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(recipe.result);
        }
    }

    public static class Type implements RecipeType<LatexPurifierRecipe> {
        public static final Type INSTANCE = new Type();
    }
}