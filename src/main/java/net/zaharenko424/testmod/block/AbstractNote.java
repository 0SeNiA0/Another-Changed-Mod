package net.zaharenko424.testmod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.testmod.entity.block.NoteEntity;
import net.zaharenko424.testmod.network.PacketHandler;
import net.zaharenko424.testmod.network.packets.ClientboundOpenNotePacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
@ParametersAreNonnullByDefault
@SuppressWarnings("deprecation")
public abstract class AbstractNote extends HorizontalDirectionalBlock implements EntityBlock {
    public AbstractNote(Properties p_49795_) {
        super(p_49795_);
    }

    public abstract int guiId();

    @Override
    public @NotNull InteractionResult use(BlockState p_60503_, Level p_60504_, BlockPos p_60505_, Player p_60506_, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if(p_60504_.isClientSide) return super.use(p_60503_, p_60504_, p_60505_, p_60506_, p_60507_, p_60508_);
        BlockEntity entity=p_60504_.getBlockEntity(p_60505_);
        if(entity instanceof NoteEntity note){
            PacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(()->(ServerPlayer)p_60506_), new ClientboundOpenNotePacket(note.getText(),note.getBlockPos(),note.isFinalized(),guiId()));
            return InteractionResult.SUCCESS;
        }
        return super.use(p_60503_,p_60504_,p_60505_,p_60506_,p_60507_,p_60508_);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState p_60541_, Direction p_60542_, BlockState p_60543_, LevelAccessor p_60544_, BlockPos p_60545_, BlockPos p_60546_) {
        return !canSurvive(p_60541_,p_60544_,p_60545_)? Blocks.AIR.defaultBlockState():super.updateShape(p_60541_, p_60542_, p_60543_, p_60544_, p_60545_, p_60546_);
    }
}