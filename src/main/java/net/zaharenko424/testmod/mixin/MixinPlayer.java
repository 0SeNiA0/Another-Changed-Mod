package net.zaharenko424.testmod.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TransfurDamageSource;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.capability.ITransfurHandler;
import net.zaharenko424.testmod.capability.TransfurCapability;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    public MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Redirect(at=@At(value = "INVOKE", target = "net/minecraft/world/entity/Entity.hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),method = "attack",allow = 1)
    public boolean hurtProxy(@NotNull Entity instance, DamageSource p_19946_, float p_19947_){
        if(instance.level().isClientSide) return instance.hurt(p_19946_,p_19947_);
        ITransfurHandler handler = getCapability(TransfurCapability.CAPABILITY).orElseThrow(TransfurCapability.NO_CAPABILITY_EXC);
        if(!(getMainHandItem().isEmpty()&&handler.isTransfurred())) return instance.hurt(p_19946_,p_19947_);
        p_19947_+=TransfurManager.LATEX_DAMAGE_BONUS;
        if(!TransfurDamageSource.checkTarget(instance)) return instance.hurt(p_19946_,p_19947_);
        TransfurManager.addTransfurProgress((LivingEntity) instance,5, Objects.requireNonNull(handler.getTransfurType()));
        return instance.hurt(TransfurDamageSource.transfur(instance, this),p_19947_);
    }

    @Inject(at = @At("HEAD"), method = "getDimensions", cancellable = true)
    private void onGetDimensions(Pose p_36166_, CallbackInfoReturnable<EntityDimensions> ci) {
        if(p_36166_==Pose.STANDING){
            getCapability(TransfurCapability.CAPABILITY).ifPresent((handler)->{
                if(!handler.isTransfurred()) return;
                ci.cancel();
                ci.setReturnValue(EntityDimensions.scalable(.6f,2));
            });
        }
    }

    @Inject(at = @At("HEAD"), method = "getStandingEyeHeight", cancellable = true)
    private void onGetEyeHeight(Pose p_36259_, EntityDimensions p_36260_, CallbackInfoReturnable<Float> ci){
        getCapability(TransfurCapability.CAPABILITY).ifPresent((handler)->{
            if(!handler.isTransfurred()) return;
            ci.cancel();
            ci.setReturnValue(handler.getTransfurType().getEyeHeight(p_36259_));
        });
    }
}