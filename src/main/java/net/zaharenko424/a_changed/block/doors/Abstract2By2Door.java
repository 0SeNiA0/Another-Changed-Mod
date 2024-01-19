package net.zaharenko424.a_changed.block.doors;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.zaharenko424.a_changed.util.StateProperties;

public abstract class Abstract2By2Door extends AbstractMultiDoor {

    protected static final ImmutableMap<Integer, Part> PARTS;
    public static final IntegerProperty PART = StateProperties.PART4;

    public Abstract2By2Door(Properties p_54120_) {
        super(p_54120_);
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    protected ImmutableMap<Integer, Part> parts() {
        return PARTS;
    }

    static {
        PARTS = ImmutableMap.<Integer, Part>builder()
                .put(0, new Part(0, 0, 0))
                .put(1, new Part(0, 1, 0))
                .put(2, new Part(-1, 1, 0))
                .put(3, new Part(-1, 0, 0)).build();
    }
}