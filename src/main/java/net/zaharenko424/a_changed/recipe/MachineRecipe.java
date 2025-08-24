package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;

public interface MachineRecipe <T extends RecipeInput> extends Recipe<T> {

    int getEnergyConsumption();

    int getProcessingTime();
}
