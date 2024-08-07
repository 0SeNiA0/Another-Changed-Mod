package net.zaharenko424.a_changed.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractFlyingLatex;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ILivingEntityExtension {

    @Unique
    protected float mod$airDecreaseDecimals = 0;

    @Shadow public abstract double getAttributeValue(Holder<Attribute> p_251296_);

    @Shadow public abstract boolean hasEffect(MobEffect pEffect);

    public MixinLivingEntity(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    /**
     * AIR_DECREASE_SPEED attribute injection
     */
    @ModifyReturnValue(at = @At("RETURN"), method = "decreaseAirSupply")
    private int onDecreaseAirSupply(int original, @Local(argsOnly = true) int currentAir){
        float airDecrease = (float) getAttributeValue(AChanged.AIR_DECREASE_SPEED);
        if(airDecrease == 0) return currentAir;
        if(airDecrease == 1) return original;

        int airReduction = (int) airDecrease;
        float decimals = airDecrease % 1;
        if(decimals > .01) mod$airDecreaseDecimals += decimals;
        if(mod$airDecreaseDecimals > 1){
            mod$airDecreaseDecimals--;
            airReduction++;
        }

        return currentAir - airReduction;
    }

    /**
     * Replace fall sound if block is latex covered.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"),
            method = "playBlockFallSound")
    private SoundType onPlayBlockFallSound(SoundType original, @Local BlockPos pos){
        if(LatexCoveredData.of(level().getChunkAt(pos)).getCoveredWith(pos) == CoveredWith.NOTHING) return original;
        return BlockRegistry.DARK_LATEX_BLOCK.get().getSoundType(null);
    }

    /**
     * Allows player to fly when transfurred as flying latex.
     */
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"), index = 1,
            method = "updateFallFlying")
    private boolean onUpdateFallFlying(boolean par2){
        if(!getSharedFlag(7) || onGround() || isPassenger() || hasEffect(MobEffects.LEVITATION)) return false;

        ITransfurHandler handler = TransfurCapability.of(self());
        if( handler == null || !handler.isTransfurred() || !(handler.getTransfurType() instanceof AbstractFlyingLatex)) return par2;
        gameEvent(GameEvent.ELYTRA_GLIDE);
        return true;
    }
}