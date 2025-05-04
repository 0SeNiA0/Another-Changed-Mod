package net.zaharenko424.a_changed.block.smalldecor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class Disc extends SmallDecorBlock {

    protected static final VoxelShape SHAPE = Shapes.box(.25, 0, .3, .75, 1/16f, .7);
    protected static final VoxelShape SHAPE1 = Shapes.box(.3, 0, .25, .7, 1/16f, .75);

    public Disc(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Disc::new);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return direction == Direction.NORTH || direction == Direction.SOUTH ? SHAPE : SHAPE1;
    }
}
