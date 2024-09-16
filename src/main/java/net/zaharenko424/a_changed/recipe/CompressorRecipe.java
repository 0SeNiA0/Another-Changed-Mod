package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class CompressorRecipe extends SingleInputRecipe {

    public CompressorRecipe(String group, Ingredient ingredient, ItemStack result, int energyConsumption, int processingTime) {
        super(group, ingredient, result, energyConsumption, processingTime);
    }

    @Override
    public @NotNull ItemStack getToastSymbol() {
        return ItemRegistry.COMPRESSOR_ITEM.toStack();
    }

    @Override
    public @NotNull RecipeSerializer<CompressorRecipe> getSerializer() {
        return RecipeRegistry.COMPRESSOR_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<CompressorRecipe> getType() {
        return RecipeRegistry.COMPRESSOR_RECIPE.get();
    }
}