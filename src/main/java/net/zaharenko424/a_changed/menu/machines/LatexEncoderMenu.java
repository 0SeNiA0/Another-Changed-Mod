package net.zaharenko424.a_changed.menu.machines;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexEncoderMenu extends AbstractMachineMenu<LatexEncoderEntity> {

    public LatexEncoderMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        super(MenuRegistry.LATEX_ENCODER_MENU.get(), pContainerId, playerInventory, buf);
    }

    public LatexEncoderMenu(int pContainerId, Inventory playerInventory, @NotNull LatexEncoderEntity entity) {
        super(MenuRegistry.LATEX_ENCODER_MENU.get(), pContainerId, playerInventory, entity);
    }

    @Override
    protected void createMenuSlots() {
        ItemStackHandler inv = entity.getInventory();
        addSlot(new SlotItemHandler(inv, 0, 52, 47));
        addSlot(new SlotItemHandler(inv, 1, 52, 25));
        addSlot(new SlotItemHandler(inv, 2, 74, 10));
        addSlot(new SlotItemHandler(inv, 3, 94, 10));
        addSlot(new SlotItemHandler(inv, 4, 114, 10));
        addSlot(new SlotItemHandler(inv, 5, 81, 62));
        addSlot(new SlotItemHandler(inv, 6, 107, 62));
        addSlot(new SlotItemHandler(inv, 7, 140, 35));
    }

    @Override
    @NotNull Block getBlock() {
        return BlockRegistry.LATEX_ENCODER.get();
    }
}