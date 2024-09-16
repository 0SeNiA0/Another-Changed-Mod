package net.zaharenko424.a_changed.block.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.entity.block.BookStackEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public class BookStack extends Block implements EntityBlock {

    private static final VoxelShape ONE_BOOK = Shapes.box(0.25, 0, 0.25, 0.75, 0.125, 0.75);
    private static final VoxelShape TWO_BOOKS = Shapes.or(ONE_BOOK,Shapes.box(0.25, 0.125, 0.25, 0.75, 0.25, 0.75));
    private static final VoxelShape THREE_BOOKS = Shapes.or(TWO_BOOKS,Shapes.box(0.25, 0.25, 0.25, 0.75, 0.375, 0.75));
    private static final VoxelShape FOUR_BOOKS = Shapes.or(THREE_BOOKS,Shapes.box(0.25, 0.375, 0.25, 0.75, 0.5, 0.75));
    private static final VoxelShape FIVE_BOOKS = Shapes.or(FOUR_BOOKS,Shapes.box(0.25, 0.5, 0.25, 0.75, 0.625, 0.75));
    private static final VoxelShape SIX_BOOKS = Shapes.or(FIVE_BOOKS,Shapes.box(0.25, 0.625, 0.25, 0.75, 0.75, 0.75));
    private static final VoxelShape SEVEN_BOOKS = Shapes.or(SIX_BOOKS,Shapes.box(0.25, 0.75, 0.25, 0.75, 0.875, 0.75));
    private static final VoxelShape EIGHT_BOOKS = Shapes.or(SEVEN_BOOKS,Shapes.box(0.25, 0.875, 0.25, 0.75, 1, 0.75));
    private static final ImmutableMap<Integer,VoxelShape> SHAPE_BY_AMOUNT = ImmutableMap.of(1,ONE_BOOK,2,TWO_BOOKS,3,THREE_BOOKS,
            4,FOUR_BOOKS,5,FIVE_BOOKS,6,SIX_BOOKS,7,SEVEN_BOOKS,8,EIGHT_BOOKS);
    public BookStack(Properties p_49795_) {
        super(p_49795_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BookStackEntity(p_153215_, p_153216_);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState p_60550_) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        BlockEntity entity = p_60556_.getBlockEntity(p_60557_);
        if(!(entity instanceof BookStackEntity bookStack) || bookStack.bookAmount() == 0) return ONE_BOOK;
        return Objects.requireNonNull(SHAPE_BY_AMOUNT.get(bookStack.bookAmount()),"Book amount out of bounds!");
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return !canSurvive(p_60541_, p_60544_, p_60545_) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide) return InteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(pos);
        if(!(entity instanceof BookStackEntity bookStack)) return super.useWithoutItem(state, level, pos, player, hitResult);

        if(bookStack.isEmpty()){
            level.removeBlock(pos, false);
            return InteractionResult.PASS;
        }

        BlockPos above = pos.above();
        BlockState stateAbove = level.getBlockState(above);

        if(stateAbove.is(this)) return useWithoutItem(stateAbove, level, above, player, hitResult);

        ItemHandlerHelper.giveItemToPlayer(player, bookStack.removeBook());
        if(bookStack.isEmpty()) {
            level.removeBlock(pos, false);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack item, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(!item.is(ItemTags.BOOKSHELF_BOOKS)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if(level.isClientSide) return ItemInteractionResult.SUCCESS;

        BlockEntity entity = level.getBlockEntity(pos);
        if(!(entity instanceof BookStackEntity bookStack)) return super.useItemOn(item, state, level, pos, player, hand, hitResult);

        BlockPos above = pos.above();
        BlockState stateAbove = level.getBlockState(above);

        if(bookStack.hasSpace()) {
            bookStack.addBook(item, (int) player.yHeadRot, !player.isCreative());
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
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock() != p_60518_.getBlock()){
            BlockEntity entity = p_60516_.getBlockEntity(p_60517_);
            if (entity instanceof BookStackEntity bookStack) bookStack.dropBooks();
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        BlockState stateBelow = p_60526_.getBlockState(pos);
        return stateBelow.isFaceSturdy(p_60526_, pos,Direction.UP) || stateBelow.is(this);
    }
}