package net.zaharenko424.a_changed.mixin.entity;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import net.zaharenko424.a_changed.AChanged;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ILivingEntityExtension {

    @Unique
    protected float mod$airDecreaseDecimals = 0;

    @Shadow public abstract double getAttributeValue(Holder<Attribute> p_251296_);

    public MixinLivingEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    /**
     * AIR_DECREASE_SPEED attribute injection
     */
    @Inject(at = @At("RETURN"), method = "decreaseAirSupply", cancellable = true)
    private void onDecreaseAirSupply(int p_21303_, CallbackInfoReturnable<Integer> cir){
        float airDecrease = (float) getAttributeValue(AChanged.AIR_DECREASE_SPEED);
        if(airDecrease == 1) return;
        int i = EnchantmentHelper.getRespiration(self());
        if(airDecrease == 0 || (i > 0 && random.nextInt(i + 1) > 0)){
            cir.setReturnValue(p_21303_);
            return;
        }

        int airReduction = (int) airDecrease;
        float decimals = airDecrease % 1;
        if(decimals > .01) mod$airDecreaseDecimals += decimals;
        if(mod$airDecreaseDecimals > 1){
            mod$airDecreaseDecimals--;
            airReduction++;
        }

        cir.setReturnValue(p_21303_ - airReduction);
    }
}