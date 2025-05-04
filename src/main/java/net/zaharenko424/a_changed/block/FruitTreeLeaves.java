package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.zaharenko424.a_changed.registry.BlockRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class FruitTreeLeaves extends LeavesBlock {

    private final Supplier<GrowingFruitBlock> fruit;

    public FruitTreeLeaves(Properties properties, Supplier<GrowingFruitBlock> fruit) {
        super(properties);
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
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        if(newState.is(state.getBlock()) || level.isClientSide) return;
        if(level.getBlockState(pos.below()).is(fruit.get())) level.removeBlock(pos.below(), false);
    }
}