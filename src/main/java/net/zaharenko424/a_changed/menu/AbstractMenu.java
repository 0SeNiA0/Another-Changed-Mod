package net.zaharenko424.a_changed.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractMenu <T extends BlockEntity> extends AbstractContainerMenu {

    protected final T entity;
    protected final ContainerLevelAccess access;

    public AbstractMenu(MenuType<?> menuType, int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        this(menuType, pContainerId, playerInventory, (T) Objects.requireNonNull(playerInventory.player.level().getBlockEntity(buf.readBlockPos())));
    }

    public AbstractMenu(MenuType<?> menuType, int pContainerId, Inventory playerInventory, @NotNull T entity) {
        super(menuType, pContainerId);
        this.entity = entity;
        access = ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos());

        createPlayerHotbar(playerInventory);
        createPlayerInventory(playerInventory);

        createMenuSlots();
    }

    protected abstract void createMenuSlots();

    protected void createPlayerHotbar(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, 8 + (column * 18), 142));
        }
    }

    protected void createPlayerInventory(Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + column + (row * 9), 8 + (column * 18), 84 + (row * 18)));
            }
        }
    }

    public T getEntity(){
        return entity;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 36) {
                if (!this.moveItemStackTo(itemstack1, 36, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 36, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

    abstract @NotNull Block getBlock();

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, getBlock());
    }
}