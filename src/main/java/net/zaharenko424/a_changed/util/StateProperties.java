package net.zaharenko424.a_changed.util;

import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class StateProperties {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty BARS = BooleanProperty.create("bars");
    public static final BooleanProperty IN_MIDDLE = BooleanProperty.create("in_middle");
    public static final IntegerProperty PART2 = IntegerProperty.create("part", 0,1);
    public static final IntegerProperty PART4 = IntegerProperty.create("part", 0,3);
    public static final IntegerProperty PART9 = IntegerProperty.create("part", 0, 8);
    public static final IntegerProperty PART12 = IntegerProperty.create("part", 0, 11);
    public static final BooleanProperty UNLOCKED = BooleanProperty.create("unlocked");
}