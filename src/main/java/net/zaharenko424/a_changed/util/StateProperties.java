package net.zaharenko424.a_changed.util;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class StateProperties {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final IntegerProperty FLAGS3 = IntegerProperty.create("flags", 0, 2);
    public static final IntegerProperty PART2 = IntegerProperty.create("part", 0,1);
    public static final IntegerProperty PART4 = IntegerProperty.create("part", 0,3);
    public static final IntegerProperty PART9 = IntegerProperty.create("part", 0, 8);
    public static final IntegerProperty PART12 = IntegerProperty.create("part", 0, 11);
    public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");

    public static final BooleanProperty LOCKED_STATE = BooleanProperty.create("locked_state");
    public static final EnumProperty<CoveredBy> COVERED_BY = EnumProperty.create("covered_by", CoveredBy.class);

    public enum CoveredBy implements StringRepresentable {
        NOTHING("nothing"),
        DARK_LATEX("dark_latex"),
        WHITE_LATEX("white_latex");

        private final String name;

        CoveredBy(String name){
            this.name = name;
        }

        @Override
        public String toString() {
            return getSerializedName();
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}