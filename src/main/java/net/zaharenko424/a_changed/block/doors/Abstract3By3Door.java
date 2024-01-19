package net.zaharenko424.a_changed.block.doors;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;

public abstract class Abstract3By3Door extends AbstractMultiDoor {

    protected static final ImmutableMap<Integer, Part> PARTS;
    public static final IntegerProperty PART = StateProperties.PART9;

    public Abstract3By3Door(Properties p_54120_) {
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

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return state.getValue(PART) == 4 && state.getValue(OPEN) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    static {
        PARTS = ImmutableMap.<Integer, Part>builder()
                .put(0, new Part(0, 0, 0))
                .put(1, new Part(0, 1, 0))
                .put(2, new Part(0, 2, 0))
                .put(3, new Part(-1, 2, 0))
                .put(4, new Part(-1, 1, 0))
                .put(5, new Part(-1, 0, 0))
                .put(6, new Part(-2, 0, 0))
                .put(7, new Part(-2, 1, 0))
                .put(8, new Part(-2, 2, 0)).build();
    }
}