package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.util.StateProperties;
import net.zaharenko424.testmod.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class AirConditioner extends AbstractMultiBlock {

    private static final VoxelShape SHAPE_0 = Shapes.box(-0.625, 0.03125, 0.4375, 0.875, 1, 1);
    private static final VoxelShape SHAPE_1 = SHAPE_0.move(1,0,0);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    public static final IntegerProperty PART = StateProperties.PART;

    public AirConditioner(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART,0));
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction= p_60555_.getValue(FACING);
        return p_60555_.getValue(PART)==0? CACHE.getShape(direction,0,SHAPE_0): CACHE.getShape(direction,1,SHAPE_1);
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