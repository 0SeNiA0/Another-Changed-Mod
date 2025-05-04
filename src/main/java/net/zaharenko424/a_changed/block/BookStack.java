package net.zaharenko424.a_changed.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
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
import net.zaharenko424.a_changed.entity.block.BookStackEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class BookStack extends AbstractStack {

    private static final VoxelShape ONE_BOOK = Shapes.box(0.25, 0, 0.25, 0.75, 0.125, 0.75);
    private static final VoxelShape TWO_BOOKS = Shapes.or(ONE_BOOK, Shapes.box(0.25, 0.125, 0.25, 0.75, 0.25, 0.75));
    private static final VoxelShape THREE_BOOKS = Shapes.or(TWO_BOOKS, Shapes.box(0.25, 0.25, 0.25, 0.75, 0.375, 0.75));
    private static final VoxelShape FOUR_BOOKS = Shapes.or(THREE_BOOKS, Shapes.box(0.25, 0.375, 0.25, 0.75, 0.5, 0.75));
    private static final VoxelShape FIVE_BOOKS = Shapes.or(FOUR_BOOKS, Shapes.box(0.25, 0.5, 0.25, 0.75, 0.625, 0.75));
    private static final VoxelShape SIX_BOOKS = Shapes.or(FIVE_BOOKS, Shapes.box(0.25, 0.625, 0.25, 0.75, 0.75, 0.75));
    private static final VoxelShape SEVEN_BOOKS = Shapes.or(SIX_BOOKS, Shapes.box(0.25, 0.75, 0.25, 0.75, 0.875, 0.75));
    private static final VoxelShape EIGHT_BOOKS = Shapes.or(SEVEN_BOOKS, Shapes.box(0.25, 0.875, 0.25, 0.75, 1, 0.75));
    private static final ImmutableMap<Integer, VoxelShape> SHAPE_BY_AMOUNT = ImmutableMap.of(1, ONE_BOOK,2, TWO_BOOKS,3, THREE_BOOKS,
            4, FOUR_BOOKS,5, FIVE_BOOKS,6, SIX_BOOKS,7, SEVEN_BOOKS,8, EIGHT_BOOKS);

    public BookStack(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BookStackEntity(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        BlockEntity entity = level.getBlockEntity(pos);
        if(!(entity instanceof BookStackEntity bookStack) || bookStack.isEmpty()) return ONE_BOOK;
        return Objects.requireNonNull(SHAPE_BY_AMOUNT.get(bookStack.size()),"Book amount out of bounds!");
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack item, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!item.is(ItemTags.BOOKSHELF_BOOKS)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(level.isClientSide) return ItemInteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(pos);
        if(!(entity instanceof BookStackEntity bookStack)) return super.useItemOn(item, state, level, pos, player, hand, hitResult);

        BlockPos above = pos.above();
        BlockState stateAbove = level.getBlockState(above);

        if(bookStack.hasSpace()) {
            bookStack.addItem(item, (int) player.yHeadRot, !player.isCreative());
            return ItemInteractionResult.SUCCESS;
        }

        if(stateAbove.is(this)) return useItemOn(item, stateAbove, level, above, player, hand, hitResult);
        if(stateAbove.canBeReplaced()){
            level.setBlockAndUpdate(above, defaultBlockState());
            return useItemOn(item, level.getBlockState(above), level, above, player, hand, hitResult);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        return Items.BOOK.getDefaultInstance();
    }
}