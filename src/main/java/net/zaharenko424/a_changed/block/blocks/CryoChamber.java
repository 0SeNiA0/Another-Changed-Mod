package net.zaharenko424.a_changed.block.blocks;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.AbstractMultiBlock;
import net.zaharenko424.a_changed.entity.block.CryoChamberEntity;
import net.zaharenko424.a_changed.registry.SoundRegistry;
import net.zaharenko424.a_changed.util.StateProperties;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class CryoChamber extends AbstractMultiBlock implements EntityBlock {

    private static final VoxelShape SHAPE;
    private static final VoxelShape SHAPE_OPEN;
    private static final VoxelShape SHAPE_INSIDE = Shapes.box(-0.625, 0, 0.375, 0.625, 2, 1.625);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();
    private static final ImmutableMap<Integer, Part> PARTS;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final IntegerProperty PART = StateProperties.PART12;

    public CryoChamber(Properties pProperties) {
        super(pProperties);
        registerDefaultState(defaultBlockState().setValue(OPEN, false));
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == 0 ? new CryoChamberEntity(pos, state) : null;
    }

    public AABB inside(BlockPos mainPos, BlockState mainSate){
        return CACHE.getShape(mainSate.getValue(FACING), 621, SHAPE_INSIDE).bounds().move(mainPos);
    }

    public static @Nullable CryoChamberEntity getEntity(BlockPos pos, Level level) {
        BlockState state = level.getBlockState(pos);
        if(!(state.getBlock() instanceof CryoChamber chamber)) return null;
        if(!(level.getBlockEntity(chamber.getMainPos(state, pos)) instanceof CryoChamberEntity chamberEntity)) return null;
        return chamberEntity;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext pContext) {
        int partId = state.getValue(PART);
        boolean open = state.getValue(OPEN);
        return CACHE.getShape(state.getValue(FACING), partId + (open ? 100 : 0), PARTS.get(partId).alignShape(open ? SHAPE_OPEN : SHAPE));
    }

    @Override
    public @NotNull InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult pHit) {
        if(player.isCrouching()) return InteractionResult.PASS;
        if(hand != InteractionHand.MAIN_HAND || level.isClientSide) return InteractionResult.CONSUME_PARTIAL;
        BlockPos mainPos = getMainPos(state, pos);
        boolean open = !state.getValue(OPEN);
        setOpen(level.getBlockState(mainPos), mainPos, level, open);
        level.playSound(null, pos, open ? SoundRegistry.SPACE_DOOR_OPEN.get() : SoundRegistry.SPACE_DOOR_CLOSE.get(), SoundSource.BLOCKS);
        if(level.getBlockEntity(mainPos) instanceof CryoChamberEntity chamber) chamber.setOpen(open);
        return InteractionResult.SUCCESS;
    }

    public void leak(BlockPos mainPos, BlockState mainState, Level level, int lvl){
        Direction direction = mainState.getValue(FACING);
        BlockPos pos1 = mainPos.relative(direction);
        BlockPos pos2 = pos1.relative(direction.getCounterClockWise());
        if(lvl > 14) lvl = 14;
        if(lvl > 7){
            level.setBlockAndUpdate(pos1.above(), Fluids.FLOWING_WATER.defaultFluidState().setValue(BlockStateProperties.LEVEL_FLOWING, lvl - 7).createLegacyBlock());
            level.setBlockAndUpdate(pos2.above(), Fluids.FLOWING_WATER.defaultFluidState().setValue(BlockStateProperties.LEVEL_FLOWING, lvl - 7).createLegacyBlock());
            lvl = 7;
        }
        level.setBlockAndUpdate(pos1, Fluids.FLOWING_WATER.defaultFluidState().setValue(BlockStateProperties.LEVEL_FLOWING, lvl).createLegacyBlock());
        level.setBlockAndUpdate(pos2, Fluids.FLOWING_WATER.defaultFluidState().setValue(BlockStateProperties.LEVEL_FLOWING, lvl).createLegacyBlock());
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block pNeighborBlock, BlockPos pNeighborPos, boolean pMovedByPiston) {
        super.neighborChanged(state, level, pos, pNeighborBlock, pNeighborPos, pMovedByPiston);
        BlockPos mainPos = getMainPos(state, pos);
        BlockState mainState = level.getBlockState(mainPos);
        if(!(level.getBlockEntity(mainPos) instanceof CryoChamberEntity chamber)) return;
        boolean active = isPowered(mainPos, mainState, level);
        if(chamber.isActive() == active) return;
        chamber.setActive(active);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        super.createBlockStateDefinition(p_49915_.add(OPEN));
    }

    void setOpen(@NotNull BlockState mainState, BlockPos mainPos, LevelAccessor level, boolean open){
        Direction direction = mainState.getValue(FACING);
        parts().forEach((id, part) -> {
            BlockPos pos = part.toSecondaryPos(mainPos, direction);
            level.setBlock(pos, level.getBlockState(pos).setValue(OPEN, open), 3);
        });
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return level.isClientSide ? null : (a, b, c, d) -> {
            if(d instanceof CryoChamberEntity chamber) chamber.tick();
        };
    }

    static {
        SHAPE = Shapes.or(Shapes.box(-0.6875f, 1.375f, 0.3125f, 0.6875f, 1.5f, 1.6875f),
                Shapes.box(-0.9375f, 1.375f, 0.8125f, -0.875f, 1.5f, 1.1875f),
                Shapes.box(-0.9375f, -0.875f, 0.8125f, -0.875f, 1.375f, 1.1875f),
                Shapes.box(0.875f, 1.375f, 0.8125f, 0.9375f, 1.5f, 1.1875f),
                Shapes.box(0.875f, -0.875f, 0.8125f, 0.9375f, 1.375f, 1.1875f),
                Shapes.box(0.8125f, -0.875f, 1.125f, 0.875f, 1.375f, 1.375f),
                Shapes.box(0.8125f, -0.875f, 0.625f, 0.875f, 1.375f, 0.875f),
                Shapes.box(-0.875f, -0.875f, 0.625f, -0.8125f, 1.375f, 0.875f),
                Shapes.box(-0.8125f, -0.875f, 0.5f, -0.75f, 1.375f, 0.6875f),
                Shapes.box(0.75f, -0.875f, 0.5f, 0.8125f, 1.375f, 0.6875f),
                Shapes.box(0.75f, -0.875f, 1.3125f, 0.8125f, 1.375f, 1.5f),
                Shapes.box(-0.8125f, -0.875f, 1.3125f, -0.75f, 1.375f, 1.5f),
                Shapes.box(-0.75f, -0.875f, 1.4375f, -0.6875f, 1.375f, 1.625f),
                Shapes.box(0.6875f, -0.875f, 1.4375f, 0.75f, 1.375f, 1.625f),
                Shapes.box(0.6875f, -0.875f, 0.375f, 0.75f, 1.375f, 0.5625f),
                Shapes.box(-0.75f, -0.875f, 0.375f, -0.6875f, 1.375f, 0.5625f),
                Shapes.box(-0.875f, -0.875f, 1.125f, -0.8125f, 1.375f, 1.375f),
                Shapes.box(-0.875f, 1.375f, 0.625f, -0.8125f, 1.5f, 1.375f),
                Shapes.box(0.8125f, 1.375f, 0.625f, 0.875f, 1.5f, 1.375f),
                Shapes.box(-0.8125f, 1.375f, 0.5f, -0.75f, 1.5f, 1.5f),
                Shapes.box(0.75f, 1.375f, 0.5f, 0.8125f, 1.5f, 1.5f),
                Shapes.box(-0.75f, 1.375f, 0.375f, -0.6875f, 1.5f, 1.625f),
                Shapes.box(0.6875f, 1.375f, 0.375f, 0.75f, 1.5f, 1.625f),
                Shapes.box(-0.1875f, 1.375f, 1.875f, 0.1875f, 1.5f, 1.9375f),
                Shapes.box(-0.1875f, -0.875f, 1.875f, 0.1875f, 1.375f, 1.9375f),
                Shapes.box(0.125f, -0.875f, 1.8125f, 0.375f, 1.375f, 1.875f),
                Shapes.box(0.125f, -0.875f, 0.125f, 0.375f, 1.375f, 0.1875f),
                Shapes.box(-0.375f, -0.875f, 1.8125f, -0.125f, 1.375f, 1.875f),
                Shapes.box(-0.5f, -0.875f, 1.75f, -0.3125f, 1.375f, 1.8125f),
                Shapes.box(0.3125f, -0.875f, 1.75f, 0.5f, 1.375f, 1.8125f),
                Shapes.box(0.3125f, -0.875f, 0.1875f, 0.5f, 1.375f, 0.25f),
                Shapes.box(-0.5f, -0.875f, 0.1875f, -0.3125f, 1.375f, 0.25f),
                Shapes.box(-0.625f, -0.875f, 0.25f, -0.4375f, 1.375f, 0.3125f),
                Shapes.box(0.4375f, -0.875f, 0.25f, 0.625f, 1.375f, 0.3125f),
                Shapes.box(0.4375f, -0.875f, 1.6875f, 0.625f, 1.375f, 1.75f),
                Shapes.box(-0.625f, -0.875f, 1.6875f, -0.4375f, 1.375f, 1.75f),
                Shapes.box(-0.6875f, -0.875f, 1.625f, -0.625f, 1.375f, 1.6875f),
                Shapes.box(0.625f, -0.875f, 1.625f, 0.6875f, 1.375f, 1.6875f),
                Shapes.box(0.625f, -0.875f, 0.3125f, 0.6875f, 1.375f, 0.375f),
                Shapes.box(-0.6875f, -0.875f, 0.3125f, -0.625f, 1.375f, 0.375f),
                Shapes.box(-0.375f, -0.875f, 0.125f, -0.125f, 1.375f, 0.1875f),
                Shapes.box(-0.1875f, 1.375f, 0.0625f, 0.1875f, 1.5f, 0.125f),
                Shapes.box(-0.1875f, -0.875f, 0.0625f, 0.1875f, 1.375f, 0.125f),
                Shapes.box(-0.375f, 1.375f, 1.8125f, 0.375f, 1.5f, 1.875f),
                Shapes.box(-0.375f, 1.375f, 0.125f, 0.375f, 1.5f, 0.1875f),
                Shapes.box(-0.5f, 1.375f, 1.75f, 0.5f, 1.5f, 1.8125f),
                Shapes.box(-0.5f, 1.375f, 0.1875f, 0.5f, 1.5f, 0.25f),
                Shapes.box(-0.625f, 1.375f, 1.6875f, 0.625f, 1.5f, 1.75f),
                Shapes.box(-0.625f, 1.375f, 0.25f, 0.625f, 1.5f, 0.3125f),
                Shapes.box(-0.6875f, -1f, 0.3125f, 0.6875f, -0.875f, 1.6875f),
                Shapes.box(-0.1875f, -1f, 1.875f, 0.1875f, -0.875f, 1.9375f),
                Shapes.box(-0.9375f, -1f, 0.8125f, -0.875f, -0.875f, 1.1875f),
                Shapes.box(-0.375f, -1f, 1.8125f, 0.375f, -0.875f, 1.875f),
                Shapes.box(-0.5f, -1f, 1.75f, 0.5f, -0.875f, 1.8125f),
                Shapes.box(-0.8125f, -1f, 0.5f, -0.75f, -0.875f, 1.5f),
                Shapes.box(-0.625f, -1f, 1.6875f, 0.625f, -0.875f, 1.75f),
                Shapes.box(-0.75f, -1f, 0.375f, -0.6875f, -0.875f, 1.625f),
                Shapes.box(-0.875f, -1f, 0.625f, -0.8125f, -0.875f, 1.375f),
                Shapes.box(0.875f, -1f, 0.8125f, 0.9375f, -0.875f, 1.1875f),
                Shapes.box(0.8125f, -1f, 0.625f, 0.875f, -0.875f, 1.375f),
                Shapes.box(0.75f, -1f, 0.5f, 0.8125f, -0.875f, 1.5f),
                Shapes.box(0.6875f, -1f, 0.375f, 0.75f, -0.875f, 1.625f),
                Shapes.box(-0.1875f, -1f, 0.0625f, 0.1875f, -0.875f, 0.125f),
                Shapes.box(-0.375f, -1f, 0.125f, 0.375f, -0.875f, 0.1875f),
                Shapes.box(-0.5f, -1f, 0.1875f, 0.5f, -0.875f, 0.25f),
                Shapes.box(-0.625f, -1f, 0.25f, 0.625f, -0.875f, 0.3125f)).move(0, 1, 0);
        SHAPE_OPEN = Shapes.or(Shapes.box(-0.6875f, 1.375f, 0.3125f, 0.6875f, 1.5f, 1.6875f),
                Shapes.box(-0.9375f, 1.375f, 0.8125f, -0.875f, 1.5f, 1.1875f),
                Shapes.box(-0.9375f, -0.875f, 0.8125f, -0.875f, 1.375f, 1.1875f),
                Shapes.box(0.875f, 1.375f, 0.8125f, 0.9375f, 1.5f, 1.1875f),
                Shapes.box(0.875f, -0.875f, 0.8125f, 0.9375f, 1.375f, 1.1875f),
                Shapes.box(0.8125f, -0.875f, 1.125f, 0.875f, 1.375f, 1.375f),
                Shapes.box(0.8125f, -0.875f, 0.625f, 0.875f, 1.375f, 0.875f),
                Shapes.box(-0.875f, -0.875f, 0.625f, -0.8125f, 1.375f, 0.875f),
                Shapes.box(-0.8125f, -0.875f, 0.5f, -0.75f, 1.375f, 0.6875f),
                Shapes.box(0.75f, -0.875f, 0.5f, 0.8125f, 1.375f, 0.6875f),
                Shapes.box(0.75f, -0.875f, 1.3125f, 0.8125f, 1.375f, 1.5f),
                Shapes.box(-0.8125f, -0.875f, 1.3125f, -0.75f, 1.375f, 1.5f),
                Shapes.box(-0.75f, -0.875f, 1.4375f, -0.6875f, 1.375f, 1.625f),
                Shapes.box(0.6875f, -0.875f, 1.4375f, 0.75f, 1.375f, 1.625f),
                Shapes.box(-0.875f, -0.875f, 1.125f, -0.8125f, 1.375f, 1.375f),
                Shapes.box(-0.875f, 1.375f, 0.625f, -0.8125f, 1.5f, 1.375f),
                Shapes.box(0.8125f, 1.375f, 0.625f, 0.875f, 1.5f, 1.375f),
                Shapes.box(-0.8125f, 1.375f, 0.5f, -0.75f, 1.5f, 1.5f),
                Shapes.box(0.75f, 1.375f, 0.5f, 0.8125f, 1.5f, 1.5f),
                Shapes.box(-0.75f, 1.375f, 0.375f, -0.6875f, 1.5f, 1.625f),
                Shapes.box(0.6875f, 1.375f, 0.375f, 0.75f, 1.5f, 1.625f),
                Shapes.box(-0.1875f, 1.375f, 1.875f, 0.1875f, 1.5f, 1.9375f),
                Shapes.box(-0.1875f, -0.875f, 1.875f, 0.1875f, 1.375f, 1.9375f),
                Shapes.box(0.125f, -0.875f, 1.8125f, 0.375f, 1.375f, 1.875f),
                Shapes.box(-0.375f, -0.875f, 1.8125f, -0.125f, 1.375f, 1.875f),
                Shapes.box(-0.5f, -0.875f, 1.75f, -0.3125f, 1.375f, 1.8125f),
                Shapes.box(0.3125f, -0.875f, 1.75f, 0.5f, 1.375f, 1.8125f),
                Shapes.box(0.4375f, -0.875f, 1.6875f, 0.625f, 1.375f, 1.75f),
                Shapes.box(-0.625f, -0.875f, 1.6875f, -0.4375f, 1.375f, 1.75f),
                Shapes.box(-0.6875f, -0.875f, 1.625f, -0.625f, 1.375f, 1.6875f),
                Shapes.box(0.625f, -0.875f, 1.625f, 0.6875f, 1.375f, 1.6875f),
                Shapes.box(-0.1875f, 1.375f, 0.0625f, 0.1875f, 1.5f, 0.125f),
                Shapes.box(-0.375f, 1.375f, 1.8125f, 0.375f, 1.5f, 1.875f),
                Shapes.box(-0.375f, 1.375f, 0.125f, 0.375f, 1.5f, 0.1875f),
                Shapes.box(-0.5f, 1.375f, 1.75f, 0.5f, 1.5f, 1.8125f),
                Shapes.box(-0.5f, 1.375f, 0.1875f, 0.5f, 1.5f, 0.25f),
                Shapes.box(-0.625f, 1.375f, 1.6875f, 0.625f, 1.5f, 1.75f),
                Shapes.box(-0.625f, 1.375f, 0.25f, 0.625f, 1.5f, 0.3125f),
                Shapes.box(-0.6875f, -1f, 0.3125f, 0.6875f, -0.875f, 1.6875f),
                Shapes.box(-0.1875f, -1f, 1.875f, 0.1875f, -0.875f, 1.9375f),
                Shapes.box(-0.9375f, -1f, 0.8125f, -0.875f, -0.875f, 1.1875f),
                Shapes.box(-0.375f, -1f, 1.8125f, 0.375f, -0.875f, 1.875f),
                Shapes.box(-0.5f, -1f, 1.75f, 0.5f, -0.875f, 1.8125f),
                Shapes.box(-0.8125f, -1f, 0.5f, -0.75f, -0.875f, 1.5f),
                Shapes.box(-0.625f, -1f, 1.6875f, 0.625f, -0.875f, 1.75f),
                Shapes.box(-0.75f, -1f, 0.375f, -0.6875f, -0.875f, 1.625f),
                Shapes.box(-0.875f, -1f, 0.625f, -0.8125f, -0.875f, 1.375f),
                Shapes.box(0.875f, -1f, 0.8125f, 0.9375f, -0.875f, 1.1875f),
                Shapes.box(0.8125f, -1f, 0.625f, 0.875f, -0.875f, 1.375f),
                Shapes.box(0.75f, -1f, 0.5f, 0.8125f, -0.875f, 1.5f),
                Shapes.box(0.6875f, -1f, 0.375f, 0.75f, -0.875f, 1.625f),
                Shapes.box(-0.1875f, -1f, 0.0625f, 0.1875f, -0.875f, 0.125f),
                Shapes.box(-0.375f, -1f, 0.125f, 0.375f, -0.875f, 0.1875f),
                Shapes.box(-0.5f, -1f, 0.1875f, 0.5f, -0.875f, 0.25f),
                Shapes.box(-0.625f, -1f, 0.25f, 0.625f, -0.875f, 0.3125f),
                Shapes.box(0.75f, -0.875f, 0.75f, 0.8125f, 1.375f, 0.9375f),
                Shapes.box(-0.8125f, -0.875f, 0.75f, -0.75f, 1.375f, 0.9375f),
                Shapes.box(0.8125f, -0.875f, 0.9375f, 0.875f, 1.375f, 1f),
                Shapes.box(-0.875f, -0.875f, 0.9375f, -0.8125f, 1.375f, 1f),
                Shapes.box(-0.75f, -0.875f, 0.5625f, -0.6875f, 1.375f, 0.75f),
                Shapes.box(0.6875f, -0.875f, 0.5625f, 0.75f, 1.375f, 0.75f),
                Shapes.box(0.625f, -0.875f, 0.375f, 0.6875f, 1.375f, 0.5625f),
                Shapes.box(-0.6875f, -0.875f, 0.375f, -0.625f, 1.375f, 0.5625f),
                Shapes.box(-0.8125f, -0.875f, 1f, -0.75f, 1.375f, 1.25f),
                Shapes.box(0.75f, -0.875f, 1f, 0.8125f, 1.375f, 1.25f),
                Shapes.box(0.5625f, -0.875f, 0.3125f, 0.625f, 1.375f, 0.375f),
                Shapes.box(-0.625f, -0.875f, 0.3125f, -0.5625f, 1.375f, 0.375f)).move(0, 1, 0);
        PARTS = ImmutableMap.<Integer, Part>builder()
            .put(0, new Part(0,0,0))
            .put(1, new Part(0,1,0))
            .put(2, new Part(0,2,0))
            .put(3, new Part(-1,2,0))
            .put(4, new Part(-1,1,0))
            .put(5, new Part(-1,0,0))
            .put(6, new Part(0,0,1))
            .put(7, new Part(0,1,1))
            .put(8, new Part(0,2,1))
            .put(9, new Part(-1,2,1))
            .put(10, new Part(-1,1,1))
            .put(11, new Part(-1,0,1)).build();
    }
}