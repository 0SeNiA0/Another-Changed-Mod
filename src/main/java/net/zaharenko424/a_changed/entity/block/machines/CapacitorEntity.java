package net.zaharenko424.a_changed.entity.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.block.machines.Capacitor;
import net.zaharenko424.a_changed.capability.energy.EnergyStorageWrapper;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.menu.machines.CapacitorMenu;
import net.zaharenko424.a_changed.registry.BlockEntityRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CapacitorEntity extends AbstractMachineEntity<ItemStackHandler, ExtendedEnergyStorage> {

    private final EnergyStorageWrapper in = new EnergyStorageWrapper(energyStorage, energyStorage.getMaxReceive(), 0);
    private final EnergyStorageWrapper out = new EnergyStorageWrapper(energyStorage, 0, energyStorage.getMaxExtract());

    public CapacitorEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegistry.CAPACITOR_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    ItemStackHandler initInv() {
        return new ItemStackHandler(2){
            @Override
            public boolean isItemValid(int slot, @NotNull ItemStack stack) {
                return checkItemEnergyCap(stack);
            }

            @Override
            protected void onContentsChanged(int slot) {
                setChanged();
            }
        };
    }

    @Override
    ExtendedEnergyStorage initEnergy() {
        return new ExtendedEnergyStorage(60000, 256);
    }

    @Override
    public void tick() {
        boolean changed = false;
        int maxExtract = energyStorage.getMaxExtract();

        ItemStack stack = inventory.getStackInSlot(0);
        if(!stack.isEmpty()) {
            energyStorage.receiveEnergyFrom(stack.getCapability(Capabilities.EnergyStorage.ITEM), energyStorage.getMaxReceive(), false);
            changed = true;
        } else if(getEnergy() == 0) return;

        stack = inventory.getStackInSlot(1);
        if(!stack.isEmpty()) {
            energyStorage.transferEnergyTo(stack.getCapability(Capabilities.EnergyStorage.ITEM), maxExtract, false);
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
        return Component.translatable("container.a_changed.capacitor");
    }

    @Override
    protected <CT> CT getEnergyCap(@NotNull BlockCapability<CT, ?> cap, @Nullable Direction side) {
        if(getBlockState().getValue(Capacitor.FACING).getOpposite() == side) return (CT) out;
        if(side != null) return (CT) in;
        return super.getEnergyCap(cap, null);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new CapacitorMenu(pContainerId, pPlayerInventory, this);
    }
}