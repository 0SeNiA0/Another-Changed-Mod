package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.block.GrowingFruitBlock;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FruitTreeLeaves extends LeavesBlock {

    private final Supplier<GrowingFruitBlock> fruit;

    public FruitTreeLeaves(Properties p_54422_, Supplier<GrowingFruitBlock> fruit) {
        super(p_54422_);
        this.fruit = fruit;
    }

    @Override
    public boolean isRandomlyTicking(@NotNull BlockState state) {
        return !state.getValue(PERSISTENT);
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if(decaying(state)){
            dropResources(state, level, pos);
            level.removeBlock(pos, false);
        } else if(canGrowFruits(pos, level, random)) {
            level.setBlockAndUpdate(pos.below(), fruit.get().defaultBlockState());
        }
    }

    protected boolean canGrowFruits(@NotNull BlockPos pos, @NotNull ServerLevel level, RandomSource random){
        BlockPos below = pos.below();
        if(!level.getBlockState(pos.below()).isAir()) return false;
        float chance = .1f;
        BlockState state;
        for(Direction direction : Direction.Plane.HORIZONTAL){
            state = level.getBlockState(below.relative(direction));
            if(state.is(BlockRegistry.ORANGE)) chance -= .025f;
        }
        return random.nextFloat() <= chance;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean pMovedByPiston) {
        super.onRemove(state, level, pos, newState, pMovedByPiston);
        if(newState.is(state.getBlock()) || level.isClientSide) return;
        if(level.getBlockState(pos.below()).is(fruit.get())) level.removeBlock(pos.below(), false);
    }
}