package net.zaharenko424.a_changed.block.doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

public class BigLabDoor extends Abstract3By3Door {

    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_OPEN;
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public BigLabDoor(Properties p_54120_) {
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
        SHAPE = Shapes.or(Shapes.box(-2f, 0f, 0f, 1f, 0.125f, 1f),
                Shapes.box(-2f, 2.8438f, 0f, 1f, 3f, 1f),
                Shapes.box(0.8125f, 0.125f, 0f, 1f, 2.8438f, 1f),
                Shapes.box(-2f, 0.125f, 0f, -1.8125f, 2.8438f, 1f),
                Shapes.box(-1.8125f, 0.125f, 0.3125f, 0.8125f, 2.8438f, 0.6875f),
                Shapes.box(-0.275f, 0.5875f, 0.25f, -0.175f, 0.7875f, 0.75f),
                Shapes.box(-0.325f, 0.6375f, 0.25f, -0.125f, 0.7375f, 0.75f),
                Shapes.box(-0.275f, 0.3063f, 0.25f, -0.175f, 0.5063f, 0.75f),
                Shapes.box(-0.325f, 0.3562f, 0.25f, -0.125f, 0.4563f, 0.75f),
                Shapes.box(-0.7812f, 2f, 0.2812f, 0.5312f, 2.0938f, 0.7188f),
                Shapes.box(0.4062f, 2.0938f, 0.2812f, 0.5312f, 2.5625f, 0.7188f),
                Shapes.box(-0.7812f, 2.5625f, 0.2812f, 0.5312f, 2.6562f, 0.7188f),
                Shapes.box(-0.7812f, 2.0938f, 0.2812f, -0.6562f, 2.5625f, 0.7188f));
        SHAPE_OPEN = Shapes.or(Shapes.box(-2f, 0f, 0f, 1f, 0.125f, 1f),
                Shapes.box(-2f, 2.8438f, 0f, 1f, 3f, 1f),
                Shapes.box(0.8125f, 0.125f, 0f, 1f, 2.8438f, 1f),
                Shapes.box(-2f, 0.125f, 0f, -1.8125f, 2.8438f, 1f),
                Shapes.box(-1.3125f, 0.125f, 0.2625f, -1.1875f, 1.0625f, 0.7375f),
                Shapes.box(0.1875f, 2f, 0.2625f, 0.3125f, 2.8438f, 0.7375f),
                Shapes.box(-1.525f, 0.5875f, 0.25f, -1.425f, 0.7875f, 0.75f),
                Shapes.box(-1.575f, 0.6375f, 0.25f, -1.375f, 0.7375f, 0.75f),
                Shapes.box(-1.525f, 0.3063f, 0.25f, -1.425f, 0.5063f, 0.75f),
                Shapes.box(-1.575f, 0.3562f, 0.25f, -1.375f, 0.4563f, 0.75f),
                Shapes.box(-1.8125f, 0.125f, 0.3125f, -1.3125f, 1.1812f, 0.6875f),
                Shapes.box(0.3125f, 1.8937f, 0.3125f, 0.8125f, 2.8438f, 0.6875f),
                Shapes.box(-1.8188f, 1.1875f, 0.3125f, -1.4375f, 1.375f, 0.6875f),
                Shapes.box(0.4313f, 1.7188f, 0.3125f, 0.8125f, 1.9062f, 0.6875f));
    }
}