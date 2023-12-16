package net.zaharenko424.a_changed.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GasCanisterItem extends BlockItem {
    public GasCanisterItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_.defaultDurability(256).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }
}