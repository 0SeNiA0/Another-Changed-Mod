package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.zaharenko424.a_changed.block.ISeatBlock;
import net.zaharenko424.a_changed.entity.SeatEntity;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Chair extends HorizontalDirectionalBlock implements ISeatBlock {

    private static final VoxelShape SHAPE_NORTH = Shapes.or(Shapes.box(0.4375, 0, 0.4375, 0.5625, 0.4375, 0.5625)
            ,Shapes.box(0.125, 0.4375, 0.125, 0.875, 0.5625, 0.875)
            ,Shapes.box(0.125, 0.5625, 0.75, 0.875, 1.125, 0.875)
            ,Shapes.box(0.125, 0, 0.4375, 0.875, 0.125, 0.5625)
            ,Shapes.box(0.4375, 0, 0.125, 0.5625, 0.125, 0.875));
    private static final VoxelShape SHAPE_EAST = Utils.rotateShape(Direction.EAST,SHAPE_NORTH);
    private static final VoxelShape SHAPE_SOUTH = Utils.rotateShape(Direction.SOUTH,SHAPE_NORTH);
    private static final VoxelShape SHAPE_WEST = Utils.rotateShape(Direction.WEST,SHAPE_NORTH);

    public Chair(Properties p_54120_) {
        super(p_54120_);
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
        if(p_60504_.isClientSide) return super.use(p_60503_,p_60504_,p_60505_,p_60506_,p_60507_,p_60508_);
        if(sit(p_60504_,p_60505_,Shapes.block().bounds().move(p_60505_),p_60506_,true)) return InteractionResult.SUCCESS;
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        return defaultBlockState().setValue(FACING,p_49820_.getHorizontalDirection().getOpposite());
    }

    @Override
    public void onPlace(@NotNull BlockState p_60566_, @NotNull Level p_60567_, @NotNull BlockPos p_60568_, @NotNull BlockState p_60569_, boolean p_60570_) {
        if(p_60567_.isClientSide) super.onPlace(p_60566_, p_60567_, p_60568_, p_60569_, p_60570_);
        p_60567_.addFreshEntity(new SeatEntity(p_60567_,p_60568_,true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING);
    }
}