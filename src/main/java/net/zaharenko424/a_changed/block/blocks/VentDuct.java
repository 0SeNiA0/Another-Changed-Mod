package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import net.zaharenko424.a_changed.util.StateProperties;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

public class VentDuct extends ConnectedTextureBlock {

    private static final VoxelShape SHAPE;
    //private static final VoxelShape SHAPE_S, SH_S_UP, SH_S_DOWN, SH_S_NORTH, SH_S_SOUTH, SH_S_WEST, SH_S_EAST;
    private static final VoxelShape SH_UP, SH_DOWN, SH_NORTH, SH_SOUTH, SH_WEST, SH_EAST;
    private static final VoxelShape SH_S_UP, SH_S_NORTH;
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    public static final AABB INSIDE;
    public static final IntegerProperty FLAGS = StateProperties.FLAGS3;

    public VentDuct(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(FLAGS, 0));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        int i = 0;
        boolean[] ar = new boolean[7];
        for (Direction direction : Direction.values()){
            ar[i++] = state.getValue(propByDirection.get(direction));
        }
        boolean b = state.getValue(FLAGS) > 0;
        ar[i] = b;

        int id = Utils.booleansToInt(ar);
        if(id == 63) return SHAPE;

        return CACHE.getShape(Direction.NORTH, id, () -> {
            VoxelShape shape;
            if(b){
                if(state.getValue(UP)) return SH_S_UP;
                if(state.getValue(NORTH)) return SH_S_NORTH;
                return Utils.rotateShape(Direction.EAST, SH_S_NORTH);

                /*shape = SHAPE_S;              //uncomment in case if small corners will be added
                if(state.getValue(UP)) shape = Shapes.join(shape, SH_S_UP, BooleanOp.NOT_SAME);//uhm actually that's XOR
                if(state.getValue(DOWN)) shape = Shapes.join(shape, SH_S_DOWN, BooleanOp.NOT_SAME);
                if(state.getValue(NORTH)) shape = Shapes.join(shape, SH_S_NORTH, BooleanOp.NOT_SAME);
                if(state.getValue(SOUTH)) shape = Shapes.join(shape, SH_S_SOUTH, BooleanOp.NOT_SAME);
                if(state.getValue(WEST)) shape = Shapes.join(shape, SH_S_WEST, BooleanOp.NOT_SAME);
                if(state.getValue(EAST)) shape = Shapes.join(shape, SH_S_EAST, BooleanOp.NOT_SAME);*/
                //return shape;
            }
            shape = SHAPE;
            if(!state.getValue(UP)) shape = Shapes.or(shape, SH_UP);
            if(!state.getValue(DOWN)) shape = Shapes.or(shape, SH_DOWN);
            if(!state.getValue(NORTH)) shape = Shapes.or(shape, SH_NORTH);
            if(!state.getValue(SOUTH)) shape = Shapes.or(shape, SH_SOUTH);
            if(!state.getValue(WEST)) shape = Shapes.or(shape, SH_WEST);
            if(!state.getValue(EAST)) shape = Shapes.or(shape, SH_EAST);
            return shape;
        });
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, Player player, @NotNull BlockHitResult hitResult) {
        if(player.getInBlockState().is(this) || !player.isCrouching()
                || player.distanceToSqr(hitResult.getLocation()) > 9
                || !getShape(state, level, pos, CollisionContext.empty()).bounds().deflate(.5 / 16).move(pos).contains(hitResult.getLocation()))
            return super.useWithoutItem(state, level, pos, player, hitResult);
        Vec3 vec = pos.getCenter();
        player.teleportTo(vec.x, vec.y, vec.z);
        player.setPose(Pose.SWIMMING);
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        return updateState(state.setValue(propByDirection.get(direction), shouldConnectTo(neighborState, neighborPos, level, direction)), level, pos);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return updateState(super.getStateForPlacement(context), context.getLevel(), context.getClickedPos());
    }

    private @NotNull BlockState updateState(BlockState state, LevelAccessor level, BlockPos pos){
        int sides = 0;
        boolean bars = true;
        Direction dir = null;
        BlockState state1;
        for(Direction direction : Direction.values()){
            if(!state.getValue(propByDirection.get(direction))) continue;
            state1 = level.getBlockState(pos.relative(direction));
            if(!state1.is(this)) continue;
            if(dir == null) dir = direction;
            sides++;
            if(sides > 2 || state1.getValue(FLAGS) == 2) bars = false;
        }

        boolean small = sides == 2 && state.getValue(propByDirection.get(dir.getOpposite()));
        bars = bars && small;

        return state.setValue(FLAGS, bars ? 2 : small ? 1 : 0);
    }

    @Override
    protected boolean shouldConnectTo(@NotNull BlockState state, BlockPos pos, LevelAccessor level, Direction direction) {
        return super.shouldConnectTo(state, pos, level, direction)
                || state.is(BlockRegistry.VENT_HATCH)
                    && ((direction == Direction.UP && state.getValue(BlockStateProperties.HALF) == Half.BOTTOM)
                        || (direction == Direction.DOWN && state.getValue(BlockStateProperties.HALF) == Half.TOP)
                        || state.getValue(BlockStateProperties.HORIZONTAL_FACING) == direction);
    }

    @Override
    public boolean isLadder(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        return state.getValue(UP) || state.getValue(DOWN);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        builder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, FLAGS);
    }

    static {
        SHAPE = Shapes.or(
                Shapes.box(0.9375f, 0.0625f, 0, 1, 0.9375f, 0.0625f),
                Shapes.box(0, 0.0625f, 0, 0.0625f, 0.9375f, 0.0625f),
                Shapes.box(0, 0.9375f, 0, 1, 1, 0.0625f),
                Shapes.box(0, 0.9375f, 0.9375f, 1, 1, 1),
                Shapes.box(0, 0.9375f, 0.0625f, 0.0625f, 1, 0.9375f),
                Shapes.box(0.9375f, 0.9375f, 0.0625f, 1, 1, 0.9375f),
                Shapes.box(0.9375f, 0, 0.0625f, 1, 0.0625f, 0.9375f),
                Shapes.box(0, 0, 0.0625f, 0.0625f, 0.0625f, 0.9375f),
                Shapes.box(0, 0, 0, 1, 0.0625f, 0.0625f),
                Shapes.box(0, 0, 0.9375f, 1, 0.0625f, 1),
                Shapes.box(0.9375f, 0.0625f, 0.9375f, 1, 0.9375f, 1),
                Shapes.box(0, 0.0625f, 0.9375f, 0.0625f, 0.9375f, 1));
        SH_UP = Shapes.box(0.0625f, 0.9375f, 0.0625f, 0.9375f, 1, 0.9375f);
        SH_DOWN = Shapes.box(0.0625f, 0, 0.0625f, 0.9375f, 0.0625f, 0.9375f);
        SH_NORTH = Shapes.box(0.0625f, 0.0625f, 0, 0.9375f, 0.9375f, 0.0625f);
        SH_SOUTH = Utils.rotateShape(Direction.SOUTH, SH_NORTH);
        SH_WEST = Utils.rotateShape(Direction.WEST, SH_NORTH);
        SH_EAST = Utils.rotateShape(Direction.EAST, SH_NORTH);
        SH_S_UP = Shapes.or(
                Shapes.box(0.875f, 0.9375f, 0.125f, 0.9375f, 1, 0.875f),
                Shapes.box(0.0625f, 0.9375f, 0.125f, 0.125f, 1, 0.875f),
                Shapes.box(0.0625f, 0.9375f, 0.875f, 0.9375f, 1, 0.9375f),
                Shapes.box(0.0625f, 0, 0.875f, 0.9375f, 0.0625f, 0.9375f),
                Shapes.box(0.0625f, 0.0625f, 0.875f, 0.125f, 0.9375f, 0.9375f),
                Shapes.box(0.875f, 0.0625f, 0.875f, 0.9375f, 0.9375f, 0.9375f),
                Shapes.box(0.875f, 0.0625f, 0.0625f, 0.9375f, 0.9375f, 0.125f),
                Shapes.box(0.0625f, 0.0625f, 0.0625f, 0.125f, 0.9375f, 0.125f),
                Shapes.box(0.0625f, 0.9375f, 0.0625f, 0.9375f, 1, 0.125f),
                Shapes.box(0.0625f, 0, 0.0625f, 0.9375f, 0.0625f, 0.125f),
                Shapes.box(0.875f, 0, 0.125f, 0.9375f, 0.0625f, 0.875f),
                Shapes.box(0.0625f, 0, 0.125f, 0.125f, 0.0625f, 0.875f),
                Shapes.box(0.0625f, 0.0625f, 0.125f, 0.125f, 0.9375f, 0.875f),
                Shapes.box(0.875f, 0.0625f, 0.125f, 0.9375f, 0.9375f, 0.875f),
                Shapes.box(0.125f, 0.0625f, 0.875f, 0.875f, 0.9375f, 0.9375f),
                Shapes.box(0.125f, 0.0625f, 0.0625f, 0.875f, 0.9375f, 0.125f));
        SH_S_NORTH = Shapes.or(
                Shapes.box(0.875f, 0.125f, 0, 0.9375f, 0.875f, 0.0625f),
                Shapes.box(0.0625f, 0.125f, 0, 0.125f, 0.875f, 0.0625f),
                Shapes.box(0.0625f, 0.875f, 0, 0.9375f, 0.9375f, 0.0625f),
                Shapes.box(0.0625f, 0.875f, 0.9375f, 0.9375f, 0.9375f, 1),
                Shapes.box(0.0625f, 0.875f, 0.0625f, 0.125f, 0.9375f, 0.9375f),
                Shapes.box(0.875f, 0.875f, 0.0625f, 0.9375f, 0.9375f, 0.9375f),
                Shapes.box(0.875f, 0.0625f, 0.0625f, 0.9375f, 0.125f, 0.9375f),
                Shapes.box(0.0625f, 0.0625f, 0.0625f, 0.125f, 0.125f, 0.9375f),
                Shapes.box(0.0625f, 0.0625f, 0, 0.9375f, 0.125f, 0.0625f),
                Shapes.box(0.0625f, 0.0625f, 0.9375f, 0.9375f, 0.125f, 1),
                Shapes.box(0.875f, 0.125f, 0.9375f, 0.9375f, 0.875f, 1),
                Shapes.box(0.0625f, 0.125f, 0.9375f, 0.125f, 0.875f, 1),
                Shapes.box(0.0625f, 0.125f, 0.0625f, 0.125f, 0.875f, 0.9375f),
                Shapes.box(0.875f, 0.125f, 0.0625f, 0.9375f, 0.875f, 0.9375f),
                Shapes.box(0.125f, 0.875f, 0.0625f, 0.875f, 0.9375f, 0.9375f),
                Shapes.box(0.125f, 0.0625f, 0.0625f, 0.875f, 0.125f, 0.9375f));
        /*SHAPE_S = Shapes.or(                  //uncomment in case if small corners will be added
                Shapes.box(0.875f, 0.125f, 0.0625f, 0.9375f, 0.875f, 0.125f),
                Shapes.box(0.0625f, 0.125f, 0.0625f, 0.125f, 0.875f, 0.125f),
                Shapes.box(0.0625f, 0.875f, 0.0625f, 0.9375f, 0.9375f, 0.125f),
                Shapes.box(0.0625f, 0.875f, 0.875f, 0.9375f, 0.9375f, 0.9375f),
                Shapes.box(0.0625f, 0.875f, 0.125f, 0.125f, 0.9375f, 0.875f),
                Shapes.box(0.875f, 0.875f, 0.125f, 0.9375f, 0.9375f, 0.875f),
                Shapes.box(0.875f, 0.0625f, 0.125f, 0.9375f, 0.125f, 0.875f),
                Shapes.box(0.0625f, 0.0625f, 0.125f, 0.125f, 0.125f, 0.875f),
                Shapes.box(0.0625f, 0.0625f, 0.0625f, 0.9375f, 0.125f, 0.125f),
                Shapes.box(0.0625f, 0.0625f, 0.875f, 0.9375f, 0.125f, 0.9375f),
                Shapes.box(0.875f, 0.125f, 0.875f, 0.9375f, 0.875f, 0.9375f),
                Shapes.box(0.0625f, 0.125f, 0.875f, 0.125f, 0.875f, 0.9375f),
                Shapes.box(0.0625f, 0.125f, 0.125f, 0.125f, 0.875f, 0.875f),
                Shapes.box(0.875f, 0.125f, 0.125f, 0.9375f, 0.875f, 0.875f),
                Shapes.box(0.125f, 0.125f, 0.875f, 0.875f, 0.875f, 0.9375f),
                Shapes.box(0.125f, 0.125f, 0.0625f, 0.875f, 0.875f, 0.125f),
                Shapes.box(0.125f, 0.875f, 0.125f, 0.875f, 0.9375f, 0.875f),
                Shapes.box(0.125f, 0.0625f, 0.125f, 0.875f, 0.125f, 0.875f));
        SH_S_UP = Shapes.or(
                Shapes.box(0.0625f, 0.9375f, 0.0625f, 0.9375f, 1, 0.125f),
                Shapes.box(0.0625f, 0.9375f, 0.875f, 0.9375f, 1, 0.9375f),
                Shapes.box(0.0625f, 0.9375f, 0.125f, 0.125f, 1, 0.875f),
                Shapes.box(0.875f, 0.9375f, 0.125f, 0.9375f, 1, 0.875f),
                Shapes.box(0.125f, 0.875f, 0.125f, 0.875f, 0.9375f, 0.875f));
        SH_S_DOWN = Shapes.or(
                Shapes.box(0.0625f, 0, 0.0625f, 0.9375f, 0.0625f, 0.125f),
                Shapes.box(0.0625f, 0, 0.875f, 0.9375f, 0.0625f, 0.9375f),
                Shapes.box(0.0625f, 0, 0.125f, 0.125f, 0.0625f, 0.875f),
                Shapes.box(0.875f, 0, 0.125f, 0.9375f, 0.0625f, 0.875f),
                Shapes.box(0.125f, 0.0625f, 0.125f, 0.875f, 0.125f, 0.875f));
        SH_S_NORTH = Shapes.or(
                Shapes.box(0.875f, 0.125f, 0, 0.9375f, 0.875f, 0.0625f),
                Shapes.box(0.0625f, 0.125f, 0, 0.125f, 0.875f, 0.0625f),
                Shapes.box(0.0625f, 0.875f, 0, 0.9375f, 0.9375f, 0.0625f),
                Shapes.box(0.0625f, 0.0625f, 0, 0.9375f, 0.125f, 0.0625f),
                Shapes.box(0.125f, 0.125f, 0.0625f, 0.875f, 0.875f, 0.125f));
        SH_S_SOUTH = Utils.rotateShape(Direction.SOUTH, SH_S_NORTH);
        SH_S_WEST = Utils.rotateShape(Direction.WEST, SH_S_NORTH);
        SH_S_EAST = Utils.rotateShape(Direction.EAST, SH_S_NORTH);*/
        INSIDE = SHAPE.bounds().deflate(1f / 16);
    }
}