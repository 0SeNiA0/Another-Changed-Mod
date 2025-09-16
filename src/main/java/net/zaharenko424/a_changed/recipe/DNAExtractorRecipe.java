package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorRecipe extends SingleInputRecipe {

    protected final Ingredient ingredient;
    protected final ItemStack result;

    public DNAExtractorRecipe(String group, Ingredient ingredient, ItemStack result, int energyConsumption, int processingTime){
        super(group, ingredient, result, energyConsumption, processingTime);
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return ItemRegistry.DNA_EXTRACTOR_ITEM.get().getDefaultInstance();
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.DNA_EXTRACTOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.DNA_EXTRACTOR_RECIPE.get();
    }
}