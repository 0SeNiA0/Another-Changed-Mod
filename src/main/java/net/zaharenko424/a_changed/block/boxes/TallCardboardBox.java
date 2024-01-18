package net.zaharenko424.a_changed.block.boxes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.zaharenko424.a_changed.block.ISeatBlock;
import net.zaharenko424.a_changed.entity.SeatEntity;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

@SuppressWarnings("deprecation")
public class TallCardboardBox extends TallBox implements ISeatBlock, Fallable {
    public TallCardboardBox(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(@NotNull BlockState p_60566_, @NotNull Level p_60567_, @NotNull BlockPos p_60568_, @NotNull BlockState p_60569_, boolean p_60570_) {
        super.onPlace(p_60566_, p_60567_, p_60568_, p_60569_, p_60570_);
        if(p_60567_.isClientSide) return;
        if(p_60566_.getValue(PART) == 0) p_60567_.addFreshEntity(new SeatEntity(p_60567_,p_60568_,true));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState p_60503_, @NotNull Level level, @NotNull BlockPos p_60505_, @NotNull Player p_60506_, @NotNull InteractionHand p_60507_, @NotNull BlockHitResult p_60508_) {
        if(level.isClientSide) return super.use(p_60503_, level, p_60505_, p_60506_, p_60507_, p_60508_);
        if(p_60506_.isCrouching()){
            BlockPos mainPos = getMainPos(p_60503_, p_60505_);
            BlockState mainState = level.getBlockState(mainPos);
            BlockPos pos = mainPos.relative(p_60506_.getDirection());
            if(level.getBlockState(pos).canBeReplaced() && level.getBlockState(pos.above()).canBeReplaced()){
                level.setBlockAndUpdate(mainPos, getFluidState(mainState).createLegacyBlock());
                level.playSound(null, mainPos, SoundRegistry.PUSH.get(), SoundSource.BLOCKS);
                BlockState below = level.getBlockState(pos.below());
                if(below.isAir() || below.canBeReplaced()) {
                    FallingBlockEntity.fall(level, pos, mainState);
                    return InteractionResult.SUCCESS;
                }
                level.setBlockAndUpdate(pos, mainState.setValue(WATERLOGGED, level.getFluidState(pos).isSourceOfType(Fluids.WATER)));
                setPlacedBy(level, pos, p_60503_, null, ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.PASS;
        }
        BlockPos pos = p_60503_.getValue(PART) == 0 ? p_60505_ : p_60505_.below();
        if(sit(level,pos, SHAPE_0.bounds().move(pos),p_60506_,false)) return InteractionResult.SUCCESS;
        return super.use(p_60503_, level, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public void onLand(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull BlockState pReplaceableState, @NotNull FallingBlockEntity pFallingBlock) {
        setPlacedBy(level, pos, state, null, ItemStack.EMPTY);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState p_60525_, @NotNull LevelReader p_60526_, @NotNull BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        BlockState state = p_60526_.getBlockState(pos);
        return p_60525_.getValue(PART) == 0 ? state.isFaceSturdy(p_60526_, pos, Direction.UP) : state.is(this);
    }
}