package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.menu.machines.GeneratorMenu;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GeneratorEntity extends AbstractMachineEntity<ItemStackHandler, ExtendedEnergyStorage> {

    private int burnTicks;
    private int maxBurnTicks;

    public GeneratorEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistry.GENERATOR_ENTITY.get(), pos, state);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(2){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return slot == 0 ? stack.getBurnTime(null) > 0 && !(stack.getItem() instanceof BucketItem)
                        : checkItemEnergyCap(stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    ExtendedEnergyStorage initEnergy() {
        return new ExtendedEnergyStorage(10000, 0, 64);
    }

    public boolean isEmpty(){
        return energyStorage.isEmpty();
    }

    public int getBurnTicks(){
        return burnTicks;
    }

    public int getMaxBurnTicks(){
        return maxBurnTicks;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new GeneratorMenu(pContainerId, playerInventory, this);
    }
//Generates 16/t
    public void tick(){
        boolean changed = false;

        if(burnTicks > 0){
            burnTicks--;
            if(!energyStorage.isFull()) energyStorage.addEnergy(16);
            changed = true;
        } else if(!energyStorage.isFull()) {
            ItemStack fuel = inventory.getStackInSlot(0);
            int burnTime = fuel.getBurnTime(null);
            if(burnTime > 0 && !(fuel.getItem() instanceof BucketItem)) {
                setActive(true);
                burnTicks = burnTime;
                maxBurnTicks = burnTime;
                inventory.extractItem(0, 1, false);
                changed = true;
            } else {
                setActive(false);
            }
        } else {
            setActive(false);
        }

        if(!energyStorage.isEmpty() && !inventory.getStackInSlot(1).isEmpty()){
            energyStorage.transferEnergyTo(inventory.getStackInSlot(1).getCapability(Capabilities.EnergyStorage.ITEM), 100, false);
            changed = true;
        }

        BlockEntity entity;
        BlockPos pos;
        for(Direction direction : Direction.values()){
            if(energyStorage.isEmpty()) break;
            pos = worldPosition.relative(direction);
            entity = level.getBlockEntity(pos);
            if(entity == null) continue;
            if(entity instanceof AbstractProxyWire wire){
                wire.tickNetwork();
                continue;
            }
            if(energyStorage.transferEnergyTo(
                    level.getCapability(Capabilities.EnergyStorage.BLOCK, pos, direction.getOpposite()),
                    energyStorage.getMaxExtract(), false) != 0) {
                if(entity instanceof AbstractMachineEntity<?, ?> machineEntity) machineEntity.update();
                changed = true;
            }
        }

        if(changed) update();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("container.a_changed.generator");
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        burnTicks = tag.getInt("burnTicks");
        maxBurnTicks = tag.getInt("maxBurnTicks");
    }

    void save(@NotNull CompoundTag tag){
        super.save(tag);
        if(burnTicks > 0) {
            tag.putInt("burnTicks", burnTicks);
            tag.putInt("maxBurnTicks", maxBurnTicks);
        }
    }
}