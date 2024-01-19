package net.zaharenko424.a_changed.block.doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LibraryDoor extends Abstract2By2Door {

    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_OPEN;
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public LibraryDoor(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        boolean open = p_60555_.getValue(OPEN);
        int partId = p_60555_.getValue(PART);
        return CACHE.getShape(p_60555_.getValue(FACING), partId + (open ? 10 : 0),
                PARTS.get(partId).alignShape(open ? SHAPE_OPEN : SHAPE));
    }

    static {
        SHAPE = Shapes.or(Shapes.box(-1f, 0f, 0f, 1f, 0.0625f, 1f),
                Shapes.box(-1f, 1.875f, 0f, 1f, 2f, 1f),
                Shapes.box(0.875f, 0.0625f, 0f, 1f, 1.875f, 1f),
                Shapes.box(-1f, 0.0625f, 0f, -0.875f, 1.875f, 1f),
                Shapes.box(-0.875f, 0.0625f, 0.3125f, 0.875f, 1.875f, 0.6875f),
                Shapes.box(-0.0625f, 0.0625f, 0.25f, 0.0625f, 1.875f, 0.75f),
                Shapes.box(-0.875f, 0.5625f, 0.3f, 0.875f, 0.8125f, 0.7f),
                Shapes.box(-0.875f, 0.5f, 0.2812f, 0.875f, 0.5625f, 0.7188f),
                Shapes.box(-0.875f, 0.8125f, 0.2812f, 0.875f, 0.875f, 0.7188f));
        SHAPE_OPEN = Shapes.or(Shapes.box(-1f, 0f, 0f, 1f, 0.0625f, 1f),
                Shapes.box(-1f, 1.875f, 0f, 1f, 2f, 1f),
                Shapes.box(0.875f, 0.0625f, 0f, 1f, 1.875f, 1f),
                Shapes.box(-1f, 0.0625f, 0f, -0.875f, 1.875f, 1f),
                Shapes.box(0.75f, 0.0625f, 0.3125f, 0.875f, 1.875f, 0.6875f),
                Shapes.box(-0.7812f, 0.0625f, 0.25f, -0.7188f, 1.875f, 0.75f),
                Shapes.box(0.7188f, 0.0625f, 0.25f, 0.7812f, 1.875f, 0.75f),
                Shapes.box(0.7812f, 0.5625f, 0.3f, 0.875f, 0.8125f, 0.7f),
                Shapes.box(0.7812f, 0.5f, 0.2812f, 0.875f, 0.5625f, 0.7188f),
                Shapes.box(0.7812f, 0.8125f, 0.2812f, 0.875f, 0.875f, 0.7188f),
                Shapes.box(-0.875f, 0.8125f, 0.2812f, -0.7812f, 0.875f, 0.7188f),
                Shapes.box(-0.875f, 0.5625f, 0.3f, -0.7812f, 0.8125f, 0.7f),
                Shapes.box(-0.875f, 0.5f, 0.2812f, -0.7812f, 0.5625f, 0.7188f),
                Shapes.box(-0.875f, 0.0625f, 0.3125f, -0.75f, 1.875f, 0.6875f));
    }
}