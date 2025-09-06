package net.zaharenko424.a_changed.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class TallCrystal extends NotRotatedMultiBlock implements LatexImmuneBlock {

    private static final VoxelShape SHAPE0 = Shapes.or(
            Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75),
            Shapes.box(0.375, 1, 0.375, 0.625, 2, 0.625));
    private static final VoxelShape SHAPE1 = SHAPE0.move(0,-1,0);
    private static final AABB aabb0 = SHAPE0.bounds();
    private static final AABB aabb1 = SHAPE1.bounds();
    private final Supplier<? extends TransfurType<?>> transfurType;
    public static IntegerProperty PART = StateProperties.PART2;

    public TallCrystal(Properties properties, Supplier<? extends TransfurType<?>> transfurType) {
        super(properties.friction(.9f).speedFactor(.4f).jumpFactor(.2f).noCollission());
        this.transfurType = transfurType;
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    protected ImmutableMap<Integer, AbstractMultiBlock.Part> parts() {
        return LatexContainer.PARTS;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(PART) == 0 ? SHAPE0 : SHAPE1;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(level.isClientSide || entity.tickCount % 10 != 0) return;
        if(!entity.getBoundingBox().intersects((state.getValue(PART) == 0 ? aabb0 : aabb1).move(pos))
                || !DamageSources.checkTFTarget(entity)) return;
        TransfurHandler handler = TransfurHandler.of((LivingEntity) entity);
        if(handler != null) handler.addTransfurProgress(5f, transfurType.get(), TransfurContext.CRYSTAL);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos posBelow = pos.below();
        BlockState below = level.getBlockState(posBelow);
        return (state.getValue(PART) == 0 && below.isFaceSturdy(level, posBelow, Direction.UP)) || below.is(this);
    }
}