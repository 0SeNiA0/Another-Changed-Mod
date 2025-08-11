package net.zaharenko424.a_changed.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.zaharenko424.a_changed.block.machine.Wrenchable;
import net.zaharenko424.a_changed.entity.block.LaserEmitterEntity;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.zaharenko424.a_changed.util.StateProperties.ACTIVE;

@ParametersAreNonnullByDefault
public class LaserEmitter extends DirectionalBlock implements EntityBlock, Wrenchable {

    public LaserEmitter(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE,false));
    }

    @Override
    protected @NotNull MapCodec<? extends DirectionalBlock> codec() {
        return simpleCodec(LaserEmitter::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LaserEmitterEntity(pos, state);
    }

    @Override
    public @NotNull InteractionResult useWrenchOn(BlockState state, BlockPos pos, ServerLevel level, UseOnContext context) {
        Player player = context.getPlayer();
        if(player != null && player.isCrouching()){
            level.removeBlock(pos, false);
            ItemHandlerHelper.giveItemToPlayer(player, asItem().getDefaultInstance());
        } else {
            Direction clicked = context.getClickedFace();
            Direction.Axis clickedAxis = clicked.getAxis();
            Direction current = state.getValue(FACING);

            BlockState newState = clickedAxis == Direction.Axis.Y
                    ? rotate(state, level, pos, Rotation.CLOCKWISE_90)
                    : state.setValue(FACING, current.getClockWise(clickedAxis));

            if(state == newState) return InteractionResult.PASS;

            level.setBlock(pos, newState, Block.UPDATE_ALL);
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
        if(context.getLevel().hasNeighborSignal(context.getClickedPos())) return state.setValue(ACTIVE, true);
        return state;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        boolean signal = level.hasNeighborSignal(pos);
        if(signal == state.getValue(ACTIVE)) return;

        level.setBlockAndUpdate(pos, state.setValue(ACTIVE, signal));
        level.playSound(null,pos, SoundRegistry.LASER.get(), SoundSource.BLOCKS);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ACTIVE);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (a, b, c, emitter) -> ((LaserEmitterEntity)emitter).tick();
    }
}