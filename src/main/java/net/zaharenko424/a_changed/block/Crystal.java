package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class Crystal extends Block {

    private static final VoxelShape SHAPE = Shapes.create(.375,0,.375,.625,.75,.625);
    private static final AABB aabb = SHAPE.bounds();
    private final Supplier<? extends TransfurType> transfurType;

    public Crystal(Properties properties, Supplier<? extends TransfurType> transfurType) {
        super(properties.friction(.9f).speedFactor(.4f).jumpFactor(.2f).noCollission());
        this.transfurType = transfurType;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(level.isClientSide || entity.tickCount % 10 != 0) return;
        if(!entity.getBoundingBox().intersects(aabb.move(pos)) || !DamageSources.checkTFTarget(entity)) return;
        TransfurHandler handler = TransfurHandler.of((LivingEntity) entity);
        if(handler != null) handler.addTransfurProgress(5f, transfurType.get(), TransfurContext.CRYSTAL);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canSupportCenter(level, pos.below(), Direction.UP);
    }
}