package net.zaharenko424.a_changed.block.boxes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.zaharenko424.a_changed.block.ISeatBlock;
import net.zaharenko424.a_changed.entity.SeatEntity;
import org.jetbrains.annotations.NotNull;
@SuppressWarnings("deprecation")
public class TallCardboardBox extends TallBox implements ISeatBlock {
    public TallCardboardBox(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public void onPlace(@NotNull BlockState p_60566_, @NotNull Level p_60567_, @NotNull BlockPos p_60568_, @NotNull BlockState p_60569_, boolean p_60570_) {
        super.onPlace(p_60566_,p_60567_,p_60568_,p_60569_,p_60570_);
        if(p_60567_.isClientSide) return;
        if(p_60566_.getValue(PART)==0) p_60567_.addFreshEntity(new SeatEntity(p_60567_,p_60568_,true));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState p_60503_, @NotNull Level p_60504_, @NotNull BlockPos p_60505_, @NotNull Player p_60506_, @NotNull InteractionHand p_60507_, @NotNull BlockHitResult p_60508_) {
        if(p_60504_.isClientSide) return super.use(p_60503_,p_60504_,p_60505_,p_60506_,p_60507_,p_60508_);
        BlockPos pos=p_60503_.getValue(PART)==0?p_60505_:p_60505_.below();
        if(sit(p_60504_,pos, SHAPE_0.bounds().move(pos),p_60506_,false)) return InteractionResult.SUCCESS;
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState p_60525_, @NotNull LevelReader p_60526_, @NotNull BlockPos p_60527_) {
        BlockPos pos=p_60527_.below();
        BlockState state=p_60526_.getBlockState(pos);
        return p_60525_.getValue(PART) == 0 ? state.isFaceSturdy(p_60526_, pos, Direction.UP) : state.is(this);

    }
}