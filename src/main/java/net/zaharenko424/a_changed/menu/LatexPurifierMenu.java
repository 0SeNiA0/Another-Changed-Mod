package net.zaharenko424.a_changed.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.LatexPurifierEntity;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierMenu extends AbstractMenu<LatexPurifierEntity> {

    public LatexPurifierMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        super(AChanged.LATEX_PURIFIER_MENU.get(), pContainerId, playerInventory, buf);
    }

    public LatexPurifierMenu(int pContainerId, Inventory playerInventory, @NotNull LatexPurifierEntity entity) {
        super(AChanged.LATEX_PURIFIER_MENU.get(), pContainerId, playerInventory, entity);
    }

    @Override
    protected void createMenuSlots() {
        ItemStackHandler inv = entity.getInventory();
        Container container = new ItemHandlerContainer(inv);
        addSlot(new PowerCellSlot(container, 0, 56, 53));

        addSlot(new Slot(container, 1, 56, 17){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ItemRegistry.DARK_LATEX_ITEM.get()) || stack.is(ItemRegistry.WHITE_LATEX_ITEM.get());
            }
        });

        addSlot(new Slot(container, 2, 116, 35){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }
        });
    }

    @Override
    @NotNull Block getBlock() {
        return BlockRegistry.LATEX_PURIFIER.get();
    }
}