package net.zaharenko424.a_changed.menu;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.zaharenko424.a_changed.item.AbstractSyringeRifle;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractSyringeRifleMenu extends AbstractMenu {

    protected final ItemStack rifle;
    protected final IItemHandler inventory;

    public AbstractSyringeRifleMenu(MenuType<?> menuType, int pContainerId, Inventory playerInventory) {
        this(menuType, pContainerId, playerInventory, playerInventory.player.getItemInHand(InteractionHand.MAIN_HAND));
    }

    public AbstractSyringeRifleMenu(MenuType<?> menuType, int pContainerId, Inventory playerInventory, @NotNull ItemStack rifle) {
        super(menuType, pContainerId);
        if(!(rifle.getItem() instanceof AbstractSyringeRifle)) throw new IllegalArgumentException("rifle must be an instance of AbstractSyringeRifle");
        this.rifle = rifle;
        inventory = rifle.getCapability(Capabilities.ItemHandler.ITEM);

        createPlayerHotbar(playerInventory);
        createPlayerInventory(playerInventory);

        createRifleSlots();
    }

    @Override
    public void clicked(int pSlotId, int button, @NotNull ClickType clickType, @NotNull Player player) {
        if(clickType == ClickType.SWAP){
            Inventory plInv = player.getInventory();
            if(plInv.getItem(button).equals(rifle) || plInv.getItem(pSlotId).equals(rifle)) return;
        }
        super.clicked(pSlotId, button, clickType, player);
    }

    @Override
    protected void createPlayerHotbar(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            if(playerInv.getItem(column).equals(rifle)) {
                addSlot(new Slot(playerInv, column, 8 + (column * 18), 142){
                    @Override
                    public boolean mayPickup(@NotNull Player pPlayer) {
                        return false;
                    }
                });
                continue;
            }
            addSlot(new Slot(playerInv, column, 8 + (column * 18), 142));
        }
    }

    protected abstract void createRifleSlots();

    public IItemHandler getInventory(){
        return inventory;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }
}