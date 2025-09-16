package net.zaharenko424.a_changed.block.door;

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
public class MaintenanceDoor extends Abstract2By2Door {

    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_OPEN;
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public MaintenanceDoor(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean open = state.getValue(OPEN);
        int partId = state.getValue(PART);
        return CACHE.getShape(state.getValue(FACING), partId + (open ? 10 : 0),
                PARTS.get(partId).alignShape(open ? SHAPE_OPEN : SHAPE));
    }

    static {
        SHAPE = Shapes.or(Shapes.box(-1f, 0f, 0f, 1f, 0.0625f, 1f),
                Shapes.box(-1f, 1.875f, 0f, 1f, 2f, 1f),
                Shapes.box(0.875f, 0.0625f, 0f, 1f, 1.875f, 1f),
                Shapes.box(-1f, 0.0625f, 0f, -0.875f, 1.875f, 1f),
                Shapes.box(-0.875f, 0.0625f, 0.3125f, 0.875f, 1.875f, 0.6875f),
                Shapes.box(-0.875f, 0.9062f, 0.25f, 0.875f, 1.0094f, 0.75f),
                Shapes.box(-0.875f, 0.4688f, 0.2812f, 0.875f, 0.8438f, 0.7188f),
                Shapes.box(-0.875f, 1.0625f, 0.2812f, 0.875f, 1.4375f, 0.7188f));
        SHAPE_OPEN = Shapes.or(Shapes.box(-1f, 0f, 0f, 1f, 0.0625f, 1f),
                Shapes.box(-1f, 1.875f, 0f, 1f, 2f, 1f),
                Shapes.box(0.875f, 0.0625f, 0f, 1f, 1.875f, 1f),
                Shapes.box(-1f, 0.0625f, 0f, -0.875f, 1.875f, 1f),
                Shapes.box(0.8562f, 0.25f, 0.3125f, 0.875f, 1.6875f, 0.6875f),
                Shapes.box(-0.875f, 0.25f, 0.3125f, -0.8562f, 1.6875f, 0.6875f));
    }
}