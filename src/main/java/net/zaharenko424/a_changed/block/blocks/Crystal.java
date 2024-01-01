package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.transfurSystem.TransfurDamageSource;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.TransfurSource;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Crystal extends Block {

    private static final VoxelShape SHAPE = Shapes.create(.375,0,.375,.625,.875,.625);
    private static final AABB aabb=SHAPE.bounds();
    private final Supplier<? extends AbstractTransfurType> transfurType;

    public Crystal(Properties p_49795_, Supplier<? extends AbstractTransfurType> transfurType) {
        super(p_49795_.friction(.9f).speedFactor(.4f).jumpFactor(.2f).noCollission());
        this.transfurType=transfurType;
    }

    @Override
    public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity p_60498_) {
        if(p_60498_.tickCount%10!=0) return;
        if(p_60498_.getBoundingBox().intersects(aabb.move(p_60497_))&&TransfurDamageSource.checkTarget(p_60498_)&&!TransfurManager.isBeingTransfurred((LivingEntity) p_60498_)) {
            TransfurManager.addTransfurProgress((LivingEntity) p_60498_,5,transfurType.get(), TransfurSource.CRYSTAL);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        return canSupportCenter(p_60526_,p_60527_.below(),Direction.UP);
    }
}