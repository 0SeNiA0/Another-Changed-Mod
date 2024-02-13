package net.zaharenko424.a_changed.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.network.NetworkHooks;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexEncoder extends AbstractMachine {

    private static final VoxelShape SHAPE_N = Shapes.or(
            Shapes.box(0f, 0f, 0f, 1f, 0.75f, 1f),
            Shapes.box(0.6875f, 0.75f, 0.6875f, 0.8125f, 1.125f, 0.8125f),
            Shapes.box(0.625f, 0.75f, 0.625f, 0.875f, 0.8125f, 0.875f),
            Shapes.box(0.125f, 0.75f, 0.625f, 0.5625f, 0.8125f, 0.875f),
            Shapes.box(0.125f, 0.7188f, 0.125f, 0.875f, 0.7812f, 0.5625f),
            Shapes.box(0.5625f, 0.75f, 0.7188f, 0.625f, 0.7812f, 0.7812f),
            Shapes.box(1.0063f, 0.1875f, 0.3125f, 1.0063f, 0.5625f, 0.6875f),
            Shapes.box(-0.0063f, 0.1875f, 0.3125f, -0.0063f, 0.5625f, 0.6875f));
    private static final VoxelShape SHAPE_E = Utils.rotateShape(Direction.EAST, SHAPE_N);
    private static final VoxelShape SHAPE_S = Utils.rotateShape(Direction.SOUTH, SHAPE_N);
    private static final VoxelShape SHAPE_W = Utils.rotateShape(Direction.WEST, SHAPE_N);

    public LatexEncoder(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new LatexEncoderEntity(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext pContext) {
        return switch(state.getValue(FACING)){
            case EAST -> SHAPE_E;
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_W;
            default -> SHAPE_N;
        };
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult pHit) {
        if(player.isCrouching() || hand != InteractionHand.MAIN_HAND
                || !(level.getBlockEntity(pos) instanceof LatexEncoderEntity encoder)) return InteractionResult.PASS;
        if(!level.isClientSide){
            NetworkHooks.openScreen((ServerPlayer) player, encoder, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> pBlockEntityType) {
        return level.isClientSide ? null : (a, b, c, d) -> {
            if(d instanceof LatexEncoderEntity encoder) encoder.tick();
        };
    }
}