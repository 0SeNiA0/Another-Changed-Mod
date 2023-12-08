package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.block.blockEntity.BookStackEntity;
import net.zaharenko424.testmod.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class BookStack extends HorizontalDirectionalBlock implements EntityBlock {

    private static final VoxelShape ONE_BOOK = Shapes.box(0.3125, 0, 0.25, 0.75, 0.125, 0.8125);
    private static final VoxelShape TWO_BOOKS = Shapes.or(ONE_BOOK,Shapes.box(0.25, 0.125, 0.34375, 0.6875, 0.25, 0.90625));
    private static final VoxelShape THREE_BOOKS = Shapes.or(TWO_BOOKS,Shapes.box(0.34375, 0.25, 0.25, 0.78125, 0.375, 0.8125));
    private static final VoxelShape FOUR_BOOKS = Shapes.or(THREE_BOOKS,Shapes.box(0.28125, 0.375, 0.21875, 0.6875, 0.5, 0.75));
    public static final IntegerProperty BOOK_AMOUNT = IntegerProperty.create("books",1,4);

    public BookStack(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(BOOK_AMOUNT,1).setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BookStackEntity(p_153215_,p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch(p_60555_.getValue(BOOK_AMOUNT)){
            case 2-> Utils.rotateShape(p_60555_.getValue(FACING),TWO_BOOKS);
            case 3-> Utils.rotateShape(p_60555_.getValue(FACING),THREE_BOOKS);
            case 4-> Utils.rotateShape(p_60555_.getValue(FACING),FOUR_BOOKS);
            default -> Utils.rotateShape(p_60555_.getValue(FACING),ONE_BOOK);
        };
    }

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity entity=p_60504_.getBlockEntity(p_60505_);
        if(entity instanceof BookStackEntity bookStack){
            ItemStack item=p_60506_.getItemInHand(p_60507_);
            if(item.is(ItemTags.BOOKSHELF_BOOKS)&&bookStack.hasSpace()){
                bookStack.addBook(item,!p_60506_.getAbilities().instabuild);
                p_60504_.setBlock(p_60505_,p_60503_.setValue(BOOK_AMOUNT, bookStack.bookAmount()),3);
                return InteractionResult.CONSUME;
            }
            if(item.isEmpty()){
                Utils.addItemOrDrop(p_60506_,bookStack.removeBook());
                if(bookStack.isEmpty()) {
                    p_60504_.setBlock(p_60505_, Blocks.AIR.defaultBlockState(), 3);
                    return InteractionResult.CONSUME;
                }
                p_60504_.setBlock(p_60505_,p_60503_.setValue(BOOK_AMOUNT, bookStack.bookAmount()),3);
                return InteractionResult.CONSUME;
            }
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock()!=p_60518_.getBlock()){
            BlockEntity entity = p_60516_.getBlockEntity(p_60517_);
            if (entity instanceof BookStackEntity bookStack) bookStack.dropBooks();
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos pos= p_60527_.below();
        return p_60526_.getBlockState(pos).isFaceSturdy(p_60526_, pos,Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(BOOK_AMOUNT,FACING);
    }
}