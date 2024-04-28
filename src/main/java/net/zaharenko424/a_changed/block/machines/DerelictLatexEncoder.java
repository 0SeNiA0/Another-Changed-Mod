package net.zaharenko424.a_changed.block.machines;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.registry.ItemRegistry;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class DerelictLatexEncoder extends AbstractDerelictMachine {

    private static final VoxelShape SHAPE_N = Shapes.or(
            Shapes.box(0f, 0f, 0f, 1f, 0.75f, 1f),
            Shapes.box(0.625f, 0.75f, 0.625f, 0.875f, 0.8125f, 0.875f),
            Shapes.box(0.125f, 0.75f, 0.625f, 0.5625f, 0.8125f, 0.875f),
            Shapes.box(0.125f, 0.7188f, 0.125f, 0.875f, 0.7812f, 0.5625f),
            Shapes.box(0.5625f, 0.75f, 0.7188f, 0.625f, 0.7812f, 0.7812f),
            Shapes.box(1.0063f, 0.1875f, 0.3125f, 1.0063f, 0.5625f, 0.6875f),
            Shapes.box(-0.0063f, 0.1875f, 0.3125f, -0.0063f, 0.5625f, 0.6875f));
    private static final VoxelShape SHAPE_E = Utils.rotateShape(Direction.EAST, SHAPE_N);
    private static final VoxelShape SHAPE_S = Utils.rotateShape(Direction.SOUTH, SHAPE_N);
    private static final VoxelShape SHAPE_W = Utils.rotateShape(Direction.WEST, SHAPE_N);

    public DerelictLatexEncoder(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends DerelictLatexEncoder> codec() {
        return simpleCodec(DerelictLatexEncoder::new);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext pContext) {
        return switch(state.getValue(FACING)){
            case EAST -> SHAPE_E;
            case SOUTH -> SHAPE_S;
            case WEST -> SHAPE_W;
            default -> SHAPE_N;
        };
    }

    @Override
    ItemStack getDrop(@NotNull RandomSource random) {
        return ItemRegistry.LATEX_ENCODER_COMPONENTS.toStack(random.nextFloat() > .9 ? 2 : 1);
    }
}