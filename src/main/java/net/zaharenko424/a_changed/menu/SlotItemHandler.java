package net.zaharenko424.a_changed.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class SlotItemHandler extends net.neoforged.neoforge.items.SlotItemHandler {

    public SlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPickup(@NotNull Player playerIn) {
        return getItem().isEmpty() || !getItemHandler().extractItem(index, 1, true).isEmpty();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.isEmpty() || getItemHandler().isItemValid(index, stack);
    }
}