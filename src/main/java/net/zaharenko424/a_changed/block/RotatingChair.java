package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.entity.RotatingChairEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RotatingChair extends Block implements ISeatBlock<RotatingChairEntity> {

    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.4375, 0, 0.4375, 0.5625, 0.4375, 0.5625)
            ,Shapes.box(0.125, 0.4375, 0.125, 0.875, 0.5625, 0.875)
            ,Shapes.box(0.125, 0, 0.4375, 0.875, 0.125, 0.5625)
            ,Shapes.box(0.4375, 0, 0.125, 0.5625, 0.125, 0.875));

    public RotatingChair(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    public boolean use(@NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        if(!level.isClientSide){
            return sit(level, pos, Shapes.block().bounds().move(pos), player, true);
        }
        return false;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        return use(level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return use(level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public Class<RotatingChairEntity> seatClass() {
        return RotatingChairEntity.class;
    }

    @Override
    public RotatingChairEntity getSeat(Level level, BlockPos pos, boolean renderPlayer) {
        return new RotatingChairEntity(level, pos, renderPlayer);
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if(!level.isClientSide) level.addFreshEntity(getSeat(level, pos, true));
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        removeSeat(level, pos);
    }
}