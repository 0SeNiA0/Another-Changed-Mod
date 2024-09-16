package net.zaharenko424.a_changed.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class LatexEncoderRecipe implements Recipe<LatexEncoderRecipeWrapper> {

    protected final NonNullList<Ingredient> ingredients;
    protected final Gender gender;
    protected final ItemStack result;
    protected final int energyConsumption;
    protected final int processingTime;

    public LatexEncoderRecipe(@NotNull List<Ingredient> ingredients, Gender gender, ItemStack result, int energyConsumption, int processingTime){
        if(ingredients.size() != 7) throw
                new IllegalStateException("Wrong amount of ingredients in Latex Encoder recipe (" + ingredients.size() + "/7)");

        this.ingredients = ingredients instanceof NonNullList<Ingredient> nonNull ? nonNull : Utils.toNonNull(ingredients, Ingredient.EMPTY);
        this.gender = gender;
        this.result = result;
        this.energyConsumption = energyConsumption;
        this.processingTime = processingTime;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return new ItemStack(ItemRegistry.LATEX_ENCODER_ITEM.get());
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public boolean matches(@NotNull LatexEncoderRecipeWrapper input, @NotNull Level level) {
        if(this.gender != input.getGender()) return false;
        ItemStack item;
        Ingredient ingredient;
        for(int i = 0; i < input.size(); i++){
            item = input.getItem(i);
            ingredient = ingredients.get(i);
            if(item.isEmpty() && ingredient.isEmpty()) continue;
            if(!ingredient.test(item)) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull LatexEncoderRecipeWrapper input, HolderLookup.@NotNull Provider registries) {
        Ingredient ingredient;
        for(int i = 0; i < input.size(); i++){
            ingredient = ingredients.get(i);
            if(!ingredient.isEmpty()) input.shrink(i, ingredient.getItems()[0].getCount());
        }
        return result.copy();
    }

    public Gender getGender(){
        return gender;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.@NotNull Provider registries) {
        return result.copy();
    }

    public @NotNull ItemStack getResultItem(){
        return result.copy();
    }

    public int getEnergyConsumption() {
        return energyConsumption;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.LATEX_ENCODER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.LATEX_ENCODER_RECIPE.get();
    }

    public static class Serializer implements RecipeSerializer<LatexEncoderRecipe> {

        private final MapCodec<LatexEncoderRecipe> codec;
        private final StreamCodec<RegistryFriendlyByteBuf, LatexEncoderRecipe> streamCodec;

        public final int defaultEnergyConsumption;
        public final int defaultProcessingTime;

        public Serializer(int defaultEnergyConsumption, int defaultProcessingTime){
            codec = RecordCodecBuilder.mapCodec(builder -> builder.group(
                    Ingredient.CODEC.listOf().fieldOf("ingredients").forGetter(recipe -> recipe.ingredients),
                    Codec.STRING.fieldOf("gender").xmap(Gender::valueOf, Enum::toString).forGetter(recipe -> recipe.gender),
                    ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                    Codec.INT.fieldOf("energyConsumption").orElse(defaultEnergyConsumption).forGetter(recipe -> recipe.energyConsumption),
                    Codec.INT.fieldOf("processingTime").orElse(defaultProcessingTime).forGetter(recipe -> recipe.processingTime)
            ).apply(builder, LatexEncoderRecipe::new));

            streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec.codec());

            this.defaultEnergyConsumption = defaultEnergyConsumption;
            this.defaultProcessingTime = defaultProcessingTime;
        }

        @Override
        public @NotNull MapCodec<LatexEncoderRecipe> codec() {
            return codec;
        }

        @Override
        public @NotNull StreamCodec<RegistryFriendlyByteBuf, LatexEncoderRecipe> streamCodec() {
            return streamCodec;
        }
    }
}