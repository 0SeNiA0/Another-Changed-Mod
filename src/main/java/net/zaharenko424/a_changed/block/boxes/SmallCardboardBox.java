package net.zaharenko424.a_changed.block.boxes;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
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
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.block.SmallDecorBlock;
import net.zaharenko424.a_changed.entity.block.BoxPileEntity;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class SmallCardboardBox extends SmallDecorBlock implements EntityBlock {

    private static final VoxelShape ONE_BOX = Shapes.box(0.1875, 0, 0.0625, 0.8125, 0.625, 0.9375);
    private static final VoxelShape TWO_BOXES = Shapes.or(Shapes.box(-0.1875, 0, 0.0625, 0.4375, 0.625, 0.9375)
            ,Shapes.box(0.5625, 0, 0.0625, 1.1875, 0.625, 0.9375)) ;
    private static final VoxelShape THREE_BOXES = Shapes.or(Shapes.box(-0.1875, 0, 0.0625, 0.4375, 0.625, 0.9375),
            Shapes.box(0.1875, 0.625, 0.0625, 0.8125, 1.25, 0.9375),
            Shapes.box(0.5625, 0, 0.0625, 1.1875, 0.625, 0.9375));
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    public static final IntegerProperty BOX_AMOUNT = IntegerProperty.create("boxes",1,3);

    public SmallCardboardBox(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(defaultBlockState().setValue(BOX_AMOUNT, 1));
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(SmallCardboardBox::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new BoxPileEntity(p_153215_, p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction=p_60555_.getValue(FACING);
        return switch (p_60555_.getValue(BOX_AMOUNT)){
            default -> CACHE.getShape(direction,1,ONE_BOX);
            case 2 -> CACHE.getShape(direction,2,TWO_BOXES);
            case 3 -> CACHE.getShape(direction,3,THREE_BOXES);
        };
    }

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide) return InteractionResult.SUCCESS;
        BlockEntity entity = p_60504_.getBlockEntity(p_60505_);
        if(entity instanceof BoxPileEntity boxPile){
            ItemStack item = p_60506_.getItemInHand(p_60507_);
            if(item.is(ItemRegistry.SMALL_CARDBOARD_BOX_ITEM.get())&&boxPile.hasSpace()){
                boxPile.addBox(item, !p_60506_.isCreative());
                p_60504_.setBlock(p_60505_, p_60503_.setValue(BOX_AMOUNT, boxPile.boxAmount()),3);
                return InteractionResult.CONSUME;
            }
            if(item.isEmpty()){
                ItemHandlerHelper.giveItemToPlayer(p_60506_, boxPile.removeBox());
                if(boxPile.isEmpty()){
                    p_60504_.setBlock(p_60505_, Blocks.AIR.defaultBlockState(), 3);
                    return InteractionResult.SUCCESS;
                }
                p_60504_.setBlock(p_60505_, p_60503_.setValue(BOX_AMOUNT, boxPile.boxAmount()),3);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60515_.getBlock() != p_60518_.getBlock()){
            BlockEntity entity = p_60516_.getBlockEntity(p_60517_);
            if (entity instanceof BoxPileEntity boxPile) boxPile.dropBoxes();
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(BOX_AMOUNT));
    }
}