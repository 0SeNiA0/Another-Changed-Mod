package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.util.Utils;
import org.jetbrains.annotations.NotNull;
//Probably a bit too overboard for a trapdoor but hey, it works
public class Vent extends TrapDoorBlock {
    private static final VoxelShape TOP = Shapes.or(Shapes.box(0, 0.8125, 0, 1, 1, 0.09375),
            Shapes.box(0, 0.8125, 0.90625, 1, 1, 1),
            Shapes.box(0.875, 0.8125, 0.09375, 1, 1, 0.90625),
            Shapes.box(0, 0.8125, 0.09375, 0.125, 1, 0.90625),
            Shapes.box(0.125, 0.8125, 0.675, 0.875, 1, 0.8),
            Shapes.box(0.125, 0.8125, 0.4375, 0.875, 1, 0.5625),
            Shapes.box(0.125, 0.8125, 0.2, 0.875, 1, 0.325));
    private static final VoxelShape TOP_EAST = Utils.rotateShape(Direction.EAST,TOP);
    private static final VoxelShape TOP_SOUTH = Utils.rotateShape(Direction.SOUTH,TOP);
    private static final VoxelShape TOP_WEST = Utils.rotateShape(Direction.WEST,TOP);
    private static final VoxelShape BOTTOM = TOP.move(0,-.8125,0);
    private static final VoxelShape BOTTOM_EAST = TOP_EAST.move(0,-.8125,0);
    private static final VoxelShape BOTTOM_SOUTH = TOP_SOUTH.move(0,-.8125,0);
    private static final VoxelShape BOTTOM_WEST = TOP_WEST.move(0,-.8125,0);
    private static final VoxelShape OPEN_NORTH = Shapes.or(Shapes.box(0.125, 0.2, 0.8125, 0.875, 0.325, 1),
            Shapes.box(0.125, 0.4375, 0.8125, 0.875, 0.5625, 1),
            Shapes.box(0.125, 0.675, 0.8125, 0.875, 0.8, 1),
            Shapes.box(0, 0.09375, 0.8125, 0.125, 0.90625, 1),
            Shapes.box(0.875, 0.09375, 0.8125, 1, 0.90625, 1),
            Shapes.box(0, 0.90625, 0.8125, 1, 1, 1),
            Shapes.box(0, 0, 0.8125, 1, 0.09375, 1));
    private static final VoxelShape OPEN_EAST = Utils.rotateShape(Direction.EAST,OPEN_NORTH);
    private static final VoxelShape OPEN_SOUTH = Utils.rotateShape(Direction.SOUTH,OPEN_NORTH);
    private static final VoxelShape OPEN_WEST = Utils.rotateShape(Direction.WEST,OPEN_NORTH);


    public Vent(Properties p_273079_, BlockSetType p_272964_) {
        super(p_273079_, p_272964_);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_57563_, @NotNull BlockGetter p_57564_, @NotNull BlockPos p_57565_, @NotNull CollisionContext p_57566_) {
        boolean open=p_57563_.getValue(OPEN);
        boolean top=p_57563_.getValue(HALF)==Half.TOP;
        return switch (p_57563_.getValue(FACING)) {
            default -> open?OPEN_NORTH:top?TOP:BOTTOM;
            case SOUTH -> open?OPEN_SOUTH:top?TOP_SOUTH:BOTTOM_SOUTH;
            case WEST -> open?OPEN_WEST:top?TOP_WEST:BOTTOM_WEST;
            case EAST -> open?OPEN_EAST:top?TOP_EAST:BOTTOM_EAST;
        };
    }
}