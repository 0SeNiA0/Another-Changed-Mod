package net.zaharenko424.a_changed.block.boxes;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.SmallDecorBlock;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

@ParametersAreNonnullByDefault
public class CardboardBox extends SmallDecorBlock {

    private static final VoxelShape SHAPE = Block.box(1,0,1,15,14,15);

    public CardboardBox(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult pHit) {
        if(player.isCrouching()){
            BlockPos pos1 = pos.relative(player.getDirection());
            if(level.getBlockState(pos1).canBeReplaced()){
                level.setBlockAndUpdate(pos, getFluidState(state).createLegacyBlock());
                level.playSound(null, pos, SoundRegistry.PUSH.get(), SoundSource.BLOCKS);
                BlockState below = level.getBlockState(pos1.below());
                if(below.isAir() || below.canBeReplaced()){
                    FallingBlockEntity.fall(level, pos1, state);
                    return InteractionResult.SUCCESS;
                }
                level.setBlockAndUpdate(pos1, state.setValue(WATERLOGGED, level.getFluidState(pos1).isSourceOfType(Fluids.WATER)));
                setPlacedBy(level, pos1, state, null, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}