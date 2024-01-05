package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class AbstractMultiBlock extends Block {

    public AbstractMultiBlock(Properties p_54120_) {
        super(p_54120_);
    }

    protected abstract IntegerProperty part();

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return canSurvive(p_60541_,p_60544_,p_60545_) ? p_60541_ : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(!super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)) return false;
        if(level.isClientSide) return true;
        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        if(!mainState.isAir() && mainPos != pos) {
            if(level.destroyBlock(mainPos, false, player)&&willHarvest)
                Block.dropResources(mainState,level,mainPos,null,player,player.getMainHandItem());
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(part()));
    }

    protected abstract BlockPos getMainPos(BlockState state, BlockPos pos);
}