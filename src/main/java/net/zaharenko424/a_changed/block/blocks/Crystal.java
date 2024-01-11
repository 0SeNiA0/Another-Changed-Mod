package net.zaharenko424.a_changed.block.blocks;

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
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Crystal extends Block {

    private static final VoxelShape SHAPE = Shapes.create(.375,0,.375,.625,.75,.625);
    private static final AABB aabb=SHAPE.bounds();
    private final Supplier<? extends AbstractTransfurType> transfurType;

    public Crystal(Properties p_49795_, Supplier<? extends AbstractTransfurType> transfurType) {
        super(p_49795_.friction(.9f).speedFactor(.4f).jumpFactor(.2f).noCollission());
        this.transfurType=transfurType;
    }

    @Override
    public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity p_60498_) {
        if(p_60496_.isClientSide || p_60498_.tickCount % 10 != 0) return;
        if(!p_60498_.getBoundingBox().intersects(aabb.move(p_60497_)) || !DamageSources.checkTarget(p_60498_)) return;
        TransfurEvent.ADD_TRANSFUR_CRYSTAL.accept((LivingEntity) p_60498_, transfurType.get(), 5f);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return canSurvive(p_60541_,p_60544_,p_60545_) ? p_60541_ : Blocks.AIR.defaultBlockState();
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        return canSupportCenter(p_60526_,p_60527_.below(),Direction.UP);
    }
}