package net.zaharenko424.a_changed.block.machines;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.zaharenko424.a_changed.block.blocks.ConnectedTextureBlock;
import net.zaharenko424.a_changed.entity.block.machines.CopperWireEntity;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WireBlock extends ConnectedTextureBlock implements EntityBlock {

    private static final VoxelShape SHAPE = Shapes.box(0.4062f, 0.4062f, 0.4062f, 0.5938f, 0.5938f, 0.5938f);
    private static final VoxelShape SHAPE_N = Shapes.box(0.4062f, 0.4062f, 0f, 0.5938f, 0.5938f, 0.4062f);
    private static final VoxelShape SHAPE_U = Shapes.box(0.4062f, 0.5938f, 0.4062f, 0.5938f, 1f, 0.5938f);
    private static final VoxelShape SHAPE_D = Shapes.box(0.4062f, 0f, 0.4062f, 0.5938f, 0.4062f, 0.5938f);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public WireBlock(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new CopperWireEntity(pPos, pState);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        boolean u = state.getValue(UP);
        boolean d = state.getValue(DOWN);
        boolean n = state.getValue(NORTH);
        boolean e = state.getValue(EAST);
        boolean s = state.getValue(SOUTH);
        boolean w = state.getValue(WEST);

        int id = (u ? 1 : 0) + (d ? 10 : 0) + (n ? 100 : 0) + (e ? 1000 : 0) + (s ? 10000 : 0) + (w ? 100000 : 0);

        if(id == 0) return SHAPE;

        return CACHE.getShape(Direction.NORTH, id, ()-> {
            VoxelShape shape = SHAPE;
            if(u) shape = Shapes.or(shape, SHAPE_U);
            if(d) shape = Shapes.or(shape, SHAPE_D);
            if(n) shape = Shapes.or(shape, SHAPE_N);
            if(e) shape = Shapes.or(shape, Utils.rotateShape(Direction.EAST, SHAPE_N));
            if(s) shape = Shapes.or(shape, Utils.rotateShape(Direction.SOUTH, SHAPE_N));
            if(w) shape = Shapes.or(shape, Utils.rotateShape(Direction.WEST, SHAPE_N));
            return shape;
        });
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState p_60543_, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos p_60546_) {
        BlockEntity entity = level.getBlockEntity(p_60546_);
        return state.setValue(propByDirection.get(direction), p_60543_.is(this)
                || entity != null && entity.getCapability(Capabilities.ENERGY).isPresent());
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockState state = defaultBlockState();
        Level level = context.getLevel();
        BlockEntity entity;
        for(Direction direction : Direction.values()){
            pos.setWithOffset(context.getClickedPos(), direction);
            entity = level.getBlockEntity(pos);
            state = state.setValue(propByDirection.get(direction), level.getBlockState(pos).is(this)
                    || entity != null && entity.getCapability(Capabilities.ENERGY).isPresent());
        }
        return state;
    }
}