package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.TransfurDamageSource;
import net.zaharenko424.a_changed.TransfurManager;
import net.zaharenko424.a_changed.block.AbstractMultiBlock;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class TallCrystal extends AbstractMultiBlock {

    private final AbstractTransfurType transfurType;
    public static IntegerProperty PART = StateProperties.PART;

    public TallCrystal(Properties p_54120_, AbstractTransfurType transfurType) {
        super(p_54120_.friction(.8f).speedFactor(.5f).jumpFactor(.2f).noCollission());
        registerDefaultState(stateDefinition.any().setValue(PART,0));
        this.transfurType=transfurType;
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    public void stepOn(Level p_152431_, BlockPos p_152432_, BlockState p_152433_, Entity p_152434_) {
        AChanged.LOGGER.warn("stepOn");
        if(TransfurDamageSource.checkTarget(p_152434_)) TransfurManager.addTransfurProgress((LivingEntity) p_152434_,10,transfurType);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return Shapes.create(.375,0,.375,.625,0,.625);
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos below=p_60527_.below();
        return (p_60525_.getValue(PART) == 0&&p_60526_.getBlockState(below).isFaceSturdy(p_60526_,below, Direction.UP)) || p_60526_.getBlockState(p_60527_.below()).is(this);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        p_49847_.setBlock(p_49848_.above(),p_49849_.setValue(PART,1),3);
    }

    @Override
    protected BlockPos getMainPos(BlockState state, BlockPos pos) {
        return state.getValue(PART)==0?pos:pos.below();
    }

    @Override
    protected BlockPos getSecondaryPos(BlockState state, BlockPos pos) {
        return state.getValue(PART)==1?pos:pos.above();
    }
}