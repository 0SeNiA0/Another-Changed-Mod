package net.zaharenko424.a_changed.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.TransfurSource;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.util.Latex;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LatexItem extends Item {

    private final Supplier<? extends AbstractTransfurType> transfurType;
    private final Latex type;

    public LatexItem(@NotNull Supplier<? extends AbstractTransfurType> transfurType, Latex type) {
        super(new Properties().food(new FoodProperties.Builder().fast().nutrition(1).saturationMod(2).build()));
        this.transfurType=transfurType;
        this.type=type;
    }

    public Latex getLatexType(){
        return type;
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_41409_, @NotNull Level p_41410_, @NotNull LivingEntity p_41411_) {
        Player player= (Player) p_41411_;
        if(!p_41410_.isClientSide){
            if(TransfurManager.isTransfurred(player)) return super.finishUsingItem(p_41409_,p_41410_,p_41411_);
            TransfurManager.addTransfurProgress(player,10,transfurType.get(), TransfurSource.GENERIC,false);
        }
        if(!player.getAbilities().instabuild) p_41409_.shrink(1);
        return p_41409_;
    }
}