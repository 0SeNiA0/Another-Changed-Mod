package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class RecipeWrapper extends net.neoforged.neoforge.items.wrapper.RecipeWrapper {

    public RecipeWrapper(IItemHandler inv) {
        super(inv);
    }

    /**
     * @return a copy of a stack.
     */
    @Override
    public @NotNull ItemStack getItem(int slot) {
        return inv.getStackInSlot(slot).copy();
    }

    public void shrink(int slot, int amount){
        inv.extractItem(slot, amount, false);
    }
}