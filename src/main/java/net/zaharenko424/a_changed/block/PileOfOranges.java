package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.entity.block.PileOfOrangesEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PileOfOranges extends Block implements EntityBlock {

    public PileOfOranges(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        if(!(level.getBlockEntity(pos) instanceof PileOfOrangesEntity oranges)) return Shapes.empty();
        return oranges.getFinalShape();
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        return canSurvive(state, level, pos) ? state : level.getFluidState(pos).createLegacyBlock();
    }

    @Override
    protected void spawnDestroyParticles(@NotNull Level level, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state) {}

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide) return super.useWithoutItem(state, level, pos, player, hitResult);

        if(!(level.getBlockEntity(pos) instanceof PileOfOrangesEntity oranges)) return InteractionResult.FAIL;

        if(oranges.isEmpty()){
            level.removeBlock(pos, false);
            return InteractionResult.PASS;
        }

        ItemHandlerHelper.giveItemToPlayer(player, oranges.removeOrange(hitResult.getLocation()));
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(level.isClientSide) return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        if(!(level.getBlockEntity(pos) instanceof PileOfOrangesEntity oranges)) return ItemInteractionResult.FAIL;

        Vec3 clickPos = hitResult.getLocation();
        if(!validateClickPos(clickPos, pos) || !oranges.addOrange(clickPos, (int) player.yHeadRot)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if(!player.isCreative()) stack.shrink(1);
        return ItemInteractionResult.SUCCESS;
    }

    private boolean validateClickPos(Vec3 clickPos, BlockPos pos){
        return new AABB(pos).deflate(2 / 16f).move(0, -2 / 16f, 0).contains(clickPos);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PileOfOrangesEntity(pos, state);
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if(!level.isClientSide && level.getBlockEntity(pos) instanceof PileOfOrangesEntity oranges){
            oranges.onRemove();
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}