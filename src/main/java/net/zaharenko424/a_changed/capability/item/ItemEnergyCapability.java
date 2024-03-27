package net.zaharenko424.a_changed.capability.item;

import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.zaharenko424.a_changed.capability.energy.EnergyConsumer;
import net.zaharenko424.a_changed.registry.AttachmentRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ItemEnergyCapability {

    @Contract("_, _, _ -> new") @ApiStatus.Internal
    public static @NotNull ItemEnergyStorage getCapability(int capacity, int maxTransfer, @NotNull ItemStack item){
        return item.getData(AttachmentRegistry.ITEM_ENERGY_HANDLER).init(capacity, maxTransfer, item);
    }

    public static class ItemEnergyStorage extends EnergyConsumer {

        private ItemStack item;

        public ItemEnergyStorage(){
            super(0);
        }

        ItemEnergyStorage init(int capacity, int maxTransfer, @NotNull ItemStack item){
            if(this.item != null) return this;
            this.item = item;
            this.capacity = capacity;
            this.maxReceive = maxTransfer;
            this.maxExtract = maxTransfer;
            updateItemDamage();
            return this;
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

        @Override
        public void consumeEnergy(int amount) {
            super.consumeEnergy(amount);
            updateItemDamage();
        }

        private void updateItemDamage(){
            item.setDamageValue(item.getMaxDamage() * (capacity - energy) / capacity);
        }
    }

    public static class Serializer implements IAttachmentSerializer<Tag, ItemEnergyStorage> {

        public static final Serializer INSTANCE = new Serializer();

        private Serializer() {}

        @Override
        public @NotNull ItemEnergyStorage read(@NotNull IAttachmentHolder holder, @NotNull Tag tag) {
            ItemEnergyStorage storage = new ItemEnergyStorage();
            storage.deserializeNBT(tag);
            return storage;
        }

        @Override
        public @Nullable Tag write(@NotNull ItemEnergyStorage itemEnergyStorage) {
            return itemEnergyStorage.serializeNBT();
        }
    }
}