package net.zaharenko424.testmod.mixins;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.testmod.TransfurDamageSource;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.entity.TransfurHolder;
import net.zaharenko424.testmod.entity.Transfurrable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static net.zaharenko424.testmod.TransfurManager.*;

@Mixin(Player.class)
public abstract class MixinPlayer extends LivingEntity implements TransfurHolder {

    protected MixinPlayer(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Override
    public int mod$getTransfurProgress() {
        return TransfurManager.modTag(getPersistentData()).getInt(TRANSFUR_PROGRESS_KEY);
    }

    @Override
    public void mod$setTransfurProgress(int amount, @NotNull ResourceLocation transfurType) {
        mod$updateCompound(amount,transfurType,null);
    }

    @Override
    public @Nullable ResourceLocation mod$getTransfurType() {
        CompoundTag tag=TransfurManager.modTag(getPersistentData());
        return tag.contains(TRANSFUR_TYPE_KEY)?new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY)):null;
    }

    @Override
    public void mod$setTransfurType(@NotNull ResourceLocation transfurType) {
        mod$updateCompound(null,transfurType,null);
    }

    @Override
    public boolean mod$isTransfurred() {
        return TransfurManager.modTag(getPersistentData()).getBoolean(TRANSFURRED_KEY);
    }

    @Override
    public void mod$transfur(@NotNull ResourceLocation transfurType) {
        mod$updateCompound(20,transfurType,true);
    }

    @Override
    public void mod$unTransfur() {
        mod$updateCompound(0,null,false);
    }

    @Unique
    private void mod$updateCompound(@Nullable Integer transfurProgress, @Nullable ResourceLocation transfurType,@Nullable Boolean isTransfurred){
        CompoundTag tag= TransfurManager.modTag(getPersistentData());
        if(transfurProgress!=null) tag.putInt(TRANSFUR_PROGRESS_KEY,transfurProgress);
        if(transfurType!=null) tag.putString(TRANSFUR_TYPE_KEY,transfurType.toString()); else tag.remove(TRANSFUR_TYPE_KEY);
        if(isTransfurred!=null) tag.putBoolean(TRANSFURRED_KEY,isTransfurred);
    }

    @Redirect(at=@At(value = "INVOKE", target = "net/minecraft/world/entity/Entity.hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),method = "attack")
    public boolean hurtProxy(Entity instance, DamageSource p_19946_, float p_19947_){
        if(getMainHandItem().isEmpty()&&mod$isTransfurred()&&instance instanceof Transfurrable transfurrable) {
            TransfurManager.addTransfurProgress(transfurrable,5,mod$getTransfurType());
            return instance.hurt(TransfurDamageSource.transfur(instance, this),p_19947_);
        }
        return instance.hurt(p_19946_,p_19947_);
    }
}