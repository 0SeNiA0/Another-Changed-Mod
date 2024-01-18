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

    public HorizontalTwoBlockMultiBlock(Properties p_54120_) {
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

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext p_49820_) {
        BlockState state = super.getStateForPlacement(p_49820_);
        if(state == null) return null;

        Direction direction = p_49820_.getClickedFace();
        if(direction.getAxis() == Direction.Axis.Y) return state;
        return state.setValue(FACING, direction);
    }
}