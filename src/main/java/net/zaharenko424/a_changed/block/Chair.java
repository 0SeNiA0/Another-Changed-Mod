package net.zaharenko424.a_changed.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.entity.SeatEntity;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Chair extends HorizontalDirectionalBlock implements SeatBlock<SeatEntity> {

    private static final VoxelShape SHAPE_NORTH, SHAPE_EAST, SHAPE_SOUTH, SHAPE_WEST;

    public Chair(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Chair::new);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    public boolean use(@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        if (level.isClientSide) return false;
        return sit(level, pos, Shapes.block().bounds().move(pos), player, true);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        return use(level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return use(level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        removeSeat(level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING));
    }

    static {
        SHAPE_NORTH = Shapes.or(
                Shapes.box(0.125f, 0.0938f, 0.75f, 0.25f, 0.4375f, 0.875f),
                Shapes.box(0.125f, 0.0938f, 0.125f, 0.25f, 0.4375f, 0.25f),
                Shapes.box(0.75f, 0.0938f, 0.75f, 0.875f, 0.4375f, 0.875f),
                Shapes.box(0.75f, 0.0938f, 0.125f, 0.875f, 0.4375f, 0.25f),
                Shapes.box(0.125f, 0.4375f, 0.125f, 0.875f, 0.5625f, 0.875f),
                Shapes.box(0.125f, 0.5625f, 0.75f, 0.875f, 1.125f, 0.875f),
                Shapes.box(0.75f, 0, 0.125f, 0.875f, 0.0938f, 0.875f),
                Shapes.box(0.125f, 0, 0.125f, 0.25f, 0.0938f, 0.875f));
        SHAPE_EAST = Utils.rotateShape(Direction.EAST, SHAPE_NORTH);
        SHAPE_SOUTH = Utils.rotateShape(Direction.SOUTH, SHAPE_NORTH);
        SHAPE_WEST = Utils.rotateShape(Direction.WEST, SHAPE_NORTH);
    }
}