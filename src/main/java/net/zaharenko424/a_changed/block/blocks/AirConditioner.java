package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.HorizontalTwoBlockMultiBlock;
import net.zaharenko424.a_changed.entity.block.AirConditionerEntity;
import net.zaharenko424.a_changed.util.VoxelShapeCache;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class AirConditioner extends HorizontalTwoBlockMultiBlock implements EntityBlock {

    private static final VoxelShape SHAPE_0 = Shapes.box(-0.625, 0.03125, 0.4375, 0.875, 1, 1);
    private static final VoxelShape SHAPE_1 = SHAPE_0.move(1,0,0);
    private static final VoxelShapeCache CACHE = new VoxelShapeCache();

    public AirConditioner(Properties p_54120_) {
        super(p_54120_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return p_153216_.getValue(PART)==1?null:new AirConditionerEntity(p_153215_,p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        Direction direction= p_60555_.getValue(FACING);
        return p_60555_.getValue(PART)==0? CACHE.getShape(direction,0,SHAPE_0): CACHE.getShape(direction,1,SHAPE_1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide?null:(a,b,c,d)->{
            if(d instanceof AirConditionerEntity conditioner) conditioner.tick();
        };
    }
}