package net.zaharenko424.a_changed.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.LatexCoveredData;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.util.CoveredWith;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Supplier;

@Mixin(ServerLevel.class)
public abstract class MixinServerLevel extends Level {

    protected MixinServerLevel(WritableLevelData pLevelData, ResourceKey<Level> pDimension, RegistryAccess pRegistryAccess, Holder<DimensionType> pDimensionTypeRegistration, Supplier<ProfilerFiller> pProfiler, boolean pIsClientSide, boolean pIsDebug, long pBiomeZoomSeed, int pMaxChainedNeighborUpdates) {
        super(pLevelData, pDimension, pRegistryAccess, pDimensionTypeRegistration, pProfiler, pIsClientSide, pIsDebug, pBiomeZoomSeed, pMaxChainedNeighborUpdates);
    }

    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;isRandomlyTicking()Z"),
            method = "tickChunk")
    private boolean onIsRandomlyTicking(boolean original, @Local(argsOnly = true) LevelChunk chunk){
        return (getGameRules().getBoolean(AChanged.DO_LATEX_SPREAD) && !LatexCoveredData.of(chunk).isEmpty()) || original;
    }

    /**
     * Spread latex covered blocks.
     */
    @ModifyExpressionValue(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/LevelChunkSection;getBlockState(III)Lnet/minecraft/world/level/block/state/BlockState;"), method = "tickChunk")
    private BlockState onRandomTick(BlockState original, @Local(argsOnly = true) LevelChunk chunk, @Local BlockPos pos){
        final LatexCoveredData data = LatexCoveredData.of(chunk);

        if(!LatexCoveredData.isLatex(original) && LatexCoveredData.isStateNotCoverable(original)) {//If block should not be covered, but is covered, uncover it.
            data.coverWith(pos, CoveredWith.NOTHING);
            return original;
        }

        if(!getGameRules().getBoolean(AChanged.DO_LATEX_SPREAD)) return original;

        CoveredWith coveredWith;

        if(original.is(BlockRegistry.DARK_LATEX_BLOCK)){
            coveredWith = CoveredWith.DARK_LATEX;
        } else if(original.is(BlockRegistry.WHITE_LATEX_BLOCK)) {
            coveredWith = CoveredWith.WHITE_LATEX;
        } else {
            coveredWith = data.getCoveredWith(pos);
            if(coveredWith == CoveredWith.NOTHING) return original;
        }

        BlockPos pos1;
        BlockState state1;
        LevelChunk chunk1;
        LatexCoveredData data1;

        for(Direction direction : Direction.values()){
            pos1 = pos.relative(direction);
            if(!isLoaded(pos1)) continue;
            state1 = getBlockState(pos1);
            if(LatexCoveredData.isLatex(state1) || LatexCoveredData.isStateNotCoverable(state1)) continue;
            chunk1 = getChunkAt(pos1);
            data1 = (chunk == chunk1 ? data : LatexCoveredData.of(chunk1));

            if(data1.getCoveredWith(pos1) != CoveredWith.NOTHING) continue;
            data1.coverWith(pos1, coveredWith);
            break;
        }

        return original;
    }
}