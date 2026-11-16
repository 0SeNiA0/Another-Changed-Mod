package net.zaharenko424.a_changed.block.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Part2x2 implements StringRepresentable, Part {

    TOP_LEFT(0, 1),    TOP_RIGHT(1, 1),
    BOTTOM_LEFT(0, 0), BOTTOM_RIGHT(1, 0);

    public final int offsetX, offsetY;

    Part2x2(int x, int y){
        this.offsetX = x;
        this.offsetY = y;
    }

    @Override
    public String getSerializedName() {
        return toString().toLowerCase(Locale.ROOT);
    }

    public @NotNull BlockPos toMainPos(BlockPos secondaryPos, Direction direction){
        return secondaryPos
                .relative(direction.getClockWise(), offsetX)
                .below(offsetY);
    }

    public @NotNull BlockPos toSecondaryPos(BlockPos mainPos, Direction direction){
        return mainPos
                .relative(direction.getCounterClockWise(), offsetX)
                .above(offsetY);
    }
}
