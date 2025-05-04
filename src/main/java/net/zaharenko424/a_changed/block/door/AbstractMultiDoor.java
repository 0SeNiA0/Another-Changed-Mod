package net.zaharenko424.a_changed.block.door;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.zaharenko424.a_changed.block.AbstractMultiBlock;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractMultiDoor extends AbstractMultiBlock {

    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;

    public AbstractMultiDoor(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(OPEN,false));
    }

    protected void use(BlockState state, Level level, BlockPos pos) {
        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        if(isPowered(mainPos, mainState, level)) {
            setOpen(mainState, mainPos, level, !state.getValue(OPEN));
            level.playSound(null, pos, mainState.getValue(OPEN) ? SoundRegistry.DOOR_CLOSE.get() : SoundRegistry.DOOR_OPEN.get(), SoundSource.BLOCKS);
        } else {
            level.playSound(null, pos, SoundRegistry.DOOR_LOCKED.get(), SoundSource.BLOCKS);
        }
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if(level.isClientSide) return super.useWithoutItem(state, level, pos, player, hitResult);
        use(state, level, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if(hand != InteractionHand.MAIN_HAND || level.isClientSide) return ItemInteractionResult.CONSUME_PARTIAL;
        use(state, level, pos);
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        if(mainState.isAir()) return;
        if(!isPowered(mainPos, level.getBlockState(mainPos), level) && state.getValue(OPEN)){
            setOpen(mainState, mainPos, level,false);
            level.playSound(null, pos, SoundRegistry.DOOR_CLOSE.get(), SoundSource.BLOCKS);
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(OPEN));
    }

    void setOpen(BlockState mainState, BlockPos mainPos, LevelAccessor level, boolean open){
        Direction direction = mainState.getValue(FACING);
        parts().forEach((id, part) -> {
            BlockPos pos = part.toSecondaryPos(mainPos, direction);
            level.setBlock(pos, level.getBlockState(pos).setValue(OPEN, open), 3);
        });
    }
}