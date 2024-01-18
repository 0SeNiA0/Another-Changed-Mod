package net.zaharenko424.a_changed.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class AbstractMultiBlock extends Block {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AbstractMultiBlock(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    protected abstract IntegerProperty part();

    protected abstract ImmutableMap<Integer, Part> parts();

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return canSurvive(p_60541_,p_60544_,p_60545_) ? p_60541_ : Blocks.AIR.defaultBlockState();
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        if(canBePlaced(context.getClickedPos(), direction, context.getLevel()))
            return defaultBlockState().setValue(FACING, direction);
        return null;
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
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState pNewState, boolean pMovedByPiston) {
        if(state.is(pNewState.getBlock())) return;
        super.onRemove(state, level, pos, pNewState, pMovedByPiston);
        BlockPos mainPos = getMainPos(state, pos);
        if(state.getValue(part()) != 0){
            if(level.getBlockState(mainPos).is(this)) level.setBlockAndUpdate(mainPos, Blocks.AIR.defaultBlockState());
            return;
        }
        Direction direction = state.getValue(FACING);
        parts().forEach((id, part) -> {
            BlockPos pos1 = part.toSecondaryPos(pos, direction);
            if(id != 0) level.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
        });
    }

    @Override
    public void setPlacedBy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity pPlacer, @NotNull ItemStack pStack) {
        Direction direction = state.getValue(FACING);
        parts().forEach((id, part) -> {
            BlockPos pos1 = part.toSecondaryPos(pos, direction);
            if(id != 0) level.setBlockAndUpdate(pos1, state.setValue(part(), id));
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING, part());
    }

    protected BlockPos getMainPos(BlockState state, BlockPos pos){
        return parts().get(state.getValue(part())).toMainPos(pos, state.getValue(FACING));
    }

    protected boolean isPowered(BlockPos mainPos, BlockState mainState, Level level){
        Direction direction = mainState.getValue(FACING);
        for(Part part : parts().values()){
            if(level.hasNeighborSignal(part.toSecondaryPos(mainPos, direction))) return true;
        }
        return false;
    }

    public record Part(int x, int y, int z){

        public @NotNull BlockPos toMainPos(@NotNull BlockPos secondaryPos, @NotNull Direction direction){
            return secondaryPos
                    .relative(direction.getCounterClockWise(), x)
                    .below(y)
                    .relative(direction, z);
        }

        public @NotNull BlockPos toSecondaryPos(@NotNull BlockPos mainPos, @NotNull Direction direction){
            return mainPos
                    .relative(direction.getClockWise(), x)
                    .above(y)
                    .relative(direction.getOpposite(), z);
        }

        @Contract(pure = true)
        public @NotNull Supplier<VoxelShape> alignShape(VoxelShape baseShape){
            return () -> baseShape.move(-x, -y, -z);
        }
    }
}