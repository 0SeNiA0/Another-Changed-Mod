package net.zaharenko424.a_changed.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zaharenko424.a_changed.entity.block.machines.LatexPurifierEntity;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierMenu extends AbstractMenu<LatexPurifierEntity> {

    public LatexPurifierMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        super(MenuRegistry.LATEX_PURIFIER_MENU.get(), pContainerId, playerInventory, buf);
    }

    public LatexPurifierMenu(int pContainerId, Inventory playerInventory, @NotNull LatexPurifierEntity entity) {
        super(MenuRegistry.LATEX_PURIFIER_MENU.get(), pContainerId, playerInventory, entity);
    }

    @Override
    protected void createMenuSlots() {
        ItemStackHandler inv = entity.getInventory();
        addSlot(new SlotItemHandler(inv, 0, 56, 53));
        addSlot(new SlotItemHandler(inv, 1, 56, 17));
        addSlot(new SlotItemHandler(inv, 2, 116, 35));
    }

    @Override
    @NotNull Block getBlock() {
        return BlockRegistry.LATEX_PURIFIER.get();
    }
}