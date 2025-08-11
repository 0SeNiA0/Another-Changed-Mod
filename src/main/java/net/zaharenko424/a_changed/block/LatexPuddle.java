package net.zaharenko424.a_changed.block;

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
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurContext;
import net.zaharenko424.a_changed.transfurSystem.transfurType.TransfurType;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.function.Supplier;

import static net.zaharenko424.a_changed.block.ConnectedTextureBlock.*;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class LatexPuddle extends Block implements LatexImmuneBlock {

    private static final VoxelShape SHAPE0;
    private static final VoxelShape SHAPE_N;
    private static final VoxelShape SHAPE_NE;
    private static final VoxelShape SHAPE_NS = Shapes.box(0.125, 0, 0, 0.875, 0.0625, 1);
    private static final VoxelShape SHAPE_NES = Shapes.box(0.125, 0, 0, 1, 0.0625, 1);
    private static final VoxelShape SHAPE_NESW = Shapes.box(0, 0, 0, 1, 0.0625, 1);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    private static final HashMap<VoxelShape, AABB> AABB_CACHE = new HashMap<>();
    private final Supplier<? extends TransfurType<?>> transfurType;

    public LatexPuddle(BlockBehaviour.Properties properties, Supplier<? extends TransfurType<?>> transfurType) {
        super(properties.friction(.9f).speedFactor(.6f).jumpFactor(.6f));
        registerDefaultState(stateDefinition.any()
                .setValue(NORTH,false)
                .setValue(EAST,false)
                .setValue(SOUTH,false)
                .setValue(WEST,false));
        this.transfurType=transfurType;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if(!canSurvive(state, level, pos)) return Blocks.AIR.defaultBlockState();
        if(direction.getAxis() == Direction.Axis.Y) return state;
        return state.setValue(propByDirection.get(direction),neighborState.is(this));
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        BlockState state=defaultBlockState();
        for(Direction direction: Direction.Plane.HORIZONTAL){
            if(context.getLevel().getBlockState(pos.relative(direction)).is(this))
                state = state.setValue(propByDirection.get(direction),true);
        }
        return state;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if(level.isClientSide || entity.tickCount % 10 != 0 || !DamageSources.checkTFTarget(entity)) return;
        if(entity.getBoundingBox().intersects(AABB_CACHE.computeIfAbsent(getShape(state, level, pos, CollisionContext.empty()), shape -> shape.bounds().expandTowards(0, .1, 0)).move(pos)))
            TransfurHandler.nonNullOf((LivingEntity) entity).addTransfurProgress(5f, transfurType.get(), TransfurContext.DEF);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos below = pos.below();
        return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(NORTH, EAST, SOUTH, WEST));
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