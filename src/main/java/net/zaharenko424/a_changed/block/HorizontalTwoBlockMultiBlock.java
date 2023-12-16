package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class HorizontalTwoBlockMultiBlock extends AbstractMultiBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty PART = StateProperties.PART;

    public HorizontalTwoBlockMultiBlock(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART,0));
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        BlockPos blockpos = p_49820_.getClickedPos();
        Level level = p_49820_.getLevel();
        Direction direction=p_49820_.getHorizontalDirection().getOpposite();
        BlockPos pos2=blockpos.relative(direction.getCounterClockWise());
        if (blockpos.getY() < level.getMaxBuildHeight() && level.getBlockState(pos2).canBeReplaced(p_49820_)){
            return defaultBlockState().setValue(FACING,direction);
        } else return null;
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        return p_60525_.getValue(PART)==0||p_60526_.getBlockState(p_60527_.relative(p_60525_.getValue(FACING).getClockWise())).is(this);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        p_49847_.setBlockAndUpdate(p_49848_.relative(p_49849_.getValue(FACING).getCounterClockWise()),p_49849_.setValue(PART,1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(FACING));
    }

    @Override
    protected BlockPos getMainPos(BlockState state, BlockPos pos) {
        return state.getValue(PART)==0?pos:pos.relative(state.getValue(FACING).getClockWise());
    }

    @Override
    protected BlockPos getSecondaryPos(BlockState state, BlockPos pos) {
        return state.getValue(PART)==1?pos:pos.relative(state.getValue(FACING).getCounterClockWise());
    }
}