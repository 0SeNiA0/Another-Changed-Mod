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

public class BrokenFlask extends SmallDecorBlock {

    private static final VoxelShape SHAPE_N = Shapes.or(Shapes.box(0.375, 0, 0.125, 0.625, 0.25, 0.75),
            Shapes.box(0.375, 0.25, 0.225, 0.625, 0.5, 0.475),
            Shapes.box(0.375, 0.25, 0.1625, 0.625, 0.3125, 0.225),
            Shapes.box(0.375, 0.25, 0.475, 0.625, 0.4375, 0.5375),
            Shapes.box(0.375, 0.25, 0.5375, 0.625, 0.375, 0.6),
            Shapes.box(0.375, 0, 0.75, 0.625, 0.1875, 0.875));
    private static final VoxelShape SHAPE_E = Utils.rotateShape(Direction.EAST, SHAPE_N);
    private static final VoxelShape SHAPE_S = Utils.rotateShape(Direction.SOUTH, SHAPE_N);
    private static final VoxelShape SHAPE_W = Utils.rotateShape(Direction.WEST, SHAPE_N);

    public BrokenFlask(Properties pProperties) {
        super(pProperties);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(BrokenFlask::new);
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