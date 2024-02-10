package net.zaharenko424.a_changed.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zaharenko424.a_changed.entity.block.machines.DNAExtractorEntity;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorMenu extends AbstractMenu<DNAExtractorEntity> {

    public DNAExtractorMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        super(MenuRegistry.DNA_EXTRACTOR_MENU.get(), pContainerId, playerInventory, buf);
    }

    public DNAExtractorMenu(int pContainerId, Inventory playerInventory, @NotNull DNAExtractorEntity entity) {
        super(MenuRegistry.DNA_EXTRACTOR_MENU.get(), pContainerId, playerInventory, entity);
    }

    @Override
    protected void createMenuSlots() {
        ItemStackHandler inv = entity.getInventory();
        addSlot(new SlotItemHandler(inv, 0, 82, 10));
        addSlot(new SlotItemHandler(inv, 1, 56, 36));
        addSlot(new SlotItemHandler(inv, 2, 108, 36));
        addSlot(new SlotItemHandler(inv, 3, 82, 62));
        addSlot(new SlotItemHandler(inv, 4, 144, 8));
        addSlot(new SlotItemHandler(inv, 5, 144, 26));
        addSlot(new SlotItemHandler(inv, 6, 144, 44));
        addSlot(new SlotItemHandler(inv, 7, 144, 62));
    }

    @Override
    @NotNull Block getBlock() {
        return BlockRegistry.DNA_EXTRACTOR.get();
    }
}