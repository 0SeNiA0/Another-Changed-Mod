package net.zaharenko424.a_changed.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.zaharenko424.a_changed.entity.block.sign.SignEntity;
import org.jetbrains.annotations.NotNull;

public class StandingSign extends StandingSignBlock {

    public StandingSign(Properties pProperties, WoodType pType) {
        super(pType, pProperties);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new SignEntity(pPos, pState);
    }
}