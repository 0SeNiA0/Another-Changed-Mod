package net.zaharenko424.a_changed.block.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.block.AbstractMultiBlock.Part;
import net.zaharenko424.a_changed.block.NotRotatedMultiBlock;
import net.zaharenko424.a_changed.entity.block.LatexContainerEntity;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LatexContainer extends NotRotatedMultiBlock implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.125, 0.8125)
            ,Shapes.box(0.1875, 1.625, 0.1875, 0.8125, 1.75, 0.8125)
            ,Shapes.box(0.28125, 0.125, 0.28125, 0.71875, 1.625, 0.71875));
    private static final VoxelShape SHAPE_UPPER = SHAPE.move(0,-1,0);
    protected static final ImmutableMap<Integer, Part> PARTS = ImmutableMap.of(
            0, new Part(0, 0, 0), 1, new Part(0, 1, 0));
    public static final IntegerProperty PART = StateProperties.PART2;

    public LatexContainer(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(PART, 0));
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    protected ImmutableMap<Integer, Part> parts() {
        return PARTS;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return p_153216_.getValue(PART) == 1 ? null : new LatexContainerEntity(p_153215_, p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return p_60555_.getValue(PART) == 0 ? SHAPE : SHAPE_UPPER;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide) return InteractionResult.CONSUME_PARTIAL;

        BlockPos pos1 = state.getValue(PART) == 0 ? pos : pos.below();
        BlockEntity entity = level.getBlockEntity(pos1);
        if(!(entity instanceof LatexContainerEntity container) || container.isEmpty()) return InteractionResult.PASS;

        ItemHandlerHelper.giveItemToPlayer(player, container.removeLatex());
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide) return ItemInteractionResult.CONSUME_PARTIAL;

        BlockPos pos1 = state.getValue(PART) == 0 ? pos : pos.below();
        BlockEntity entity = level.getBlockEntity(pos1);
        if(!(entity instanceof LatexContainerEntity container) || !container.hasSpace(stack.getItem()))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        container.addLatex(stack, !player.isCreative());
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pMovedByPiston) {
        pLevel.invalidateCapabilities(pPos);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(!p_60516_.isClientSide && !p_60515_.is(p_60518_.getBlock()) && p_60515_.getValue(PART) == 0){
            if(p_60516_.getBlockEntity(p_60517_) instanceof LatexContainerEntity container) container.onRemove();
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
        p_60516_.invalidateCapabilities(p_60517_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        BlockPos blockpos = p_49820_.getClickedPos();
        Level level = p_49820_.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_49820_)) {
            return defaultBlockState();
        } else return null;
    }
}