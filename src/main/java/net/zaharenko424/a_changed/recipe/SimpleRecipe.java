package net.zaharenko424.a_changed.recipe;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

public interface SimpleRecipe <C extends Container> extends Recipe<C> {

    default @NotNull ItemStack assemble(@NotNull C pContainer, @NotNull RegistryAccess pRegistryAccess) {
        return assemble(pContainer);
    }

    @NotNull ItemStack assemble(C container);

    default @NotNull ItemStack getResultItem(@NotNull RegistryAccess pRegistryAccess){
        return getResultItem();
    }

    @NotNull ItemStack getResultItem();
}