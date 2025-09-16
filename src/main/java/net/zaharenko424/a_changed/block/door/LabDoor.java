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
public class LabDoor extends Abstract2By2Door {

    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(-1, 0, 0, 1, 0.0625, 1)
            ,Shapes.box(-1, 1.875, 0, 1, 2, 1)
            ,Shapes.box(0.875, 0.0625, 0, 1, 1.875, 1)
            ,Shapes.box(-1, 0.0625, 0, -0.875, 1.875, 1)
            ,Shapes.box(-0.875, 0.0625, 0.3125, 0.875, 1.875, 0.6875));
    private static final VoxelShape SHAPE_OPEN = Shapes.or(Shapes.box(-1, 0, 0, 1, 0.0625, 1)
            ,Shapes.box(-1, 1.875, 0, 1, 2, 1)
            ,Shapes.box(0.875, 0.0625, 0, 1, 1.875, 1)
            ,Shapes.box(-1, 0.0625, 0, -0.875, 1.875, 1)
            ,Shapes.box(-0.875, 0.0625, 0.3125, -0.5625, 0.75, 0.6875)
            ,Shapes.box(0.5625, 1.1875, 0.3125, 0.875, 1.875, 0.6875));
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public LabDoor(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        boolean open = state.getValue(OPEN);
        int partId = state.getValue(PART);
        return CACHE.getShape(state.getValue(FACING), partId + (open ? 10 : 0),
                PARTS.get(partId).alignShape(open ? SHAPE_OPEN : SHAPE));
    }
}