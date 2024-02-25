package net.zaharenko424.a_changed.block.machines;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.zaharenko424.a_changed.entity.block.machines.GeneratorEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Generator extends AbstractMachine {

    public Generator(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(Generator::new);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GeneratorEntity(pos, state);
    }

    @Override @SuppressWarnings("deprecation")
    public @NotNull InteractionResult use(@NotNull BlockState pState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult pHit) {
        if(player.isCrouching() || hand != InteractionHand.MAIN_HAND
                || !(level.getBlockEntity(pos) instanceof GeneratorEntity generator)) return InteractionResult.PASS;
        if(!level.isClientSide) player.openMenu(generator, pos);
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getLightEmission(@NotNull BlockState state, BlockGetter level, BlockPos pos) {
        return state.getValue(ACTIVE) ? 10 : 0;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> pBlockEntityType) {
        return level.isClientSide ? null : (a, b, c, d) -> {
            if(d instanceof GeneratorEntity generator) generator.tick();
        };
    }
}