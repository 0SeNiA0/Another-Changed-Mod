package net.zaharenko424.a_changed.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilitySerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemEnergyCapability {

    public static final ResourceLocation KEY = AChanged.resourceLoc("item_energy_storage");

    public static class Provider implements ICapabilitySerializable<Tag> {

        ExtendedEnergyStorage storage;
        LazyOptional<ExtendedEnergyStorage> optional;

        public Provider(int capacity, ItemStack item) {
            storage = new ItemEnergyStorage(capacity, item);
            optional = LazyOptional.of(()-> storage);
        }

        public Provider(int capacity, int maxTransfer, ItemStack item) {
            storage = new ItemEnergyStorage(capacity, maxTransfer, item);
            optional = LazyOptional.of(()-> storage);
        }

        public Provider(int capacity, int maxReceive, int maxExtract, ItemStack item) {
            storage = new ItemEnergyStorage(capacity, maxReceive, maxExtract, item);
            optional = LazyOptional.of(()-> storage);
        }

        public Provider(int capacity, int maxReceive, int maxExtract, int energy, ItemStack item) {
            storage = new ItemEnergyStorage(capacity, maxReceive, maxExtract, energy, item);
            optional = LazyOptional.of(()-> storage);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return Capabilities.ENERGY.orEmpty(cap, optional.cast());
        }

        @Override
        public Tag serializeNBT() {
            return storage.serializeNBT();
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            storage.deserializeNBT(nbt);
        }
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