package net.zaharenko424.a_changed.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

public class SpecializedUnTransfurSyringe extends UnTransfurSyringeItem {

    private final Latex latex;

    public SpecializedUnTransfurSyringe(Properties properties, Latex latex){
        super(properties);
        this.latex = latex;
    }

    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack pStack) {
        return Rarity.UNCOMMON;
    }

    @Override
    protected void use(ItemStack item, ServerPlayer player) {
        if(TransfurManager.getTransfurType(player).latex == latex){
            giveDebuffs(player, 1);
            super.use(item, player);
        } else {
            giveDebuffs(player, 2);
            giveWither(player, .4f);
        }
    }
}