package net.zaharenko424.a_changed.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.zaharenko424.a_changed.LevelAccess;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class MixinLevel implements LevelAccess {

    @Shadow @Final public boolean isClientSide;

    @Shadow public abstract LevelChunk getChunkAt(BlockPos pPos);

    /**
     *  Removes latex coveredness from any blocks, that are removed.
     */
    @Inject(at = @At("TAIL"), method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z")
    private void removeLatexBlock(BlockPos pos, BlockState state, int pFlags, int pRecursionLeft, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 2) BlockState old){
        if(isClientSide || state.getBlock() == old.getBlock()) return;

        LatexCoveredData data = LatexCoveredData.of(getChunkAt(pos));
        if(data.getCoveredWith(pos) == CoveredWith.NOTHING) return;

        data.coverWith(pos, CoveredWith.NOTHING);
    }
}