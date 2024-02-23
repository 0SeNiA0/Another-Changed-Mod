package net.zaharenko424.a_changed.menu.machines;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.zaharenko424.a_changed.entity.block.machines.GeneratorEntity;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.registry.MenuRegistry;
import org.jetbrains.annotations.NotNull;

public class GeneratorMenu extends AbstractMachineMenu<GeneratorEntity> {

    public GeneratorMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        super(MenuRegistry.GENERATOR_MENU.get(), pContainerId, playerInventory, buf);
    }

    public GeneratorMenu(int pContainerId, Inventory playerInventory, @NotNull GeneratorEntity generatorEntity) {
        super(MenuRegistry.GENERATOR_MENU.get(), pContainerId, playerInventory, generatorEntity);
    }

    @Override
    protected void createMenuSlots() {
        ItemStackHandler inv = entity.getInventory();
        addSlot(new SlotItemHandler(inv, 0, 44, 36));
        addSlot(new SlotItemHandler(inv, 1, 98, 36));
    }

    @Override
    @NotNull
    Block getBlock() {
        return BlockRegistry.GENERATOR.get();
    }
}