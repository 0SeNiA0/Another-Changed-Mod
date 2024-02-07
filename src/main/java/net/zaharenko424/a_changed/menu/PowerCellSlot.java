package net.zaharenko424.a_changed.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import org.jetbrains.annotations.NotNull;

public class PowerCellSlot extends Slot {
    public PowerCellSlot(Container pContainer, int pSlot, int pX, int pY) {
        super(pContainer, pSlot, pX, pY);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getCapability(Capabilities.ENERGY).isPresent();
    }
}