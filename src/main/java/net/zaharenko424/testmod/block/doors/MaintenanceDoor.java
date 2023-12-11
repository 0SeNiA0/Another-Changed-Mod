package net.zaharenko424.testmod.block.doors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class MaintenanceDoor extends AbstractTwoByTwoDoor{

    private static final VoxelShape SHAPE_0= Shapes.or(Shapes.box(-1, 0, 0, 1, 0.0625, 1)
            ,Shapes.box(-1, 1.875, 0, 1, 2, 1)
            ,Shapes.box(0.875, 0.0625, 0, 1, 1.875, 1)
            ,Shapes.box(-1, 0.0625, 0, -0.875, 1.875, 1)
            ,Shapes.box(-0.875, 0.0625, 0.3125, 0.875, 1.875, 0.6875));
    private static final VoxelShape SHAPE_0_OPEN= Shapes.or(Shapes.box(-1, 0, 0, 1, 0.0625, 1)
            ,Shapes.box(-1, 1.875, 0, 1, 2, 1)
            ,Shapes.box(0.875, 0.0625, 0, 1, 1.875, 1)
            ,Shapes.box(-1, 0.0625, 0, -0.875, 1.875, 1)
            ,Shapes.box(-0.875, 1.75, 0.3125, 0.875, 1.875, 0.6875)
            ,Shapes.box(-0.875, 0.0625, 0.3125, 0.875, 0.1875, 0.6875));
    private static final VoxelShape SHAPE_1=SHAPE_0.move(0,-1,0);
    private static final VoxelShape SHAPE_1_OPEN=SHAPE_0_OPEN.move(0,-1,0);
    private static final VoxelShape SHAPE_2=SHAPE_1.move(1,0,0);
    private static final VoxelShape SHAPE_2_OPEN=SHAPE_1_OPEN.move(1,0,0);
    private static final VoxelShape SHAPE_3=SHAPE_2.move(0,1,0);
    private static final VoxelShape SHAPE_3_OPEN=SHAPE_2_OPEN.move(0,1,0);

    public MaintenanceDoor(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction=p_60555_.getValue(FACING);
        boolean open=p_60555_.getValue(OPEN);
        return Utils.rotateShape(direction,switch(p_60555_.getValue(PART)){
            default -> open?SHAPE_0_OPEN:SHAPE_0;
            case 1-> open?SHAPE_1_OPEN:SHAPE_1;
            case 2-> open?SHAPE_2_OPEN:SHAPE_2;
            case 3-> open?SHAPE_3_OPEN:SHAPE_3;
        });
    }
}