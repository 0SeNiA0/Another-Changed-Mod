package net.zaharenko424.a_changed.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.entity.block.NoteEntity;
import net.zaharenko424.a_changed.network.packets.ClientboundOpenNotePacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
@ParametersAreNonnullByDefault
public abstract class AbstractNote extends HorizontalDirectionalBlock implements EntityBlock {

    public AbstractNote(Properties properties) {
        super(properties);
    }

    public abstract int guiId();

    public boolean use(Level level, BlockPos pos, Player player) {
        if(level.isClientSide) return false;
        BlockEntity entity = level.getBlockEntity(pos);
        if(entity instanceof NoteEntity note){
            PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientboundOpenNotePacket(note.getText(), note.getBlockPos(), note.isFinalized(), guiId()));
            return true;
        }
        return false;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        return use(level, pos, player) ? InteractionResult.SUCCESS_NO_ITEM_USED : super.useWithoutItem(state, level, pos, player, hitResult);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return use(level, pos, player) ? ItemInteractionResult.SUCCESS : super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return !canSurvive(state, level, pos) ? Blocks.AIR.defaultBlockState()
                : super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }
}