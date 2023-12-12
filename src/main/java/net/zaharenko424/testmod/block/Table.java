package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class Table extends Block {

    private static final VoxelShape TOP = Shapes.box(0, 0.875, 0, 1, 1, 1);
    private static final VoxelShape LEG_1_ = Shapes.box(0.75, 0, 0.125, 0.875, 0.875, 0.25);
    private static final VoxelShape LEG_2_ = Utils.rotateShape(Direction.EAST,LEG_1_);
    private static final VoxelShape LEG_3_ = Utils.rotateShape(Direction.SOUTH,LEG_1_);
    private static final VoxelShape LEG_4_ = Utils.rotateShape(Direction.WEST,LEG_1_);
    public static final BooleanProperty LEG_1 = BooleanProperty.create("leg_1");
    public static final BooleanProperty LEG_2 = BooleanProperty.create("leg_2");
    public static final BooleanProperty LEG_3 = BooleanProperty.create("leg_3");
    public static final BooleanProperty LEG_4 = BooleanProperty.create("leg_4");

    public Table(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(LEG_1,true).setValue(LEG_2,true)
                .setValue(LEG_3,true).setValue(LEG_4,true));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        VoxelShape table = TOP;
        if(p_60555_.getValue(LEG_1)) table=Shapes.or(table,LEG_1_);
        if(p_60555_.getValue(LEG_2)) table=Shapes.or(table,LEG_2_);
        if(p_60555_.getValue(LEG_3)) table=Shapes.or(table,LEG_3_);
        if(p_60555_.getValue(LEG_4)) table=Shapes.or(table,LEG_4_);
        return table;
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState p_60541_, @NotNull Direction p_60542_, @NotNull BlockState p_60543_, @NotNull LevelAccessor p_60544_, @NotNull BlockPos p_60545_, @NotNull BlockPos p_60546_) {
        return getConnections(p_60541_,p_60544_,p_60545_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        return getConnections(defaultBlockState(),p_49820_.getLevel(),p_49820_.getClickedPos());
    }

    public BlockState getConnections(@NotNull BlockState state, @NotNull LevelAccessor level, @NotNull BlockPos pos) {
        boolean n = validConnection(level.getBlockState(pos.north()));
        boolean e = validConnection(level.getBlockState(pos.east()));
        boolean s = validConnection(level.getBlockState(pos.south()));
        boolean w = validConnection(level.getBlockState(pos.west()));
        boolean leg1 = (!n && !e) || (n && e && !(validConnection(level.getBlockState(pos.north().east()))));
        boolean leg2 = (!e && !s) || (e && s && !(validConnection(level.getBlockState(pos.south().east()))));
        boolean leg3 = (!s && !w) || (s && w && !(validConnection(level.getBlockState(pos.south().west()))));
        boolean leg4 = (!n && !w) || (n && w && !(validConnection(level.getBlockState(pos.north().west()))));
        return state.setValue(LEG_1, leg1).setValue(LEG_2, leg2).setValue(LEG_3, leg3).setValue(LEG_4, leg4);
    }

    public boolean validConnection(@NotNull BlockState state) {
        return state.is(this);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(LEG_1,LEG_2,LEG_3,LEG_4);
    }
}