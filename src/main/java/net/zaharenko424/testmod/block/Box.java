package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@SuppressWarnings("deprecation")
public class Box extends Block {
    protected static final VoxelShape SHAPE = Block.box(2,0,2,14,26,14);
    public static final EnumProperty<DoubleBlockHalf> PART = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public Box(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(PART,DoubleBlockHalf.LOWER));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        BlockPos blockpos = p_49820_.getClickedPos();
        Level level = p_49820_.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_49820_)) {
            return defaultBlockState().setValue(PART, DoubleBlockHalf.LOWER);
        } else return null;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState p_60479_, @NotNull BlockGetter p_60480_, @NotNull BlockPos p_60481_, @NotNull CollisionContext p_60482_) {
        return p_60479_.getValue(PART)==DoubleBlockHalf.LOWER?SHAPE:SHAPE.move(0,-1,0);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState p_60541_, @NotNull Direction p_60542_, @NotNull BlockState p_60543_, @NotNull LevelAccessor p_60544_, @NotNull BlockPos p_60545_, @NotNull BlockPos p_60546_) {
        if(p_60542_.getAxis()== Direction.Axis.Y){
            return p_60543_.is(this) && p_60541_.getValue(PART)!=p_60543_.getValue(PART)?p_60541_:Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(p_60541_,p_60542_,p_60543_,p_60544_,p_60545_,p_60546_);
    }

    @Override
    public void playerWillDestroy(@NotNull Level p_49852_, @NotNull BlockPos p_49853_, @NotNull BlockState p_49854_, @NotNull Player p_49855_) {
        if (!p_49852_.isClientSide && p_49855_.isCreative()) {
            DoubleBlockHalf doubleblockhalf = p_49854_.getValue(PART);
            if (doubleblockhalf == DoubleBlockHalf.UPPER) {
                BlockPos blockpos = p_49853_.below();
                BlockState blockstate = p_49852_.getBlockState(blockpos);
                if (blockstate.is(p_49854_.getBlock()) && blockstate.getValue(PART) == DoubleBlockHalf.LOWER) {
                    p_49852_.setBlock(blockpos, blockstate.getFluidState().is(Fluids.WATER) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
                    p_49852_.levelEvent(p_49855_, 2001, blockpos, Block.getId(blockstate));
                }
            }
        }
        super.playerWillDestroy(p_49852_, p_49853_, p_49854_, p_49855_);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState p_60525_, @NotNull LevelReader p_60526_, @NotNull BlockPos p_60527_) {
        BlockPos pos=p_60527_.below();
        BlockState state=p_60526_.getBlockState(pos);
        return p_60525_.getValue(PART) == DoubleBlockHalf.LOWER ? state.isFaceSturdy(p_60526_, pos, Direction.UP) : state.is(this);
    }

    @Override
    public void setPlacedBy(@NotNull Level p_49847_, @NotNull BlockPos p_49848_, @NotNull BlockState p_49849_, @Nullable LivingEntity p_49850_, @NotNull ItemStack p_49851_) {
        p_49847_.setBlock(p_49848_.above(),p_49849_.setValue(PART,DoubleBlockHalf.UPPER),3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> p_49915_) {
        p_49915_.add(PART);
    }
}