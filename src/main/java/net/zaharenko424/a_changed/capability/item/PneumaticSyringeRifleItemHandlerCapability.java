package net.zaharenko424.a_changed.capability.item;

import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilitySerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.item.CompressedAirCanister;
import net.zaharenko424.a_changed.item.LatexSyringeItem;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PneumaticSyringeRifleItemHandlerCapability {

    public static final ResourceLocation KEY = AChanged.resourceLoc("pneumatic_syringe_rifle_item_handler");

    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        PneumaticSyringeRifleItemHandler handler;
        LazyOptional<PneumaticSyringeRifleItemHandler> optional;

        public Provider(int size){
            handler = new PneumaticSyringeRifleItemHandler(size);
            optional = LazyOptional.of(()-> handler);
        }

        public Provider(NonNullList<ItemStack> list){
            handler = new PneumaticSyringeRifleItemHandler(list);
            optional = LazyOptional.of(()-> handler);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return Capabilities.ITEM_HANDLER.orEmpty(cap, optional.cast());
        }

        @Override
        public CompoundTag serializeNBT() {
            return handler.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            handler.deserializeNBT(nbt);
        }
    }

    public static class PneumaticSyringeRifleItemHandler extends ItemStackHandler {

        public PneumaticSyringeRifleItemHandler(int size){
            super(size);
        }

        public PneumaticSyringeRifleItemHandler(NonNullList<ItemStack> list){
            super(list);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            Item item = stack.getItem();
            return slot == 0 ? item instanceof CompressedAirCanister || stack.is(ItemRegistry.EMPTY_CANISTER.get()) : item instanceof LatexSyringeItem;
        }
    }
}