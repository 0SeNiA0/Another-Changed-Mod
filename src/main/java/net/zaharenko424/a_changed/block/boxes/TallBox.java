package net.zaharenko424.a_changed.block.boxes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.VerticalTwoBlockMultiBlock;
import net.zaharenko424.a_changed.util.StateProperties;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class TallBox extends VerticalTwoBlockMultiBlock {
    protected static final VoxelShape SHAPE_0 = Block.box(2,0,2,14,26,14);
    private static final VoxelShape SHAPE_1 = SHAPE_0.move(0,-1,0);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    public static final IntegerProperty PART = StateProperties.PART2;

    public TallBox(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60479_, BlockGetter p_60480_, BlockPos p_60481_, CollisionContext p_60482_) {
        Direction direction=p_60479_.getValue(FACING);
        return p_60479_.getValue(PART)==0? CACHE.getShape(direction,0, SHAPE_0): CACHE.getShape(direction,1, SHAPE_1);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return !canSurvive(p_60541_,p_60544_,p_60545_)? Blocks.AIR.defaultBlockState():super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
    }
}