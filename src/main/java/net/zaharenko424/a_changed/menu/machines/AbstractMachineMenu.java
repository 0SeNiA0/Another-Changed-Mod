package net.zaharenko424.a_changed.menu.machines;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.zaharenko424.a_changed.menu.AbstractMenu;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class AbstractMachineMenu<T extends BlockEntity> extends AbstractMenu {

    protected final T entity;
    protected final ContainerLevelAccess access;

    public AbstractMachineMenu(MenuType<?> menuType, int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        this(menuType, pContainerId, playerInventory, (T) Objects.requireNonNull(playerInventory.player.level().getBlockEntity(buf.readBlockPos())));
    }

    public AbstractMachineMenu(MenuType<?> menuType, int pContainerId, Inventory playerInventory, @NotNull T entity) {
        super(menuType, pContainerId);
        this.entity = entity;
        access = ContainerLevelAccess.create(entity.getLevel(), entity.getBlockPos());

        createPlayerHotbar(playerInventory);
        createPlayerInventory(playerInventory);

        createMenuSlots();
    }

    protected abstract void createMenuSlots();

    public T getEntity(){
        return entity;
    }

    abstract @NotNull Block getBlock();

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, getBlock());
    }
}