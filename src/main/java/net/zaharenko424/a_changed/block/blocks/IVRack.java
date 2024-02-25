package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.VerticalTwoBlockMultiBlock;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class IVRack extends VerticalTwoBlockMultiBlock {

    private static final VoxelShape SHAPE_0N = Shapes.or(Shapes.box(0.4375, 0.125, 0.125, 0.5625, 0.1875, 0.875),
            Shapes.box(0.125, 0.125, 0.4375, 0.875, 0.1875, 0.5625),
            Shapes.box(0.8125, 0, 0.46875, 0.9375, 0.125, 0.53125),
            Shapes.box(0.0625, 0, 0.46875, 0.1875, 0.125, 0.53125),
            Shapes.box(0.45, 0.1875, 0.45, 0.55, 1.75, 0.55),
            Shapes.box(0.125, 1.75, 0.45, 0.875, 1.8125, 0.55),
            Shapes.box(0.46875, 0, 0.0625, 0.53125, 0.125, 0.1875),
            Shapes.box(0.46875, 0, 0.8125, 0.53125, 0.125, 0.9375));
    private static final VoxelShape SHAPE_0E = Utils.rotateShape(Direction.EAST, SHAPE_0N);
    private static final VoxelShape SHAPE_1N = SHAPE_0N.move(0, -1, 0);
    private static final VoxelShape SHAPE_1E = SHAPE_0E.move(0, -1, 0);

    public IVRack(@NotNull Properties p_54120_) {
        super(p_54120_.pushReaction(PushReaction.DESTROY));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return switch(state.getValue(FACING)){
            case NORTH, SOUTH -> state.getValue(PART) == 0 ? SHAPE_0N : SHAPE_1N;
            default -> state.getValue(PART) == 0 ? SHAPE_0E : SHAPE_1E;
        };
    }

    @Override
    public boolean canSurvive(@NotNull BlockState p_60525_, @NotNull LevelReader p_60526_, @NotNull BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        BlockState state = p_60526_.getBlockState(pos);
        return p_60525_.getValue(PART) == 0 ? state.isFaceSturdy(p_60526_, pos, Direction.UP) : state.is(this);
    }
}