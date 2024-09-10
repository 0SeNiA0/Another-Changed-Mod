package net.zaharenko424.a_changed.capability.item;

import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.capability.energy.ExtendedEnergyStorage;
import net.zaharenko424.a_changed.registry.ComponentRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ItemEnergyCapability {

    @Contract("_, _, _ -> new") @ApiStatus.Internal
    public static @NotNull ItemEnergyStorage getCapability(int capacity, int maxTransfer, @NotNull ItemStack item){
        return new ItemEnergyStorage(item, capacity, maxTransfer);
    }

    public static class ItemEnergyStorage extends ExtendedEnergyStorage {

        private final ItemStack item;

        public ItemEnergyStorage(@NotNull ItemStack item, int capacity, int maxTransfer){
            super(capacity, maxTransfer);
            this.item = item;
            energy = item.getOrDefault(ComponentRegistry.ITEM_ENERGY, 0);
        }

        @Override
        public void onEnergyChanged() {
            item.set(ComponentRegistry.ITEM_ENERGY, energy);
            updateItemDamage();
        }

        private void updateItemDamage(){
            item.setDamageValue(item.getMaxDamage() * (capacity - energy) / capacity);
        }
    }
}