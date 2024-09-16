package net.zaharenko424.a_changed.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.EntityAccess;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityAccess {

    @Shadow private Level level;

    @ModifyReturnValue(at = @At("TAIL"), method = "getAirSupply")
    private int capAirSupply(int original){
        return Math.max(original, -20);
    }

    /**
     * Stops grabbed entities from pushing the player, that grabbed them.
     */
    @Inject(at = @At("HEAD"), method = "push(Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    private void onPush(Entity entity, CallbackInfo ci){
        if((entity instanceof Player player && TransfurManager.isGrabbed(player))
                || (getSelf() instanceof Player player1 && GrabData.dataOf(player1).getGrabbedEntity() == entity))
            ci.cancel();
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