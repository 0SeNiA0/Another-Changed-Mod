package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.zaharenko424.testmod.registry.SoundRegistry;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class AbstractMultiDoor extends HorizontalDirectionalBlock {

    public static final BooleanProperty OPEN= BlockStateProperties.OPEN;

    public AbstractMultiDoor(Properties p_54120_) {
        super(p_54120_);
    }

    abstract IntegerProperty part();

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60507_!=InteractionHand.MAIN_HAND) return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
        if(p_60504_.isClientSide) {
            p_60504_.playSound(p_60506_,p_60505_,SoundRegistry.TEST_SOUND.get(),SoundSource.BLOCKS);//TODO replace with actual sounds
            return InteractionResult.SUCCESS;
        }
        BlockPos mainPos= getMainPos(p_60503_,p_60505_);
        setOpenClose(p_60504_.getBlockState(mainPos),mainPos,p_60504_);
        p_60504_.playSound(p_60506_,p_60505_, SoundRegistry.TEST_SOUND.get(), SoundSource.BLOCKS);
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        if(!canSurvive(p_60541_,p_60544_,p_60545_)){
            BlockPos main=getMainPos(p_60541_,p_60545_);
            BlockState air=Blocks.AIR.defaultBlockState();
            if(!p_60544_.getBlockState(main).is(Blocks.AIR)) p_60544_.setBlock(main,air,3);
            return air;
        }
        return p_60541_;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
        p_49915_.add(FACING,OPEN,part());
    }

    abstract void setOpenClose(BlockState mainState, BlockPos mainPos, LevelAccessor level);

    abstract BlockPos getMainPos(BlockState state, BlockPos pos);
}