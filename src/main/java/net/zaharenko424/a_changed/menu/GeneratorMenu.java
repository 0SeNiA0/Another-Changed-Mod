package net.zaharenko424.a_changed.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.GeneratorEntity;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;

public class GeneratorMenu extends AbstractMenu<GeneratorEntity> {

    public GeneratorMenu(int pContainerId, Inventory playerInventory, @NotNull FriendlyByteBuf buf) {
        super(AChanged.GENERATOR_MENU.get(), pContainerId, playerInventory, buf);
    }

    public GeneratorMenu(int pContainerId, Inventory playerInventory, @NotNull GeneratorEntity generatorEntity) {
        super(AChanged.GENERATOR_MENU.get(), pContainerId, playerInventory, generatorEntity);
    }

    @Override
    protected void createMenuSlots() {
        ItemStackHandler inv = entity.getInventory();
        Container container = new ItemHandlerContainer(inv);
        addSlot(new Slot(container, 0, 44, 36){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return CommonHooks.getBurnTime(stack, null) > 0;
            }
        });

        addSlot(new Slot(container, 1, 98, 36){
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getCapability(Capabilities.ENERGY).isPresent();
            }
        });
    }

    @Override
    @NotNull
    Block getBlock() {
        return BlockRegistry.GENERATOR.get();
    }
}