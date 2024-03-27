package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.function.Supplier;

import static net.zaharenko424.a_changed.block.blocks.ConnectedTextureBlock.*;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class LatexPuddle extends Block {

    private static final VoxelShape SHAPE0;
    private static final VoxelShape SHAPE_N;
    private static final VoxelShape SHAPE_NE;
    private static final VoxelShape SHAPE_NS = Shapes.box(0.125, 0, 0, 0.875, 0.0625, 1);
    private static final VoxelShape SHAPE_NES = Shapes.box(0.125, 0, 0, 1, 0.0625, 1);
    private static final VoxelShape SHAPE_NESW = Shapes.box(0, 0, 0, 1, 0.0625, 1);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    private static final HashMap<VoxelShape, AABB> AABB_CACHE = new HashMap<>();
    private final Supplier<? extends AbstractTransfurType> transfurType;

    public LatexPuddle(BlockBehaviour.Properties p_49795_, Supplier<? extends AbstractTransfurType> transfurType) {
        super(p_49795_.friction(.9f).speedFactor(.6f).jumpFactor(.6f));
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH,false)
                .setValue(EAST,false)
                .setValue(SOUTH,false)
                .setValue(WEST,false));
        this.transfurType=transfurType;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        boolean n = state.getValue(NORTH);
        boolean e = state.getValue(EAST);
        boolean s = state.getValue(SOUTH);
        boolean w = state.getValue(WEST);
        if(n && e && s && w) return SHAPE_NESW;

        if(n && !e && !s && !w) return SHAPE_N;
        if(!n && e && !s && !w) return CACHE.getShape(Direction.EAST, 0, SHAPE_N);
        if(!n && !e && s && !w) return CACHE.getShape(Direction.SOUTH, 0, SHAPE_N);
        if(!n && !e && !s && w) return CACHE.getShape(Direction.WEST, 0, SHAPE_N);

        if(n && e && !s && !w) return SHAPE_NE;
        if(!n && e && s && !w) return CACHE.getShape(Direction.EAST, 1, SHAPE_NE);
        if(!n && !e && s) return CACHE.getShape(Direction.SOUTH, 1, SHAPE_NE);
        if(n && !e && !s) return CACHE.getShape(Direction.WEST, 1, SHAPE_NE);

        if(n && !e && !w) return SHAPE_NS;
        if(!n && e && !s) return CACHE.getShape(Direction.EAST, 10, SHAPE_NS);

        if(n && e && s) return SHAPE_NES;
        if(!n && e) return CACHE.getShape(Direction.EAST, 2, SHAPE_NES);
        if(n && !e) return CACHE.getShape(Direction.SOUTH, 2, SHAPE_NES);
        if(n) return CACHE.getShape(Direction.WEST, 2, SHAPE_NES);

        return SHAPE0;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        if(!canSurvive(p_60541_, p_60544_, p_60545_)) return Blocks.AIR.defaultBlockState();
        if(p_60542_.getAxis() == Direction.Axis.Y) return p_60541_;
        return p_60541_.setValue(propByDirection.get(p_60542_),p_60543_.is(this));
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext p_49820_) {
        BlockPos pos = p_49820_.getClickedPos();
        BlockState state=defaultBlockState();
        for(Direction direction: Direction.Plane.HORIZONTAL){
            if(p_49820_.getLevel().getBlockState(pos.relative(direction)).is(this))
                state = state.setValue(propByDirection.get(direction),true);
        }
        return state;
    }

    @Override
    public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity p_60498_) {
        if(p_60496_.isClientSide || p_60498_.tickCount % 10 != 0 || !DamageSources.checkTarget(p_60498_)) return;
        if(p_60498_.getBoundingBox().intersects(AABB_CACHE.computeIfAbsent(getShape(p_60495_, p_60496_, p_60497_, CollisionContext.empty()), shape -> shape.bounds().expandTowards(0, .1, 0)).move(p_60497_)))
            TransfurEvent.ADD_TRANSFUR_DEF.accept((LivingEntity) p_60498_, transfurType.get(), 5f);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        return p_60526_.getBlockState(pos).isFaceSturdy(p_60526_, pos, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(NORTH, EAST, SOUTH, WEST));
    }

    static {
        SHAPE0 = Shapes.or(Shapes.box(0.25, 0, 0.25, 0.75, 0.0625, 0.75),
                Shapes.box(0.1875, 0, 0.3125, 0.25, 0.0625, 0.6875),
                Shapes.box(0.75, 0, 0.3125, 0.8125, 0.0625, 0.6875),
                Shapes.box(0.3125, 0, 0.75, 0.6875, 0.0625, 0.8125),
                Shapes.box(0.3125, 0, 0.1875, 0.6875, 0.0625, 0.25));
        SHAPE_N = Shapes.or(Shapes.box(0.25, 0, 0, 0.75, 0.0625, 0.75),
                Shapes.box(0.1875, 0, 0, 0.25, 0.0625, 0.6875),
                Shapes.box(0.125, 0, 0, 0.1875, 0.0625, 0.375),
                Shapes.box(0.8125, 0, 0, 0.875, 0.0625, 0.375),
                Shapes.box(0.75, 0, 0, 0.8125, 0.0625, 0.6875),
                Shapes.box(0.3125, 0, 0.75, 0.6875, 0.0625, 0.8125));
        SHAPE_NE = Shapes.or(Shapes.box(0.25, 0, 0, 1, 0.0625, 0.75),
                Shapes.box(0.1875, 0, 0, 0.25, 0.0625, 0.625),
                Shapes.box(0.125, 0, 0, 0.1875, 0.0625, 0.375),
                Shapes.box(0.375, 0, 0.75, 1, 0.0625, 0.8125),
                Shapes.box(0.625, 0, 0.8125, 1, 0.0625, 0.875));
    }
}