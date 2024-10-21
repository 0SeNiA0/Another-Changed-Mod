package net.zaharenko424.a_changed.block.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.AbstractMultiBlock;
import net.zaharenko424.a_changed.block.NotRotatedMultiBlock;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WhiteLatexPillar extends NotRotatedMultiBlock {

    private static final VoxelShape SHAPE0 = Shapes.or(
            Shapes.box(0.0938f, 0, 0.0938f, 0.9062f, 1, 0.9062f),
            Shapes.box(0.1562f, 1, 0.1562f, 0.8438f, 2, 0.8438f));
    private static final VoxelShape SHAPE1 = SHAPE0.move(0,-1,0);
    private static final AABB aabb0 = SHAPE0.bounds();
    private static final AABB aabb1 = SHAPE1.bounds();

    public WhiteLatexPillar(Properties pProperties) {
        super(pProperties.friction(.5f).speedFactor(.2f).jumpFactor(.2f).noCollission());
    }

    @Override
    public @Nullable PathType getBlockPathType(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @Nullable Mob mob) {
        return PathType.OPEN;
    }

    @Override
    protected IntegerProperty part() {
        return TallCrystal.PART;
    }

    @Override
    protected ImmutableMap<Integer, AbstractMultiBlock.Part> parts() {
        return LatexContainer.PARTS;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return state.getValue(TallCrystal.PART) == 0 ? SHAPE0 : SHAPE1;
    }

    @Override
    protected void entityInside(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Entity entity) {
        if(level.isClientSide || entity.tickCount % 10 != 0) return;
        if(!entity.getBoundingBox().intersects((state.getValue(TallCrystal.PART) == 0 ? aabb0 : aabb1).move(pos))
                || !DamageSources.checkTarget(entity)) return;
        TransfurHandler handler = TransfurHandler.of((LivingEntity) entity);
        if(handler != null) handler.addTransfurProgress(5f, TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get(), TransfurContext.ADD_PROGRESS_CRYSTAL);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos posBelow = pos.below();
        BlockState below = level.getBlockState(posBelow);
        return (state.getValue(TallCrystal.PART) == 0 && below.isFaceSturdy(level, posBelow, Direction.UP)) || below.is(this);
    }
}