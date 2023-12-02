package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@SuppressWarnings("deprecation")
public class Scanner extends HorizontalDirectionalBlock {
    private static final VoxelShape SHAPE_NORTH = Shapes.box(0.0625, 0.1875, 0.625, 0.9375, 0.8125, 1);
    private static final VoxelShape SHAPE_EAST = Shapes.box(0, 0.1875, 0.0625, 0.375, 0.8125, 0.9375);
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(0.0625, 0.1875, 0, 0.9375, 0.8125, 0.375);
    private static final VoxelShape SHAPE_WEST = Shapes.box(0.625, 0.1875, 0.0625, 1, 0.8125, 0.9375);

    public Scanner(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        return switch (p_60555_.getValue(FACING)){
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState p_60503_, @NotNull Level p_60504_, @NotNull BlockPos p_60505_, @NotNull Player p_60506_, @NotNull InteractionHand p_60507_, @NotNull BlockHitResult p_60508_) {
        if(!p_60504_.isClientSide) ((ServerPlayer)p_60506_).setRespawnPosition(p_60504_.dimension(),p_60506_.blockPosition(),p_60506_.getYHeadRot(),false,true);
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        return defaultBlockState().setValue(FACING,p_49820_.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }
}