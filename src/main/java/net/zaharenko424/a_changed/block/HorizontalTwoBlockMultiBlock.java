package net.zaharenko424.a_changed.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class HorizontalTwoBlockMultiBlock extends AbstractMultiBlock {

    protected static final ImmutableMap<Integer, Part> PARTS = ImmutableMap.of(
            0, new Part(0, 0, 0), 1, new Part(-1, 0, 0));
    public static final IntegerProperty PART = StateProperties.PART2;

    public HorizontalTwoBlockMultiBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    protected ImmutableMap<Integer, Part> parts() {
        return PARTS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);
        if(state == null) return null;

        Direction direction = context.getClickedFace();
        if(direction.getAxis() == Direction.Axis.Y) return state;
        return state.setValue(FACING, direction);
    }
}