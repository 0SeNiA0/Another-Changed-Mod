package net.zaharenko424.a_changed.block.doors;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class AbstractTwoByTwoDoor extends AbstractMultiDoor {

    public static final IntegerProperty PART= StateProperties.PART4;

    public AbstractTwoByTwoDoor(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART,0).setValue(OPEN,false));
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
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_49820_)
                && level.getBlockState(pos2).canBeReplaced(p_49820_)
                && level.getBlockState(pos2.above()).canBeReplaced(p_49820_)) {
            return defaultBlockState().setValue(FACING,direction);
        } else return null;
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        int part=p_60525_.getValue(PART);
        Direction direction=p_60525_.getValue(FACING);
        if((part==1||part==2)) {
            if(!p_60526_.getBlockState(p_60527_.below()).is(this)) return false;
            return p_60526_.getBlockState(p_60527_.relative(part==1?direction.getCounterClockWise():direction.getClockWise())).is(this);
        }
        return part == 0 || p_60526_.getBlockState(p_60527_.relative(direction.getClockWise())).is(this);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        BlockState defState=defaultBlockState().setValue(FACING,p_49849_.getValue(FACING));
        Direction direction=p_49849_.getValue(FACING).getCounterClockWise();
        p_49847_.setBlockAndUpdate(p_49848_.above(), defState.setValue(PART, 1));
        p_49847_.setBlockAndUpdate(p_49848_.relative(direction), defState.setValue(PART, 3));
        p_49847_.setBlockAndUpdate(p_49848_.relative(direction).above(), defState.setValue(PART, 2));
    }

    protected boolean isPowered(BlockState mainState, BlockPos mainPos, LevelAccessor level){
        BlockPos pos3=mainPos.relative(mainState.getValue(FACING).getCounterClockWise());
        return level.hasNeighborSignal(mainPos)||level.hasNeighborSignal(mainPos.above())
                ||level.hasNeighborSignal(pos3)||level.hasNeighborSignal(pos3.above());
    }

    protected void setOpen(BlockState mainState, BlockPos mainPos, LevelAccessor level, boolean open){
        level.setBlock(mainPos,mainState.setValue(OPEN,open),3);
        mainPos=mainPos.above();
        level.setBlock(mainPos,level.getBlockState(mainPos).setValue(OPEN,open),3);
        mainPos=mainPos.relative(mainState.getValue(FACING).getCounterClockWise());
        level.setBlock(mainPos,level.getBlockState(mainPos).setValue(OPEN,open),3);
        mainPos=mainPos.below();
        level.setBlock(mainPos,level.getBlockState(mainPos).setValue(OPEN,open),3);
    }

    protected BlockPos getMainPos(BlockState state, BlockPos pos){
        return switch(state.getValue(PART)){
            case 1 ->pos.below();
            case 2 ->pos.below().relative(state.getValue(FACING).getClockWise());
            case 3 ->pos.relative(state.getValue(FACING).getClockWise());
            default -> pos;
        };
    }

    protected BlockPos getSecondaryPos(BlockState state, BlockPos pos) {
        return switch(state.getValue(PART)){
            case 1,3 -> pos;
            case 2 -> pos.below();
            default -> pos.above();
        };
    }
}