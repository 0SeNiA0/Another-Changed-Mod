package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.Tags;
import net.zaharenko424.a_changed.entity.block.PaperStackEntity;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PaperStack extends AbstractStack {

    private static final VoxelShape ONE_PAPER = Shapes.box(0.25, 0, 0.25, 0.75, 0.03125, 0.75);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public PaperStack(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PaperStackEntity(pos, state);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        BlockEntity entity = level.getBlockEntity(pos);
        if(!(entity instanceof PaperStackEntity paperStack) || paperStack.isEmpty()) return ONE_PAPER;
        return CACHE.getShape(Direction.NORTH, paperStack.size(), () ->
                Shapes.box(0.25, 0, 0.25, 0.75,  paperStack.size() * 0.03125, 0.75));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack item, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if(!(item.is(Tags.Items.DYES_BLACK) || item.is(Tags.Items.DYES_WHITE) || item.is(Items.PAPER))) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(level.isClientSide) return ItemInteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(pos);
        if(!(entity instanceof PaperStackEntity bookStack)) return super.useItemOn(item, state, level, pos, player, hand, hitResult);

        BlockPos above = pos.above();
        BlockState stateAbove = level.getBlockState(above);

        if(stateAbove.is(this)) return useItemOn(item, stateAbove, level, above, player, hand, hitResult);

        if(item.is(Tags.Items.DYES_BLACK)){
            bookStack.write();
            return ItemInteractionResult.SUCCESS;
        }

        if(item.is(Tags.Items.DYES_WHITE)){
            bookStack.erase();
            return ItemInteractionResult.SUCCESS;
        }

        if(!item.is(Items.PAPER)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

        if(bookStack.hasSpace()) {
            bookStack.addItem(item, (int) player.yHeadRot, !player.isCreative());
            return ItemInteractionResult.SUCCESS;
        }

        if(stateAbove.canBeReplaced()){
            level.setBlockAndUpdate(above, defaultBlockState());
            return useItemOn(item, level.getBlockState(above), level, above, player, hand, hitResult);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        return Items.PAPER.getDefaultInstance();
    }
}
