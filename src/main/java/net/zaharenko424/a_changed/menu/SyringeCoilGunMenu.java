package net.zaharenko424.a_changed.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class SyringeCoilGunMenu extends AbstractSyringeRifleMenu {

    public SyringeCoilGunMenu(int pContainerId, Inventory playerInventory) {
        super(MenuRegistry.SYRINGE_COIL_GUN_MENU.get(), pContainerId, playerInventory);
    }

    public SyringeCoilGunMenu(int pContainerId, Inventory playerInventory, @NotNull ItemStack rifle) {
        super(MenuRegistry.SYRINGE_COIL_GUN_MENU.get(), pContainerId, playerInventory, rifle);
    }

    @Override
    protected void createRifleSlots() {
        addSlot(new SlotItemHandler(inventory, 0, 63, 16));
        addSlot(new SlotItemHandler(inventory, 1, 98, 16));
        addSlot(new SlotItemHandler(inventory, 2, 98, 50));
        addSlot(new SlotItemHandler(inventory, 3, 63, 50));
    }
}