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
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class DNAExtractorRecipe implements SlotAwareRecipe<Container> {

    private final Ingredient ingredient;
    private final ItemStack result;

    public DNAExtractorRecipe(Ingredient ingredient, ItemStack result){
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.DNA_EXTRACTOR_ITEM.get());
    }

    @Override
    public boolean matches(@NotNull Container container, int slot, @NotNull Level level) {
        if(level.isClientSide) return false;
        return test(container.getItem(slot));
    }

    private boolean test(ItemStack stack){
        if(stack == null) return false;
        for(ItemStack itemstack : ingredient.getItems()) {
            if (stack.getItem() == itemstack.getItem() && Objects.equals(stack.getTag(), itemstack.getTag()) && stack.areCapsCompatible(itemstack)) {
                return true;
            }
        }
        return false;
    }

    public @NotNull ItemStack assemble(@NotNull Container container, int slot) {
        container.removeItem(slot, ingredient.getItems()[0].getCount());
        return result.copy();
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
        public static final Codec<DNAExtractorRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
            ).apply(instance, DNAExtractorRecipe::new));

        @Override
        public @NotNull Codec<DNAExtractorRecipe> codec() {
            return CODEC;
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