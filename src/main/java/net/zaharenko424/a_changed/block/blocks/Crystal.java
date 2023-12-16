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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.TransfurDamageSource;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class Crystal extends Block {
    private final AbstractTransfurType transfurType;

    public Crystal(Properties p_49795_, AbstractTransfurType transfurType) {
        super(p_49795_.friction(.8f).speedFactor(.5f).jumpFactor(.2f).noCollission());
        this.transfurType=transfurType;
    }

    @Override
    public void stepOn(Level p_152431_, BlockPos p_152432_, BlockState p_152433_, Entity p_152434_) {
        if(TransfurDamageSource.checkTarget(p_152434_)) TransfurManager.addTransfurProgress((LivingEntity) p_152434_,10,transfurType);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Shapes.create(.375,0,.375,.625,0,.625);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos below=p_60527_.below();
        return p_60526_.getBlockState(below).isFaceSturdy(p_60526_,below, Direction.UP);
    }
}