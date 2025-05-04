package net.zaharenko424.a_changed.block.smalldecor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TrafficCone extends SmallDecorBlock {

    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.0625, 0.8125),
            Shapes.box(0.3125, 0.0625, 0.3125, 0.6875, 0.5, 0.6875),
            Shapes.box(0.375, 0.5, 0.375, 0.625, 0.875, 0.625));

    public TrafficCone(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(TrafficCone::new);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}