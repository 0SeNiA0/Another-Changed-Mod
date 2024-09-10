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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.neoforge.common.extensions.ILivingEntityExtension;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity implements ILivingEntityExtension {

    @Unique
    protected float achanged$airDecreaseDecimals = 0;

    @Shadow public abstract double getAttributeValue(Holder<Attribute> p_251296_);

    @Shadow public abstract boolean hasEffect(Holder<MobEffect> effect);

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
        if(airDecrease == 1 || original == currentAir) return original;

        int airReduction = (int) airDecrease;
        float decimals = airDecrease % 1;
        if(decimals > .01) achanged$airDecreaseDecimals += decimals;
        if(achanged$airDecreaseDecimals > 1){
            achanged$airDecreaseDecimals--;
            airReduction++;
        }

        return currentAir - airReduction;
    }

    /**
     * Replace fall sound if block is latex covered.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"),
            method = "playBlockFallSound")
    private SoundType onPlayBlockFallSound(SoundType original, @Local BlockState state, @Local BlockPos pos){
        if(LatexCoveredData.of(level().getChunkAt(pos)).getCoveredWith(pos) == CoveredWith.NOTHING) return original;
        return BlockRegistry.DARK_LATEX_BLOCK.get().getSoundType(state, level(), pos, null);
    }

    /**
     * Allows player to fly when transfurred as flying latex.
     */
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setSharedFlag(IZ)V"), index = 1,
            method = "updateFallFlying")
    private boolean onUpdateFallFlying(boolean par2){
        if(!getSharedFlag(7) || onGround() || isPassenger() || hasEffect(MobEffects.LEVITATION)) return false;

        if(!TransfurManager.hasFallFlyingAbility(self())) return par2;
        gameEvent(GameEvent.ELYTRA_GLIDE);
        return true;
    }
}