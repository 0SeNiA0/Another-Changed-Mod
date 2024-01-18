package net.zaharenko424.a_changed.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Not so nice solution for multiBlocks that do not need FACING property
 */
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class NotRotatedMultiBlock extends Block {

    public NotRotatedMultiBlock(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(part(), 0));
    }

    protected abstract IntegerProperty part();

    protected abstract ImmutableMap<Integer, AbstractMultiBlock.Part> parts();

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return canSurvive(p_60541_,p_60544_,p_60545_) ? p_60541_ : Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        if(!canBePlaced(context.getClickedPos(), direction, context.getLevel())) return null;
        return defaultBlockState();
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(level.isClientSide) return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        if(!super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid)) return false;
        if(!mainState.isAir() && mainPos != pos) {
            if(willHarvest) Block.dropResources(mainState, level, mainPos,null, player, player.getMainHandItem());
        }
        return true;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState pNewState, boolean pMovedByPiston) {
        if(state.is(pNewState.getBlock())) return;
        super.onRemove(state, level, pos, pNewState, pMovedByPiston);
        BlockPos mainPos = getMainPos(state, pos);
        if(state.getValue(part()) != 0){
            if(level.getBlockState(mainPos).is(this)) level.setBlockAndUpdate(mainPos, Blocks.AIR.defaultBlockState());
            return;
        }
        parts().forEach((id, part) -> {
            if(id != 0) level.setBlockAndUpdate(part.toSecondaryPos(pos, Direction.NORTH), Blocks.AIR.defaultBlockState());
        });
    }

    protected boolean canBePlaced(BlockPos mainPos, Direction direction, Level level){
        BlockPos pos;
        for(AbstractMultiBlock.Part part : parts().values()){
            pos = part.toSecondaryPos(mainPos, direction);
            if(!level.isInWorldBounds(pos) || !level.getBlockState(pos).canBeReplaced()) return false;
        }
        return true;
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        parts().forEach((id, part) -> {
            if(id != 0) level.setBlockAndUpdate(part.toSecondaryPos(pos, Direction.NORTH), state.setValue(part(), id));
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(part()));
    }

    protected BlockPos getMainPos(BlockState state, BlockPos pos){
        return parts().get(state.getValue(part())).toMainPos(pos, Direction.NORTH);
    }
}