package net.zaharenko424.testmod.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.block.AbstractNote;
import net.zaharenko424.testmod.entity.block.NoteEntity;
import net.zaharenko424.testmod.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Notepad extends AbstractNote {

    private static final VoxelShape SHAPE_NORTH = Shapes.box(0.25, 0, 0.125, 0.75, 0.0625, 0.8125);
    private static final VoxelShape SHAPE_EAST = Utils.rotateShape(Direction.EAST, SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = Utils.rotateShape(Direction.SOUTH, SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = Utils.rotateShape(Direction.WEST, SHAPE_NORTH);

    public Notepad(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public int guiId() {
        return 1;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new NoteEntity(p_153215_,p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (p_60555_.getValue(FACING)){
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos pos=p_60527_.below();
        return p_60526_.getBlockState(pos).isFaceSturdy(p_60526_,pos,Direction.UP);
    }
}