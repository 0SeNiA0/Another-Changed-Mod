package net.zaharenko424.a_changed.block.blocks;

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
import net.zaharenko424.a_changed.block.ISeatBlock;
import net.zaharenko424.a_changed.entity.RotatingChairEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class RotatingChair extends Block implements ISeatBlock<RotatingChairEntity> {

    private static final VoxelShape SHAPE = Shapes.or(Shapes.box(0.4375, 0, 0.4375, 0.5625, 0.4375, 0.5625)
            ,Shapes.box(0.125, 0.4375, 0.125, 0.875, 0.5625, 0.875)
            ,Shapes.box(0.125, 0, 0.4375, 0.875, 0.125, 0.5625)
            ,Shapes.box(0.4375, 0, 0.125, 0.5625, 0.125, 0.875));

    public RotatingChair(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60555_, @NotNull BlockGetter p_60556_, @NotNull BlockPos p_60557_, @NotNull CollisionContext p_60558_) {
        return SHAPE;
    }

    public boolean use(@NotNull BlockState p_60503_, @NotNull Level p_60504_, @NotNull BlockPos p_60505_, @NotNull Player p_60506_) {
        if(!p_60504_.isClientSide){
            return sit(p_60504_, p_60505_, Shapes.block().bounds().move(p_60505_), p_60506_, true);
        }
        return false;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
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
    public void onPlace(@NotNull BlockState p_60566_, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState p_60569_, boolean p_60570_) {
        if(!level.isClientSide) level.addFreshEntity(getSeat(level, pos, true));
        super.onPlace(p_60566_, level, pos, p_60569_, p_60570_);
    }

    @Override
    public void onRemove(BlockState pState, Level level, BlockPos pos, BlockState pNewState, boolean pMovedByPiston) {
        super.onRemove(pState, level, pos, pNewState, pMovedByPiston);
        removeSeat(level, pos);
    }
}