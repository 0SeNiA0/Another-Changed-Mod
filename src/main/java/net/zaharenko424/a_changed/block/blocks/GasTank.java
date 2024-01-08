package net.zaharenko424.a_changed.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.zaharenko424.a_changed.block.VerticalTwoBlockMultiBlock;
import net.zaharenko424.a_changed.entity.block.GasTankEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public class GasTank extends VerticalTwoBlockMultiBlock implements EntityBlock {

    private static final VoxelShape SHAPE_0 = Shapes.or(Shapes.box(0.3125, 0, 0.25, 0.6875, 1.125, 0.75),
            Shapes.box(0.6875, 0, 0.3125, 0.75, 1.125, 0.6875),
            Shapes.box(0.25, 0, 0.3125, 0.3125, 1.125, 0.6875),
            Shapes.box(0.375, 1.25, 0.375, 0.625, 1.4375, 0.625),
            Shapes.box(0.625, 1.125, 0.375, 0.6875, 1.25, 0.625),
            Shapes.box(0.3125, 1.125, 0.375, 0.375, 1.25, 0.625),
            Shapes.box(0.375, 1.125, 0.3125, 0.625, 1.25, 0.6875));
    private static final VoxelShape SHAPE_1 = SHAPE_0.move(0,-1,0);

    public GasTank(Properties p_54120_) {
        super(p_54120_);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
        return p_153216_.getValue(PART)==1?null:new GasTankEntity(p_153215_,p_153216_);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
        return p_60555_.getValue(PART)==0?SHAPE_0:SHAPE_1;
    }

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide||p_60507_!=InteractionHand.MAIN_HAND) return InteractionResult.SUCCESS;
        if(p_60504_.getBlockEntity(getMainPos(p_60503_,p_60505_)) instanceof GasTankEntity canister){
            canister.setOpenClose();//TODO make better sound
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void onRemove(BlockState p_60515_, Level p_60516_, BlockPos p_60517_, BlockState p_60518_, boolean p_60519_) {
        if(p_60518_.is(p_60515_.getBlock())) return;
        BlockEntity entity=p_60516_.getBlockEntity(p_60517_);
        if(entity instanceof GasTankEntity canister){
            popResource(p_60516_,p_60517_,canister.canister());
        }
        super.onRemove(p_60515_,p_60516_,p_60517_,p_60518_,p_60519_);
    }

    @Override
    public void setPlacedBy(Level p_49847_, BlockPos p_49848_, BlockState p_49849_, @Nullable LivingEntity p_49850_, ItemStack p_49851_) {
        BlockEntity entity=p_49847_.getBlockEntity(p_49848_);
        if(entity instanceof GasTankEntity canister)canister.setCanister(p_49851_);
        super.setPlacedBy(p_49847_, p_49848_, p_49849_, p_49850_, p_49851_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return p_153212_.isClientSide||p_153213_.getValue(PART)==1? null:(a,b,c,d)->{
            if(d instanceof GasTankEntity canister) canister.tick();
        };
    }
}