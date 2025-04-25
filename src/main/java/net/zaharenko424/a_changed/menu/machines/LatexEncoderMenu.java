package net.zaharenko424.a_changed.menu.machines;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.entity.block.machine.LatexEncoderEntity;
import net.zaharenko424.a_changed.menu.SlotItemHandler;
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
        addSlot(new SlotItemHandler(inv, 0, 38, 47));
        addSlot(new SlotItemHandler(inv, 1, 38, 25));
        addSlot(new SlotItemHandler(inv, 2, 60, 10));
        addSlot(new SlotItemHandler(inv, 3, 80, 10));
        addSlot(new SlotItemHandler(inv, 4, 100, 10));
        addSlot(new SlotItemHandler(inv, 5, 67, 62));
        addSlot(new SlotItemHandler(inv, 6, 93, 62));
        addSlot(new SlotItemHandler(inv, 7, 122, 35){
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }

            @Override
            public boolean isFake() {
                return true;
            }
        });
    }

    @Override
    @NotNull Block getBlock() {
        return BlockRegistry.LATEX_ENCODER.get();
    }
}