package net.zaharenko424.a_changed.block.multi;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum Part3x3 implements StringRepresentable, Part {

    TOP_LEFT(-1, 2),      TOP_MIDDLE(0, 2),   TOP_RIGHT(1, 2),
    MIDDLE_LEFT(-1, 1),   CENTER(0, 1),       MIDDLE_RIGHT(1, 1),
    BOTTOM_LEFT(-1, 0),   BOTTOM_MIDDLE(0, 0), BOTTOM_RIGHT(1, 0);

    public final int offsetX, offsetY;

    Part3x3(int x, int y){
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
