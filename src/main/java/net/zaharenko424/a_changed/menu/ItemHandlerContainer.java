package net.zaharenko424.a_changed.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class ItemHandlerContainer implements Container {

    private final IItemHandlerModifiable handler;

    public ItemHandlerContainer(IItemHandlerModifiable handler){
        this.handler = handler;
    }

    @Override
    public int getContainerSize() {
        return handler.getSlots();
    }

    @Override
    public boolean isEmpty() {
        for(int i = 0; i < handler.getSlots(); i++){
            if(!handler.getStackInSlot(i).isEmpty()) return false;
        }
        return true;
    }

    @Override
    public @NotNull ItemStack getItem(int pSlot) {
        return handler.getStackInSlot(pSlot).copy();
    }

    @Override
    public @NotNull ItemStack removeItem(int pSlot, int pAmount) {
        return handler.extractItem(pSlot, pAmount, false);
    }

    @Override
    public @NotNull ItemStack removeItemNoUpdate(int pSlot) {
        return handler.extractItem(pSlot, handler.getStackInSlot(pSlot).getCount(), false);
    }

    @Override
    public void setItem(int pSlot, @NotNull ItemStack pStack) {
        handler.setStackInSlot(pSlot, pStack);
    }

    @Override
    public void setChanged() {}

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        for(int i = 0; i < handler.getSlots(); i++){
            handler.setStackInSlot(i, ItemStack.EMPTY);
        }
    }
}