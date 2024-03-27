package net.zaharenko424.a_changed.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class PneumaticSyringeRifleMenu extends AbstractSyringeRifleMenu {

    public PneumaticSyringeRifleMenu(int pContainerId, Inventory playerInventory) {
        super(MenuRegistry.PNEUMATIC_SYRINGE_RIFLE_MENU.get(), pContainerId, playerInventory);
    }

    public PneumaticSyringeRifleMenu(int pContainerId, Inventory playerInventory, @NotNull ItemStack rifle) {
        super(MenuRegistry.PNEUMATIC_SYRINGE_RIFLE_MENU.get(), pContainerId, playerInventory, rifle);
    }

    protected void createRifleSlots() {
        addSlot(new SlotItemHandler(inventory, 0, 80, 33));
        addSlot(new SlotItemHandler(inventory, 1, 52, 33));
        addSlot(new SlotItemHandler(inventory, 2, 58, 11));
        addSlot(new SlotItemHandler(inventory, 3, 80, 5));
        addSlot(new SlotItemHandler(inventory, 4, 102, 11));
        addSlot(new SlotItemHandler(inventory, 5, 108, 33));
        addSlot(new SlotItemHandler(inventory, 6, 102, 55));
        addSlot(new SlotItemHandler(inventory, 7, 80, 61));
        addSlot(new SlotItemHandler(inventory, 8, 58, 55));
    }
}