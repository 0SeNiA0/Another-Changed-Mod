package net.zaharenko424.a_changed.capability.energy;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemEnergyCapability {

    @Contract("_, _, _ -> new")
    public static @NotNull ItemEnergyStorage getCapability(int capacity, int maxTransfer, ItemStack item){
        return new ItemEnergyStorage(capacity, maxTransfer, item);
    }

    public static class ItemEnergyStorage extends ExtendedEnergyStorage {

        private final ItemStack item;

        public ItemEnergyStorage(int capacity, ItemStack item) {
            super(capacity);
            this.item = item;
        }

        public ItemEnergyStorage(int capacity, int maxTransfer, ItemStack item) {
            super(capacity, maxTransfer);
            this.item = item;
        }

        public ItemEnergyStorage(int capacity, int maxReceive, int maxExtract, ItemStack item) {
            super(capacity, maxReceive, maxExtract);
            this.item = item;
        }

        public ItemEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, ItemStack item) {
            super(capacity, maxReceive, maxExtract, energy);
            this.item = item;
        }

        @Override
        public int receiveEnergy(int maxReceive, boolean simulate) {
            int received = super.receiveEnergy(maxReceive, simulate);
            if(!simulate) updateItemDamage();
            return received;
        }

        @Override
        public int extractEnergy(int maxExtract, boolean simulate) {
            int extracted = super.extractEnergy(maxExtract, simulate);
            if(!simulate) updateItemDamage();
            return extracted;
        }

        private void updateItemDamage(){
            item.setDamageValue(item.getMaxDamage() * (capacity - energy) / capacity);
        }
    }
}