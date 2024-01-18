package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.zaharenko424.a_changed.entity.block.LaserEmitterEntity;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.util.StateProperties.ACTIVE;

@ParametersAreNonnullByDefault
public class LaserEmitter extends DirectionalBlock implements EntityBlock {

    public LaserEmitter(Properties p_52591_) {
        super(p_52591_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE,false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return new LaserEmitterEntity(p_153215_, p_153216_);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        BlockState state = defaultBlockState().setValue(FACING, p_49820_.getNearestLookingDirection().getOpposite());
        if(p_49820_.getLevel().hasNeighborSignal(p_49820_.getClickedPos())) return state.setValue(ACTIVE, true);
        return state;
    }

    @Override
    public void neighborChanged(BlockState p_60509_, Level p_60510_, BlockPos p_60511_, Block p_60512_, BlockPos p_60513_, boolean p_60514_) {
        super.neighborChanged(p_60509_, p_60510_, p_60511_, p_60512_, p_60513_, p_60514_);
        boolean signal = p_60510_.hasNeighborSignal(p_60511_);
        if(signal == p_60509_.getValue(ACTIVE)) return;
        if(p_60510_.getBlockEntity(p_60511_) instanceof LaserEmitterEntity emitter){
            emitter.switchActive();
        }
        p_60510_.setBlockAndUpdate(p_60511_, p_60509_.setValue(ACTIVE, signal));
        p_60510_.playSound(null,p_60511_, SoundRegistry.LASER.get(), SoundSource.BLOCKS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_);
        p_49915_.add(FACING, ACTIVE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide ? null : (a, b, c, emitter) -> ((LaserEmitterEntity)emitter).tick();
    }
}