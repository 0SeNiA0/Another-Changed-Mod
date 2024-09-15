package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class SingleInputRecipeWrapper extends RecipeWrapper {

    protected final int slot;

    public SingleInputRecipeWrapper(IItemHandler inv, int slot) {
        super(inv);
        this.slot = slot;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public @NotNull ItemStack getItem(int slot) {
        return item();
    }

    public ItemStack item(){
        return inv.getStackInSlot(slot);
    }

    @Override
    public void shrink(int slot, int amount) {
        shrink(amount);
    }

    public void shrink(int amount){
        inv.extractItem(slot, amount, false);
    }
}