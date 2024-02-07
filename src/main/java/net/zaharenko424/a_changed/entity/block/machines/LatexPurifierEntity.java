package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.energy.EmptyEnergyStorage;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.capability.EnergyConsumer;
import net.zaharenko424.a_changed.menu.LatexPurifierMenu;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexPurifierEntity extends AbstractMachineEntity<EnergyConsumer> {

    public static final int MAX_PROGRESS = 160;
    private int progress;

    public LatexPurifierEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.LATEX_PURIFIER_ENTITY.get(), pPos, pBlockState, new ItemStackHandler(3){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot == 0 ? stack.is(ItemRegistry.DARK_LATEX_ITEM.get()) || stack.is(ItemRegistry.WHITE_LATEX_ITEM.get())
                        : stack.is(ItemRegistry.DARK_LATEX_BASE.get()) || stack.is(ItemRegistry.WHITE_LATEX_BASE.get());
            }
        }, new EnergyConsumer(25000, 256, 0));
    }

    public int getProgress(){
        return progress;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new LatexPurifierMenu(pContainerId, playerInventory, this);
    }
//Eats 48/t
    public void tick(){
        boolean changed = false;

        if(!inventory.getStackInSlot(0).isEmpty()){
            changed = energyStorage.receiveEnergyFrom(inventory.getStackInSlot(0).getCapability(Capabilities.ENERGY)
                    .orElse(EmptyEnergyStorage.INSTANCE), energyStorage.getMaxReceive(), false) != 0;
        }

        if(getEnergy() > 48) {
            if (inventory.getStackInSlot(1).isEmpty() || !isSameLatex()) {
                if (progress > 0) {
                    progress = 0;
                    setActive(false);
                }
            } else if (progress < MAX_PROGRESS) {
                setActive(true);
                energyStorage.consumeEnergy(48);
                progress++;
                changed = true;
            } else if (progress == MAX_PROGRESS) {
                progress = 0;
                inventory.insertItem(2, new ItemStack(inventory.extractItem(1, 1, false)
                        .is(ItemRegistry.DARK_LATEX_ITEM.get()) ? ItemRegistry.DARK_LATEX_BASE.get()
                        : ItemRegistry.WHITE_LATEX_BASE.get()), false);
                changed = true;
            }
        }

        if(changed) update();
    }

    private boolean isSameLatex(){
        ItemStack stack0 = inventory.getStackInSlot(1);
        ItemStack stack1 = inventory.getStackInSlot(2);
        if(stack1.isEmpty()) return true;
        return stack0.is(ItemRegistry.DARK_LATEX_ITEM.get()) && stack1.is(ItemRegistry.DARK_LATEX_BASE.get())
                || stack0.is(ItemRegistry.WHITE_LATEX_ITEM.get()) && stack1.is(ItemRegistry.WHITE_LATEX_BASE.get());
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.latex_purifier");
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        progress = tag.getInt("progress");
    }

    @Override
    void save(@NotNull CompoundTag tag) {
        super.save(tag);
        if(progress > 0) tag.putInt("progress", progress);
    }
}