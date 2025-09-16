package net.zaharenko424.a_changed.block.machine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.zaharenko424.a_changed.entity.block.machine.AbstractMachineEntity;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMachine extends HorizontalDirectionalBlock implements EntityBlock, Wrenchable {

    public static final BooleanProperty ACTIVE = StateProperties.ACTIVE;

    public AbstractMachine(Properties properties) {
        super(properties);
        registerDefState();
    }

    protected void registerDefState(){
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    @Override
    public void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        level.invalidateCapabilities(pos);
    }

    @Override
    public @NotNull InteractionResult useWrenchOn(BlockState state, BlockPos pos, ServerLevel level, @NotNull UseOnContext context) {
        Player player = context.getPlayer();

        if(player == null || !player.isCrouching()) return InteractionResult.PASS;

        level.setBlock(pos, rotate(state, level, pos, Rotation.CLOCKWISE_90), Block.UPDATE_ALL);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, @NotNull LevelAccessor level, @NotNull BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if(!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof AbstractMachineEntity<?, ?> machine){
            machine.onRemove();
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
        level.invalidateCapabilities(pos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(FACING, ACTIVE));
    }
}