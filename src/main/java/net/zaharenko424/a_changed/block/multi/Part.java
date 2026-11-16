package net.zaharenko424.a_changed.block.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface Part {

    BlockPos toMainPos(BlockPos secondaryPos, Direction direction);

    BlockPos toSecondaryPos(BlockPos mainPos, Direction direction);
}
