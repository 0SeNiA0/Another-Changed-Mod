package net.zaharenko424.a_changed.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GasTankItem extends BlockItem {

    public GasTankItem(Block block, Properties properties) {
        super(block, properties.durability(128).rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return true;
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return -12761089;
    }
}