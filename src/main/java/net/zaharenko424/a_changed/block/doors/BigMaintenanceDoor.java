package net.zaharenko424.a_changed.block.doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

public class BigMaintenanceDoor extends Abstract3By3Door {

    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_OPEN;
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public BigMaintenanceDoor(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext pContext) {
        boolean open = state.getValue(OPEN);
        int partId = state.getValue(PART);
        return CACHE.getShape(state.getValue(FACING), partId + (open ? 10 : 0),
                PARTS.get(partId).alignShape(open ? SHAPE_OPEN : SHAPE));
    }

    static {
        SHAPE = Shapes.or(Shapes.box(-2f, 0f, 0f, 1f, 0.0938f, 1f),
                Shapes.box(-2f, 2.8438f, 0f, 1f, 3f, 1f),
                Shapes.box(0.8125f, 0.0938f, 0f, 1f, 2.8438f, 1f),
                Shapes.box(-2f, 0.0938f, 0f, -1.8125f, 2.8438f, 1f),
                Shapes.box(-1.8125f, 0.0938f, 0.3125f, 0.8125f, 2.8438f, 0.6875f),
                Shapes.box(-1.8125f, 1.375f, 0.25f, 0.8125f, 1.5312f, 0.75f),
                Shapes.box(-1.8125f, 0.75f, 0.2812f, 0.8125f, 1.3125f, 0.7188f),
                Shapes.box(-1.8125f, 1.5938f, 0.2812f, 0.8125f, 2.1562f, 0.7188f));
        SHAPE_OPEN = Shapes.or(Shapes.box(-2f, 0f, 0f, 1f, 0.0938f, 1f),
                Shapes.box(-2f, 2.8438f, 0f, 1f, 3f, 1f),
                Shapes.box(0.8125f, 0.0938f, 0f, 1f, 2.8438f, 1f),
                Shapes.box(-2f, 0.0938f, 0f, -1.8125f, 2.8438f, 1f),
                Shapes.box(-1.8125f, 0.0938f, 0.3125f, 0.8125f, 0.4062f, 0.6875f),
                Shapes.box(-1.8125f, 2.5312f, 0.25f, 0.8125f, 2.6875f, 0.75f),
                Shapes.box(-1.8125f, 0.0938f, 0.2812f, 0.8125f, 0.2188f, 0.7188f),
                Shapes.box(-1.8125f, 2.7188f, 0.2812f, 0.8125f, 2.8438f, 0.7188f),
                Shapes.box(-1.8125f, 0.2812f, 0.25f, 0.8125f, 0.4375f, 0.75f),
                Shapes.box(-1.8125f, 2.5312f, 0.3125f, 0.8125f, 2.8438f, 0.6875f),
                Shapes.box(-1.8125f, 0.4375f, 0.3125f, -1.7938f, 2.5625f, 0.6875f),
                Shapes.box(0.7937f, 0.4375f, 0.3125f, 0.8125f, 2.5625f, 0.6875f));
    }
}