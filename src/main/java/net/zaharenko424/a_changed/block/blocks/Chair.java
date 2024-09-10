package net.zaharenko424.a_changed.block.blocks;

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
import net.zaharenko424.a_changed.block.ISeatBlock;
import net.zaharenko424.a_changed.entity.SeatEntity;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Chair extends HorizontalDirectionalBlock implements ISeatBlock<SeatEntity> {

    private static final VoxelShape SHAPE_NORTH, SHAPE_EAST, SHAPE_SOUTH, SHAPE_WEST;

    public Chair(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Chair::new);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        return switch (p_60555_.getValue(FACING)) {
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    public boolean use(@NotNull BlockState p_60503_, @NotNull Level p_60504_, @NotNull BlockPos p_60505_, @NotNull Player p_60506_) {
        if (p_60504_.isClientSide) return false;
        return sit(p_60504_, p_60505_, Shapes.block().bounds().move(p_60505_), p_60506_, true);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        return defaultBlockState().setValue(FACING, p_49820_.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, level, pos, pNewState, pMovedByPiston);
        removeSeat(level, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING));
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