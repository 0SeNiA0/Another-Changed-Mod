package net.zaharenko424.testmod.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.testmod.block.HorizontalDirectionalBlock;
import net.zaharenko424.testmod.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Scanner extends HorizontalDirectionalBlock {

    private static final VoxelShape SHAPE_NORTH = Shapes.box(0.0625, 0.1875, 0.625, 0.9375, 0.8125, 1);
    private static final VoxelShape SHAPE_EAST = Shapes.box(0, 0.1875, 0.0625, 0.375, 0.8125, 0.9375);
    private static final VoxelShape SHAPE_SOUTH = Shapes.box(0.0625, 0.1875, 0, 0.9375, 0.8125, 0.375);
    private static final VoxelShape SHAPE_WEST = Shapes.box(0.625, 0.1875, 0.0625, 1, 0.8125, 0.9375);

    public Scanner(Properties p_49795_) {
        super(p_49795_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return switch (p_60555_.getValue(FACING)){
            case EAST -> SHAPE_EAST;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(!p_60504_.isClientSide) {
            ((ServerPlayer) p_60506_).setRespawnPosition(p_60504_.dimension(), p_60506_.blockPosition(), p_60506_.getYHeadRot(), true, true);
            p_60504_.playSound(null, p_60505_, SoundRegistry.SAVE.get(), SoundSource.BLOCKS);
        }
        return InteractionResult.SUCCESS;
    }
}