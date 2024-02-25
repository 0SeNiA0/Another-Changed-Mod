package net.zaharenko424.a_changed.capability.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.zaharenko424.a_changed.item.CompressedAirCanister;
import net.zaharenko424.a_changed.item.LatexSyringeItem;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class PneumaticSyringeRifleItemHandlerCapability {

    @Contract("_ -> new")
    public static @NotNull PneumaticSyringeRifleItemHandler getCapability(int size){
        return new PneumaticSyringeRifleItemHandler(size);
    }

    public static class PneumaticSyringeRifleItemHandler extends ItemStackHandler {

        public PneumaticSyringeRifleItemHandler(int size){
            super(size);
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            Item item = stack.getItem();
            return slot == 0 ? item instanceof CompressedAirCanister || stack.is(ItemRegistry.EMPTY_CANISTER.get())
                    : item instanceof LatexSyringeItem;
        }
    }
}