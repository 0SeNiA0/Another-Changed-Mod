package net.zaharenko424.testmod.mixins;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TransfurDamageSource;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.capability.ITransfurHandler;
import net.zaharenko424.testmod.capability.TransfurCapability;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Objects;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity {
    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Redirect(at=@At(value = "INVOKE", target = "net/minecraft/world/entity/Entity.hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),method = "attack",allow = 1)
    public boolean hurtProxy(@NotNull Entity instance, DamageSource p_19946_, float p_19947_){
        if(instance.level().isClientSide) return instance.hurt(p_19946_,p_19947_);
        ITransfurHandler handler = getCapability(TransfurCapability.CAPABILITY).orElseThrow(TransfurCapability.NO_CAPABILITY_EXC);
        if(getMainHandItem().isEmpty()&&handler.isTransfurred()&&instance instanceof LivingEntity entity){
            TransfurManager.addTransfurProgress(entity,5, Objects.requireNonNull(handler.getTransfurType()));
            return instance.hurt(TransfurDamageSource.transfur(instance, this),p_19947_);
        }
        return instance.hurt(p_19946_,p_19947_);
    }
}