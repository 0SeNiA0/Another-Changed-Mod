package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.SmallDecorBlock;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class TestTubes extends SmallDecorBlock {

    private static final VoxelShape SHAPE_N = Shapes.or(Shapes.box(0.125, 0.3125, 0.375, 0.875, 0.375, 0.625),
            Shapes.box(0.55, 0.05, 0.4375, 0.58125, 0.6125, 0.5625),
            Shapes.box(0.41875, 0.05, 0.4375, 0.45, 0.6125, 0.5625),
            Shapes.box(0.66875, 0.05, 0.4375, 0.7, 0.6125, 0.5625),
            Shapes.box(0.16875, 0.05, 0.4375, 0.2, 0.6125, 0.5625),
            Shapes.box(0.3, 0.05, 0.4375, 0.33125, 0.6125, 0.5625),
            Shapes.box(0.8, 0.05, 0.4375, 0.83125, 0.6125, 0.5625),
            Shapes.box(0.1875, 0.05, 0.55, 0.3125, 0.6125, 0.58125),
            Shapes.box(0.1875, 0.05, 0.41875, 0.3125, 0.6125, 0.45),
            Shapes.box(0.4375, 0.05, 0.41875, 0.5625, 0.6125, 0.45),
            Shapes.box(0.6875, 0.05, 0.41875, 0.8125, 0.6125, 0.45),
            Shapes.box(0.4375, 0.05, 0.55, 0.5625, 0.6125, 0.58125),
            Shapes.box(0.6875, 0.05, 0.55, 0.8125, 0.6125, 0.58125),
            Shapes.box(0.4375, 0.03125, 0.4375, 0.5625, 0.0625, 0.5625),
            Shapes.box(0.6875, 0.03125, 0.4375, 0.8125, 0.0625, 0.5625),
            Shapes.box(0.1875, 0.03125, 0.4375, 0.3125, 0.0625, 0.5625),
            Shapes.box(0.09375, 0, 0.34375, 0.15625, 0.3125, 0.40625),
            Shapes.box(0.09375, 0, 0.59375, 0.15625, 0.3125, 0.65625),
            Shapes.box(0.84375, 0, 0.59375, 0.90625, 0.3125, 0.65625),
            Shapes.box(0.84375, 0, 0.34375, 0.90625, 0.3125, 0.40625));
    private static final VoxelShape SHAPE_E = Utils.rotateShape(Direction.EAST, SHAPE_N);

    public TestTubes(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return switch(state.getValue(FACING)){
            case EAST, WEST -> SHAPE_E;
            default -> SHAPE_N;
        };
    }
}