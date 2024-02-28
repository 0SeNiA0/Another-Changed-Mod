package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LatexEncoderRecipe implements SimpleRecipe<Container> {

    private final NonNullList<PartialNBTIngredientFix> ingredients;
    private final NonNullList<Ingredient> ingredients0;
    private final Gender gender;
    private final ItemStack result;

    public LatexEncoderRecipe(@NotNull NonNullList<PartialNBTIngredientFix> ingredients, Gender gender, ItemStack result){
        this.ingredients = ingredients;
        if(ingredients.size() != 7) throw
                new IllegalStateException("Wrong amount of ingredients in Latex Encoder recipe (" + ingredients.size() + "/7)");
        ingredients0 = Utils.toNonNull(List.copyOf(ingredients), PartialNBTIngredientFix.EMPTY);
        this.gender = gender;
        this.result = result;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.LATEX_ENCODER_ITEM.get());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients0;
    }

    @Override
    public boolean matches(@NotNull Container pContainer, @NotNull Level pLevel) {
        return matches(pContainer, Gender.NONE);
    }

    public boolean matches(@NotNull Container container, Gender gender){
        if(this.gender != gender) return false;
        ItemStack item;
        Ingredient ingredient;
        for(int i = 0; i < 7; i++){
            item = container.getItem(i);
            ingredient = ingredients.get(i);
            if(item.isEmpty() && ingredient.isEmpty()) continue;
            if(!ingredient.test(item)) return false;
        }
        return true;
    }

    public Gender getGender(){
        return gender;
    }

    @Override
    public @NotNull ItemStack assemble(Container pContainer) {
        Ingredient ingredient;
        for(int i = 0; i < 7; i++){
            ingredient = ingredients.get(i);
            if(!ingredient.isEmpty()) pContainer.removeItem(i, ingredient.getItems()[0].getCount());
        }
        return result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    public @NotNull ItemStack getResultItem(){
        return result.copy();
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return Serializer.INSTANCE;
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return Type.INSTANCE;
    }

    public static class Serializer implements RecipeSerializer<LatexEncoderRecipe> {

        public static final Serializer INSTANCE = new Serializer();
        public static final Codec<LatexEncoderRecipe> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                PartialNBTIngredientFix.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                Codec.STRING.fieldOf("gender").forGetter(recipe -> recipe.gender.toString()),
                ItemStack.CODEC.fieldOf("result").forGetter(recipe-> recipe.result)
            ).apply(instance, (ingredients, str, result) -> new LatexEncoderRecipe(Utils.toNonNull(ingredients, PartialNBTIngredientFix.EMPTY),
                Gender.valueOf(str), result)));

        @Override
        public @NotNull Codec<LatexEncoderRecipe> codec() {
            return CODEC;
        }

        @Override
        public @NotNull LatexEncoderRecipe fromNetwork(@NotNull FriendlyByteBuf pBuffer) {
            return new LatexEncoderRecipe(Utils.toNonNull(pBuffer.readList(PartialNBTIngredientFix::fromNetwork), PartialNBTIngredientFix.EMPTY),
                    pBuffer.readEnum(Gender.class), pBuffer.readItem());
        }

        @Override
        public void toNetwork(@NotNull FriendlyByteBuf pBuffer, @NotNull LatexEncoderRecipe pRecipe) {
            pBuffer.writeCollection(pRecipe.ingredients, (buf, ingredient) -> ingredient.toNetwork0(buf));
            pBuffer.writeEnum(pRecipe.gender);
            pBuffer.writeItem(pRecipe.result);
        }
    }

    public static class Type implements RecipeType<LatexEncoderRecipe> {
        public static final Type INSTANCE = new Type();
    }
}