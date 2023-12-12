package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.block.blockEntity.LatexContainerEntity;
import net.zaharenko424.testmod.item.LatexItem;
import net.zaharenko424.testmod.util.StateProperties;
import net.zaharenko424.testmod.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
public class LatexContainer extends Block implements EntityBlock {

    private static final VoxelShape SHAPE= Shapes.or(Shapes.box(0.1875, 0, 0.1875, 0.8125, 0.125, 0.8125)
            ,Shapes.box(0.1875, 1.625, 0.1875, 0.8125, 1.75, 0.8125)
            ,Shapes.box(0.28125, 0.125, 0.28125, 0.71875, 1.625, 0.71875));
    private static final VoxelShape SHAPE_UPPER=SHAPE.move(0,-1,0);
    public static final IntegerProperty PART = StateProperties.PART;

    public LatexContainer(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(PART, 0));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return p_153216_.getValue(PART)==1?null:new LatexContainerEntity(p_153215_,p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return p_60555_.getValue(PART)==0?SHAPE:SHAPE_UPPER;
    }

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide) return InteractionResult.SUCCESS;
        BlockPos pos=p_60503_.getValue(PART)==0?p_60505_:p_60505_.below();
        BlockEntity entity=p_60504_.getBlockEntity(pos);
        if(entity instanceof LatexContainerEntity container){
            ItemStack item=p_60506_.getItemInHand(p_60507_);
            if(item.getItem()instanceof LatexItem&&container.hasSpace(item.getItem())){
                container.addLatex(item,!p_60506_.isCreative());
                return InteractionResult.SUCCESS;
            }
            if(item.isEmpty()&&!container.isEmpty()){
                Utils.addItemOrDrop(p_60506_,container.removeLatex());
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        int part = p_60541_.getValue(PART);
        if (p_60542_.getAxis() != Direction.Axis.Y || part == 0 != (p_60542_ == Direction.UP)) {
            return part == 0 && p_60542_ == Direction.DOWN && !p_60541_.canSurvive(p_60544_, p_60545_)
                    ? Blocks.AIR.defaultBlockState()
                    : super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
        } else {
            return p_60543_.is(this) && p_60543_.getValue(PART) != part
                    ? p_60541_ : Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public void playerWillDestroy(Level p_49852_, BlockPos p_49853_, BlockState p_49854_, Player p_49855_) {
        if(!p_49852_.isClientSide&&p_49855_.isCreative()){
            Utils.fixCreativeDoubleBlockDrops(p_49852_,p_49853_,p_49854_,p_49855_);
        }
        super.playerWillDestroy(p_49852_, p_49853_, p_49854_, p_49855_);
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(!p_60516_.isClientSide&&p_60515_.getValue(PART)==0){
            if(p_60516_.getBlockEntity(p_60517_) instanceof LatexContainerEntity container) container.onRemove();
        }
        super.onRemove(p_60515_, p_60516_, p_60517_, p_60518_, p_60519_);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        return p_60525_.getValue(PART) == 0 || p_60526_.getBlockState(p_60527_.below()).is(this);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        p_49847_.setBlock(p_49848_.above(),p_49849_.setValue(PART,1),3);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        BlockPos blockpos = p_49820_.getClickedPos();
        Level level = p_49820_.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_49820_)) {
            return defaultBlockState().setValue(PART, 0);
        } else return null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(PART);
    }
}