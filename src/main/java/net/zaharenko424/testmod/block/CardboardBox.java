package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
@SuppressWarnings("deprecation")
public class CardboardBox extends Box implements ISeatBlock{
    public CardboardBox(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState p_60503_, @NotNull Level p_60504_, @NotNull BlockPos p_60505_, @NotNull Player p_60506_, @NotNull InteractionHand p_60507_, @NotNull BlockHitResult p_60508_) {
        if(p_60504_.isClientSide) return super.use(p_60503_,p_60504_,p_60505_,p_60506_,p_60507_,p_60508_);
        BlockPos pos=p_60503_.getValue(PART)==DoubleBlockHalf.LOWER?p_60505_:p_60505_.below();
        if(sit(p_60504_,pos,SHAPE.bounds().move(pos),p_60506_,false)) return InteractionResult.SUCCESS;
        return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
    }
}