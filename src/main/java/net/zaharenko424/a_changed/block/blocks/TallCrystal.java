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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.AbstractMultiBlock;
import net.zaharenko424.a_changed.transfurSystem.DamageSources;
import net.zaharenko424.a_changed.transfurSystem.TransfurEvent;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.util.StateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class TallCrystal extends AbstractMultiBlock {

    private static final VoxelShape SHAPE0 = Shapes.or(
            Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75),
            Shapes.box(0.375, 1, 0.375, 0.625, 2, 0.625));
    private static final VoxelShape SHAPE1 = SHAPE0.move(0,-1,0);
    private static final AABB aabb0 = SHAPE0.bounds();
    private static final AABB aabb1 = SHAPE1.bounds();
    private final Supplier<? extends AbstractTransfurType> transfurType;
    public static IntegerProperty PART = StateProperties.PART2;

    public TallCrystal(Properties p_54120_, Supplier<? extends AbstractTransfurType> transfurType) {
        super(p_54120_.friction(.9f).speedFactor(.4f).jumpFactor(.2f).noCollission());
        registerDefaultState(stateDefinition.any().setValue(PART,0));
        this.transfurType=transfurType;
    }

    @Override
    protected IntegerProperty part() {
        return PART;
    }

    @Override
    public void entityInside(BlockState p_60495_, Level p_60496_, BlockPos p_60497_, Entity p_60498_) {
        if(p_60496_.isClientSide||p_60498_.tickCount%10!=0) return;
        if(!p_60498_.getBoundingBox().intersects((p_60495_.getValue(PART) == 0 ? aabb0 : aabb1).move(p_60497_))
                || !DamageSources.checkTarget(p_60498_)) return;
        TransfurEvent.ADD_TRANSFUR_CRYSTAL.accept((LivingEntity) p_60498_, transfurType.get(), 5f);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return p_60555_.getValue(PART) == 0 ? SHAPE0 : SHAPE1;
    }

    @Override
    public boolean canSurvive(BlockState p_60525_, LevelReader p_60526_, BlockPos p_60527_) {
        BlockPos pos = p_60527_.below();
        BlockState below = p_60526_.getBlockState(pos);
        return (p_60525_.getValue(PART) == 0 && below.isFaceSturdy(p_60526_, pos, Direction.UP)) || below.is(this);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        p_49847_.setBlock(p_49848_.above(),p_49849_.setValue(PART,1),3);
    }

    @Override
    protected BlockPos getMainPos(BlockState state, BlockPos pos) {
        return state.getValue(PART)==0?pos:pos.below();
    }
}