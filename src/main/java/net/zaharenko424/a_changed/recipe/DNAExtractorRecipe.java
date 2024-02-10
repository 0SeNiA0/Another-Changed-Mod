package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorRecipe implements SlotAwareRecipe<Container> {

    private final Ingredient ingredient;
    private final ItemStack result;

    public DNAExtractorRecipe(Ingredient ingredient, ItemStack result){
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(@NotNull Container container, int slot, @NotNull Level level) {
        if(level.isClientSide) return false;
        return ingredient.test(container.getItem(slot));
    }

    public @NotNull ItemStack assemble() {
        return result.copy();
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
        return assemble();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    public @NotNull ItemStack getResultItem() {
        return result.copy();
    }

    @Override
    public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
        return getResultItem();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<DNAExtractorRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public @NotNull Codec<DNAExtractorRecipe> codec() {
            return RecordCodecBuilder.create(instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                    CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                    ).apply(instance, DNAExtractorRecipe::new));
        }

        @Override
        public @NotNull DNAExtractorRecipe fromNetwork(@NotNull FriendlyByteBuf friendlyByteBuf) {
            return new DNAExtractorRecipe(Ingredient.fromNetwork(friendlyByteBuf), friendlyByteBuf.readItem());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull DNAExtractorRecipe dnaExtractorRecipe) {
            dnaExtractorRecipe.ingredient.toNetwork(friendlyByteBuf);
            friendlyByteBuf.writeItem(dnaExtractorRecipe.result);
        }
    }

    public static class Type implements RecipeType<DNAExtractorRecipe> {
        public static final Type INSTANCE = new Type();
    }
}