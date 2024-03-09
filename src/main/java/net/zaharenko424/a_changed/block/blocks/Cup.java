package net.zaharenko424.a_changed.block.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.SmallDecorBlock;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class Cup extends SmallDecorBlock {

    private static final VoxelShape SHAPE_N = Shapes.or(Shapes.box(0.3875, 0, 0.3875, 0.6125, 0.0375, 0.6125),
            Shapes.box(0.3875, 0.025, 0.34375, 0.6125, 0.3375, 0.39375),
            Shapes.box(0.3875, 0.025, 0.60625, 0.6125, 0.3375, 0.65625),
            Shapes.box(0.34375, 0.025, 0.3875, 0.39375, 0.3375, 0.6125),
            Shapes.box(0.21875, 0.14375, 0.46875, 0.26875, 0.25625, 0.53125),
            Shapes.box(0.60625, 0.025, 0.3875, 0.65625, 0.3375, 0.6125),
            Shapes.box(0.24375, 0.25625, 0.46875, 0.34375, 0.30625, 0.53125),
            Shapes.box(0.24375, 0.09375, 0.46875, 0.34375, 0.14375, 0.53125));
    private static final VoxelShape SHAPE_E = Utils.rotateShape(Direction.EAST, SHAPE_N);
    private static final VoxelShape SHAPE_S = Utils.rotateShape(Direction.SOUTH, SHAPE_N);
    private static final VoxelShape SHAPE_W = Utils.rotateShape(Direction.WEST, SHAPE_N);

    public Cup(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Cup::new);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return switch(state.getValue(FACING)){
            case EAST -> SHAPE_E;
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_W;
            default -> SHAPE_N;
        };
    }
}