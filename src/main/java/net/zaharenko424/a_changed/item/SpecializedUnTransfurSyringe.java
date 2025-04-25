package net.zaharenko424.a_changed.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.zaharenko424.a_changed.attachments.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.Latex;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class SpecializedUnTransfurSyringe extends UnTransfurSyringeItem {

    private final Latex latex;

    public SpecializedUnTransfurSyringe(Properties properties, Latex latex){
        super(properties);
        this.latex = latex;
    }

    @Override
    protected void untransfur(@NotNull ItemStack item, @NotNull LivingEntity entity) {
        if(TransfurManager.getTransfurType(entity).latex == latex){
            TransfurHandler.nonNullOf(entity).unTransfur(TransfurContext.UNTRANSFUR);
            giveDebuffs(entity, 1);
            giveWither(entity, .2f, .25f);
        } else {
            giveDebuffs(entity, 2);
            giveWither(entity, .4f, entity.getRandom().nextFloat() + .2f);
        }
    }
}