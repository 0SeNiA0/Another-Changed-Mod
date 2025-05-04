package net.zaharenko424.a_changed.block;

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

    public LatexContainer(Properties properties) {
        super(properties);
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == 1 ? null : new LatexContainerEntity(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PART) == 0 ? SHAPE : SHAPE_UPPER;
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
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        level.invalidateCapabilities(pos);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!level.isClientSide && !state.is(newState.getBlock()) && state.getValue(PART) == 0){
            if(level.getBlockEntity(pos) instanceof LatexContainerEntity container) container.onRemove();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
        level.invalidateCapabilities(pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(context)) {
            return defaultBlockState();
        } else return null;
    }
}