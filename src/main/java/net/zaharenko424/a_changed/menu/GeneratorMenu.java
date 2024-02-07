package net.zaharenko424.a_changed.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.GeneratorEntity;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;

public class GeneratorMenu extends AbstractContainerMenu {

    private final GeneratorEntity generatorEntity;
    private final ContainerLevelAccess access;

    public GeneratorMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        this(pContainerId, playerInventory, (GeneratorEntity) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public GeneratorMenu(int pContainerId, Inventory playerInventory, @NotNull GeneratorEntity generatorEntity) {
        super(AChanged.GENERATOR_MENU.get(), pContainerId);
        this.generatorEntity = generatorEntity;
        access = ContainerLevelAccess.create(generatorEntity.getLevel(), generatorEntity.getBlockPos());

        createPlayerHotbar(playerInventory);
        createPlayerInventory(playerInventory);

        ItemStackHandler inv = generatorEntity.getInventory();
        Container container = new ItemHandlerContainer(inv);
        addSlot(new Slot(container, 0, 44, 36){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return super.mayPlace(stack) && CommonHooks.getBurnTime(stack, null) > 0;
            }
        });

        addSlot(new Slot(container, 1, 98, 36){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return super.mayPlace(stack) && stack.getCapability(Capabilities.ENERGY).isPresent();
            }
        });
    }

    public GeneratorEntity getGeneratorEntity(){
        return generatorEntity;
    }

    private void createPlayerHotbar(Inventory playerInv) {
        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, 8 + (column * 18), 142));
        }
    }

    private void createPlayerInventory(Inventory playerInv) {
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + column + (row * 9), 8 + (column * 18), 84 + (row * 18)));
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int pIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(access, player, BlockRegistry.GENERATOR.get());
    }
}