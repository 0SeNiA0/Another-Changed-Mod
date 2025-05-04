package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BigLabLamp extends HorizontalTwoBlockMultiBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    private static final VoxelShape SHAPE = Shapes.or(
            Shapes.box(-1, 0.9375f, 0, 1, 1, 1),
            Shapes.box(-0.9375f, 0.875f, 0.0625f, 0.9375f, 0.9375f, 0.9375f));
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public BigLabLamp(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(LIT, false));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(FACING);
        int partId = state.getValue(PART);
        return CACHE.getShape(direction, partId, PARTS.get(partId).alignShape(SHAPE));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return context.getClickedFace() == Direction.DOWN ?
                super.getStateForPlacement(context).setValue(LIT, context.getLevel().hasNeighborSignal(context.getClickedPos()))
                : null;
    }

    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block neighborBlock, @NotNull BlockPos neighborPos, boolean movedByPiston) {
        if (!level.isClientSide) {
            if (state.getValue(LIT) != level.hasNeighborSignal(pos)) {
                level.setBlock(pos, state.cycle(LIT), 2);
                BlockPos mainPos = getMainPos(state, pos);
                if(mainPos == pos){
                    mainPos = PARTS.get(1).toSecondaryPos(mainPos, state.getValue(FACING));
                }
                level.setBlock(mainPos, level.getBlockState(mainPos).cycle(LIT), 2);
            }
        }
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(LIT));
    }
}