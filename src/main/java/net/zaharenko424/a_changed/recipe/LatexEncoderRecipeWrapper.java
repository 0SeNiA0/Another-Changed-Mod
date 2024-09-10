package net.zaharenko424.a_changed.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class LatexEncoderRecipeWrapper implements RecipeInput {

    private final IItemHandler inv;
    private final LatexEncoderEntity encoder;

    public LatexEncoderRecipeWrapper(IItemHandler inv, LatexEncoderEntity encoder) {
        this.inv = inv;
        this.encoder = encoder;
    }

    @Override
    public @NotNull ItemStack getItem(int index) {
        return inv.getStackInSlot(index).copy();
    }

    public void shrink(int slot, int amount){
        inv.extractItem(slot, amount, false);
    }

    public Gender getGender(){
        return encoder.getSelectedGender();
    }

    @Override
    public int size() {
        return inv.getSlots();
    }
}