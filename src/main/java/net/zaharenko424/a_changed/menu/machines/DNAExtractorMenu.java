package net.zaharenko424.a_changed.menu.machines;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.entity.block.machines.DNAExtractorEntity;
import net.zaharenko424.a_changed.menu.SlotItemHandler;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorMenu extends AbstractMachineMenu<DNAExtractorEntity> {

    public DNAExtractorMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        super(MenuRegistry.DNA_EXTRACTOR_MENU.get(), pContainerId, playerInventory, buf);
    }

    public DNAExtractorMenu(int pContainerId, Inventory playerInventory, @NotNull DNAExtractorEntity entity) {
        super(MenuRegistry.DNA_EXTRACTOR_MENU.get(), pContainerId, playerInventory, entity);
    }

    @Override
    protected void createMenuSlots() {
        ItemStackHandler inv = entity.getInventory();
        addSlot(new SlotItemHandler(inv, 0, 44, 35));
        addSlot(new SlotItemHandler(inv, 1, 80, 62));
        addSlot(new SlotItemHandler(inv, 2, 116, 35){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public boolean isFake() {
                return true;
            }
        });
        addSlot(new SlotItemHandler(inv, 3, 134, 35){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
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
        return BlockRegistry.DNA_EXTRACTOR.get();
    }
}