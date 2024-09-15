package net.zaharenko424.a_changed.block.machines;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.zaharenko424.a_changed.entity.block.machines.LatexPurifierEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LatexPurifier extends AbstractMachine {

    public LatexPurifier(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(LatexPurifier::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new LatexPurifierEntity(pos, state);
    }

    public boolean use(@NotNull BlockState pState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player) {
        if(player.isCrouching() || !(level.getBlockEntity(pos) instanceof LatexPurifierEntity purifier)) return false;
        if(!level.isClientSide) player.openMenu(purifier, pos);
        return true;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        return use(state, level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return level.isClientSide ? null : (a, b, c, d) -> {
            if(d instanceof LatexPurifierEntity purifier) purifier.tick();
        };
    }
}