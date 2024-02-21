package net.zaharenko424.a_changed.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import org.jetbrains.annotations.NotNull;

public class CompressedAirCanister extends Item {

    public CompressedAirCanister() {
        super(new Properties().durability(32));
    }

    public static @NotNull ItemStack consumeAir(@NotNull ItemStack canister){
        int damage = canister.getDamageValue() + 1;
        if(damage >= canister.getMaxDamage()){
            return ItemRegistry.EMPTY_CANISTER.get().getDefaultInstance();
        }
        canister.setDamageValue(damage);
        return canister;
    }
}