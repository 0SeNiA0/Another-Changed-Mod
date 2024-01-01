package net.zaharenko424.a_changed.mixin;

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
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ILivingEntityExtension {

    @Shadow public abstract double getAttributeValue(Holder<Attribute> p_251296_);

    public MixinLivingEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(at = @At("TAIL"), method = "decreaseAirSupply", cancellable = true)
    private void onDecreaseAirSupply(int p_21303_, CallbackInfoReturnable<Integer> cir){
        int airDecrease = (int) getAttributeValue(AChanged.AIR_DECREASE_SPEED);
        if(airDecrease==1) return;
        int i = EnchantmentHelper.getRespiration(self());
        cir.setReturnValue(i > 0 && this.random.nextInt(i + 1) > 0 ? p_21303_ : p_21303_ - airDecrease);
    }
}