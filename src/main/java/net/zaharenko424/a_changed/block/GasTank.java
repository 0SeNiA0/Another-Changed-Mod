package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.entity.block.GasTankEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GasTank extends VerticalTwoBlockMultiBlock implements EntityBlock {

    private static final VoxelShape SHAPE_0 = Shapes.or(Shapes.box(0.3125, 0, 0.25, 0.6875, 1.125, 0.75),
            Shapes.box(0.6875, 0, 0.3125, 0.75, 1.125, 0.6875),
            Shapes.box(0.25, 0, 0.3125, 0.3125, 1.125, 0.6875),
            Shapes.box(0.375, 1.25, 0.375, 0.625, 1.4375, 0.625),
            Shapes.box(0.625, 1.125, 0.375, 0.6875, 1.25, 0.625),
            Shapes.box(0.3125, 1.125, 0.375, 0.375, 1.25, 0.625),
            Shapes.box(0.375, 1.125, 0.3125, 0.625, 1.25, 0.6875));
    private static final VoxelShape SHAPE_1 = SHAPE_0.move(0,-1,0);

    public GasTank(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == 1 ? null : new GasTankEntity(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PART) == 0 ? SHAPE_0 : SHAPE_1;
    }

    public boolean use(BlockState state, Level level, BlockPos pos, Player player) {
        if(!(level.getBlockEntity(getMainPos(state, pos)) instanceof GasTankEntity canister)) return false;

        canister.setOpenClose();//TODO make better sound
        return true;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide) return InteractionResult.CONSUME_PARTIAL;
        return use(state, level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide || hand != InteractionHand.MAIN_HAND) return ItemInteractionResult.CONSUME_PARTIAL;
        return use(state, level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if(!newState.is(state.getBlock()) && level.getBlockEntity(pos) instanceof GasTankEntity canister){
            popResource(level, pos, canister.canister());
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(level.getBlockEntity(pos) instanceof GasTankEntity canister) canister.setCanister(stack);
        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide || state.getValue(PART) == 1 ? null : (a, b, c, d) -> {
            if(d instanceof GasTankEntity canister) canister.tick();
        };
    }
}