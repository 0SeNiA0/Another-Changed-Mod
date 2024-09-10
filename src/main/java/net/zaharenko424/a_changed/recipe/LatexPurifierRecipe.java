package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.zaharenko424.a_changed.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierRecipe extends SingleInputRecipe {

    public LatexPurifierRecipe(String group, Ingredient ingredient, ItemStack result, int energyConsumption, int processingTime) {
        super(group, ingredient, result, energyConsumption, processingTime);
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.LATEX_PURIFIER_RECIPE_SERIALIZER.get();
    }

    @Override
    public @NotNull RecipeType<?> getType() {
        return RecipeRegistry.LATEX_PURIFIER_RECIPE.get();
    }
}