package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorRecipe implements Recipe<RecipeWrapper> {

    protected final Ingredient ingredient;
    protected final ItemStack result;

    public DNAExtractorRecipe(Ingredient ingredient, ItemStack result){
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return ItemRegistry.DNA_EXTRACTOR_ITEM.get().getDefaultInstance();
    }

    @Override
    public boolean matches(@NotNull RecipeWrapper input, @NotNull Level level) {
        return matches(input, 0, level);
    }

    public boolean matches(@NotNull RecipeWrapper container, int slot, @NotNull Level level) {
        if(level.isClientSide) return false;
        ItemStack item = container.getItem(slot);
        return ingredient.test(item);
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull RecipeWrapper input, HolderLookup.@NotNull Provider registries) {
        return assemble(input, 0);
    }

    public @NotNull ItemStack assemble(@NotNull RecipeWrapper input, int slot) {
        input.shrink(slot, 1);
        return result.copy();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.DNA_EXTRACTOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.DNA_EXTRACTOR_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<DNAExtractorRecipe> {

        private final MapCodec<DNAExtractorRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, DNAExtractorRecipe> streamCodec;

        public Serializer(){
            codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    ItemStack.CODEC.fieldOf("result").forGetter(p_300827_ -> p_300827_.result)
            ).apply(builder, DNAExtractorRecipe::new));

            streamCodec = StreamCodec.composite(
                    Ingredient.CONTENTS_STREAM_CODEC,
                    recipe -> recipe.ingredient,
                    ItemStack.STREAM_CODEC,
                    recipe -> recipe.result,
                    DNAExtractorRecipe::new
            );
        }

        @Override
        public @NotNull MapCodec<DNAExtractorRecipe> codec() {
            return codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, DNAExtractorRecipe> streamCodec() {
            return streamCodec;
        }
    }
}