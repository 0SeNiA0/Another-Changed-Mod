package net.zaharenko424.a_changed.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.registry.FluidRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.Latex;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class LatexItem extends Item {

    private final Supplier<? extends TransfurType> transfurType;
    private final Latex type;

    public LatexItem(@NotNull Supplier<? extends TransfurType> transfurType, Latex type) {
        super(new Properties().food(new FoodProperties.Builder().fast().nutrition(1).saturationMod(1).build()));
        this.transfurType = transfurType;
        this.type = type;
    }

    public Latex getLatexType(){
        return type;
    }

    private static TriConsumer<LivingEntity, TransfurType, Float> ADD_TRANSFUR;

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack p_41409_, @NotNull Level p_41410_, @NotNull LivingEntity p_41411_) {
        Player player= (Player) p_41411_;
        if(!p_41410_.isClientSide){
            if(TransfurManager.isTransfurred(player)) return super.finishUsingItem(p_41409_,p_41410_,p_41411_);
            if(ADD_TRANSFUR == null) ADD_TRANSFUR = TransfurEvent.addTransfurProgress().checkResistance(false).build();
            ADD_TRANSFUR.accept(player, transfurType.get(), 10f);
        }
        if(!player.isCreative()) p_41409_.shrink(1);
        return p_41409_;
    }

    @Override
    public boolean onEntityItemUpdate(@NotNull ItemStack stack, @NotNull ItemEntity entity) {
        if(!entity.isInFluidType(FluidRegistry.LATEX_SOLVENT_TYPE.get())) return super.onEntityItemUpdate(stack, entity);
        entity.discard();
        return true;
    }
}