package net.zaharenko424.a_changed.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.ability.DLPupMeltAbility;
import net.zaharenko424.a_changed.attachment.GrabData;
import net.zaharenko424.a_changed.attachment.LatexCoveredData;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.transfurSystem.CoveredWith;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow private Level level;

    @ModifyReturnValue(at = @At("TAIL"), method = "getAirSupply")
    private int capAirSupply(int original){
        return Math.max(original, -20);
    }

    /**
     * Stops grabbed entities from pushing the player, that grabbed them.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;isPassengerOfSameVehicle(Lnet/minecraft/world/entity/Entity;)Z"),
            method = "push(Lnet/minecraft/world/entity/Entity;)V")
    private boolean onPush(boolean original, @Local(argsOnly = true) Entity entity){
        if(original) return true;
        DLPupMeltAbility ability = AbilityRegistry.DL_PUP_MELT.get();

        if((entity instanceof LivingEntity living
                && AbilityUtils.hasAbility(ability, living) && ability.getAbilityData(living).isActivated())
            || ((Object)this instanceof LivingEntity self
                && AbilityUtils.hasAbility(ability, self) && ability.getAbilityData(self).isActivated())) {
            return true;//Don't push molten DL Pup & don't push entities as molten DL Pup
        }

        return (entity instanceof Player player && TransfurManager.isGrabbed(player))
                || ((Object) this instanceof Player player1 && GrabData.dataOf(player1).getGrabbedEntity() == entity);
    }

    /**
     *  Replace step sound if block is latex covered.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"),
            method = "playCombinationStepSounds")
    private SoundType onPlayCombinationStepSounds(SoundType original, @Local(argsOnly = true, ordinal = 0) BlockState state, @Local(argsOnly = true, ordinal = 0) BlockPos pos){
        if(LatexCoveredData.of(level.getChunkAt(pos)).getCoveredWith(pos) == CoveredWith.NOTHING) return original;
        return BlockRegistry.DARK_LATEX_BLOCK.get().getSoundType(state, level, pos, null);
    }

    /**
     *  Replace step sound if block is latex covered.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"),
            method = "playMuffledStepSound")
    private SoundType onPlayMuffledStepSound(SoundType original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos){
        if(LatexCoveredData.of(level.getChunkAt(pos)).getCoveredWith(pos) == CoveredWith.NOTHING) return original;
        return BlockRegistry.DARK_LATEX_BLOCK.get().getSoundType(state, level, pos, null);
    }

    /**
     *  Replace step sound if block is latex covered.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getSoundType(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/world/level/block/SoundType;"),
            method = "playStepSound")
    private SoundType onPlayStepSound(SoundType original, @Local(argsOnly = true) BlockState state, @Local(argsOnly = true) BlockPos pos){
        if(LatexCoveredData.of(level.getChunkAt(pos)).getCoveredWith(pos) == CoveredWith.NOTHING) return original;
        return BlockRegistry.DARK_LATEX_BLOCK.get().getSoundType(state, level, pos, null);
    }
}