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
public class LabDoor extends AbstractTwoByTwoDoor {

    private static final VoxelShape SHAPE_0 = Shapes.or(Shapes.box(-1, 0, 0, 1, 0.0625, 1)
            ,Shapes.box(-1, 1.875, 0, 1, 2, 1)
            ,Shapes.box(0.875, 0.0625, 0, 1, 1.875, 1)
            ,Shapes.box(-1, 0.0625, 0, -0.875, 1.875, 1)
            ,Shapes.box(-0.875, 0.0625, 0.3125, 0.875, 1.875, 0.6875));
    private static final VoxelShape SHAPE_0_OPEN = Shapes.or(Shapes.box(-1, 0, 0, 1, 0.0625, 1)
            ,Shapes.box(-1, 1.875, 0, 1, 2, 1)
            ,Shapes.box(0.875, 0.0625, 0, 1, 1.875, 1)
            ,Shapes.box(-1, 0.0625, 0, -0.875, 1.875, 1)
            ,Shapes.box(-0.875, 0.0625, 0.3125, -0.5625, 0.75, 0.6875)
            ,Shapes.box(0.5625, 1.1875, 0.3125, 0.875, 1.875, 0.6875));
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public LabDoor(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        boolean open = p_60555_.getValue(OPEN);
        int partId = p_60555_.getValue(PART);
        return CACHE.getShape(p_60555_.getValue(FACING), partId + (open ? 10 : 0),
                PARTS.get(partId).alignShape(open ? SHAPE_0_OPEN : SHAPE_0));
    }
}