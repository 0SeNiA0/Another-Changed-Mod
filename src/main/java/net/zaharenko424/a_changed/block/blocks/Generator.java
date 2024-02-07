package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.NetworkHooks;
import net.zaharenko424.a_changed.entity.block.machines.GeneratorEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Generator extends HorizontalDirectionalBlock implements EntityBlock {

    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public Generator(Properties pProperties) {
        super(pProperties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(LIT, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GeneratorEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult pHit) {
        if(player.isCrouching() || hand != InteractionHand.MAIN_HAND
                || !(level.getBlockEntity(pos) instanceof GeneratorEntity generatorEntity)) return InteractionResult.PASS;
        if(!level.isClientSide){
            NetworkHooks.openScreen((ServerPlayer) player, generatorEntity, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
        return defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(LIT) ? 10 : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> pBlockEntityType) {
        return level.isClientSide ? null : (a, b, c, d) -> {
            if(d instanceof GeneratorEntity generator) generator.tick();
        };
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean pMovedByPiston) {
        if(!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof GeneratorEntity generator){
            generator.remove();
        }
        super.onRemove(state, level, pos, newState, pMovedByPiston);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder.add(FACING, LIT));
    }
}