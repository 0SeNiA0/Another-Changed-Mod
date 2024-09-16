package net.zaharenko424.a_changed.item;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.Latex;

public class SpecializedUnTransfurSyringe extends UnTransfurSyringeItem {

    private final Latex latex;

    public SpecializedUnTransfurSyringe(Properties properties, Latex latex){
        super(properties);
        this.latex = latex;
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