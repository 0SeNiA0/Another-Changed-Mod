package net.zaharenko424.a_changed.block.sign;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.zaharenko424.a_changed.entity.block.sign.HangingSignEntity;
import org.jetbrains.annotations.NotNull;

public class HangingSign extends CeilingHangingSignBlock {

    public HangingSign(Properties properties, WoodType woodType) {
        super(woodType, properties);
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new HangingSignEntity(pos, state);
    }
}