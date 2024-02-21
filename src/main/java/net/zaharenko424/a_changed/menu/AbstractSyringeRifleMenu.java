package net.zaharenko424.a_changed.menu;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.EmptyHandler;
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
        inventory = rifle.getCapability(Capabilities.ITEM_HANDLER).orElse(EmptyHandler.INSTANCE);

        createPlayerHotbar(playerInventory);
        createPlayerInventory(playerInventory);

        createRifleSlots();
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