package net.zaharenko424.testmod.block.boxes;

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
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.block.AbstractMultiBlock;
import net.zaharenko424.testmod.util.StateProperties;
import net.zaharenko424.testmod.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class TallBox extends AbstractMultiBlock {
    protected static final VoxelShape SHAPE_0 = Block.box(2,0,2,14,26,14);
    private static final VoxelShape SHAPE_1 = SHAPE_0.move(0,-1,0);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    public static final IntegerProperty PART = StateProperties.PART;

    public TallBox(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(FACING,Direction.NORTH).setValue(PART,0));
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60479_, BlockGetter p_60480_, BlockPos p_60481_, CollisionContext p_60482_) {
        Direction direction=p_60479_.getValue(FACING);
        return p_60479_.getValue(PART)==0? CACHE.getShape(direction,0, SHAPE_0): CACHE.getShape(direction,1, SHAPE_1);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        BlockPos blockpos = p_49820_.getClickedPos();
        Level level = p_49820_.getLevel();
        if (blockpos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockpos.above()).canBeReplaced(p_49820_)) {
            return defaultBlockState().setValue(PART, 0).setValue(FACING,p_49820_.getHorizontalDirection().getOpposite());
        } else return null;
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        return p_60525_.getValue(PART) == 0 || p_60526_.getBlockState(p_60527_.below()).is(this);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        p_49847_.setBlock(p_49848_.above(),p_49849_.setValue(PART,1),3);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING,PART);
    }

    @Override
    protected BlockPos getMainPos(BlockState state, BlockPos pos) {
        return state.getValue(PART)==0?pos:pos.below();
    }

    @Override
    protected BlockPos getSecondaryPos(BlockState state, BlockPos pos) {
        return state.getValue(PART)==1?pos:pos.above();
    }
}