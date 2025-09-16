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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public abstract class AbstractMultiBlock extends Block {

    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AbstractMultiBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    protected abstract IntegerProperty part();

    protected abstract ImmutableMap<Integer, Part> parts();

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return canSurvive(state,level,pos) ? state : Blocks.AIR.defaultBlockState();
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
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(state.is(newState.getBlock())) return;
        super.onRemove(state, level, pos, newState, movedByPiston);
        BlockPos mainPos = getMainPos(state, pos);
        if(state.getValue(part()) != 0){
            if(level.getBlockState(mainPos).is(this)) level.setBlockAndUpdate(mainPos, Blocks.AIR.defaultBlockState());
            return;
        }
        Direction direction = state.getValue(FACING);
        parts().forEach((id, part) -> {
            BlockPos pos1 = part.toSecondaryPos(pos, direction);
            BlockState foundState = level.getBlockState(pos1);

            if(id != 0 && foundState.is(this)) level.setBlockAndUpdate(pos1, Blocks.AIR.defaultBlockState());
        });
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        Direction direction = state.getValue(FACING);
        parts().forEach((id, part) -> {
            if(id == 0) return;
            level.setBlockAndUpdate(part.toSecondaryPos(pos, direction), state.setValue(part(), id));
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, part());
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


    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    /**
     * By default, mirroring is prohibited. Not possible to reliably detect multiblock
     */
    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return mirror == Mirror.NONE ? state : Blocks.AIR.defaultBlockState();
    }

    public record Part(int x, int y, int z){

        public @NotNull BlockPos toMainPos(BlockPos secondaryPos, Direction direction){
            return secondaryPos
                    .relative(direction.getCounterClockWise(), x)
                    .below(y)
                    .relative(direction, z);
        }

        public @NotNull BlockPos toSecondaryPos(BlockPos mainPos, Direction direction){
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