package net.zaharenko424.testmod.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.zaharenko424.testmod.util.StateProperties;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class GasCanister extends VerticalTwoBlockMultiBlock{

    public static final IntegerProperty PART = StateProperties.PART;

    public GasCanister(Properties p_54120_) {
        super(p_54120_);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART,0));
    }
}