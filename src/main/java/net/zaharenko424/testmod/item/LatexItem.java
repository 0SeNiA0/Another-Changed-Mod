package net.zaharenko424.testmod.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class LatexItem extends Item {

    private final String transfurType;

    public LatexItem(@NotNull Properties p_41383_, @NotNull String transfurType) {
        super(p_41383_.food(new FoodProperties.Builder().fast().nutrition(1).saturationMod(2).build()));
        this.transfurType=transfurType;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_41409_, @NotNull Level p_41410_, @NotNull LivingEntity p_41411_) {
        Player player= (Player) p_41411_;
        if(!p_41410_.isClientSide){
            if(TransfurManager.isTransfurred(player)) return super.finishUsingItem(p_41409_,p_41410_,p_41411_);
            TransfurManager.addTransfurProgress(player,10,transfurType);
        }
        if(!player.getAbilities().instabuild) p_41409_.shrink(1);
        return p_41409_;
    }
}
