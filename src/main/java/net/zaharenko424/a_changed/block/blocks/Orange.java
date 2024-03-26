package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.GrowingFruitBlock;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class Orange extends GrowingFruitBlock {

    public static final IntegerProperty AGE_5 = BlockStateProperties.AGE_5;
    private static final VoxelShape[] SHAPE_BY_AGE;

    public Orange(Properties p_49795_, Supplier<Item> fruitItem) {
        super(p_49795_, fruitItem);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_BY_AGE[getAge(state)];
    }

    @Override
    public IntegerProperty ageProperty() {
        return AGE_5;
    }

    @Override
    public int maxAge() {
        return 5;
    }

    static {
        SHAPE_BY_AGE = new VoxelShape[]{
                Shapes.or(
                        Shapes.box(0.4375f, 0.825f, 0.4375f, 0.5625f, 0.95f, 0.5625f),
                        Shapes.box(0.4812f, 0.95f, 0.4812f, 0.5188f, 1, 0.5188f)),
                Shapes.or(
                        Shapes.box(0.425f, 0.8f, 0.425f, 0.575f, 0.95f, 0.575f),
                        Shapes.box(0.4812f, 0.95f, 0.4812f, 0.5188f, 1, 0.5188f)),
                Shapes.or(
                        Shapes.box(0.4125f, 0.7625f, 0.4125f, 0.5875f, 0.9375f, 0.5875f),
                        Shapes.box(0.475f, 0.9375f, 0.475f, 0.525f, 1, 0.525f)),
                Shapes.or(
                        Shapes.box(0.4f, 0.7375f, 0.4f, 0.6f, 0.9375f, 0.6f),
                        Shapes.box(0.475f, 0.9375f, 0.475f, 0.525f, 1, 0.525f)),
                Shapes.or(
                        Shapes.box(0.3875f, 0.7125f, 0.3875f, 0.6125f, 0.9375f, 0.6125f),
                        Shapes.box(0.4688f, 0.9375f, 0.4688f, 0.5312f, 1, 0.5312f)),
                Shapes.or(
                        Shapes.box(0.375f, 0.6875f, 0.375f, 0.625f, 0.9375f, 0.625f),
                        Shapes.box(0.4688f, 0.9375f, 0.4688f, 0.5312f, 1, 0.5312f))
        };
    }
}