package net.zaharenko424.testmod.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.block.blockEntity.NoteEntity;
import net.zaharenko424.testmod.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
@ParametersAreNonnullByDefault
public class ServerboundEditNotePacket implements SimpleMessage {

    private final List<String> text;
    private final BlockPos notePos;
    private final boolean finalize;

    public ServerboundEditNotePacket(List<String> text, BlockPos notePos, boolean finalize){
        this.text=text;
        this.notePos=notePos;
        this.finalize=finalize;
    }

    public ServerboundEditNotePacket(FriendlyByteBuf buffer){
        text=new ArrayList<>();
        Utils.readFromTag(buffer.readNbt(),text);
        notePos=buffer.readBlockPos();
        finalize=buffer.readBoolean();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        CompoundTag tag=new CompoundTag();
        Utils.writeToTag(tag,text);
        buffer.writeNbt(tag);
        buffer.writeBlockPos(notePos);
        buffer.writeBoolean(finalize);
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        ServerPlayer sender=context.getSender();
        if(sender==null) {
            TestMod.LOGGER.warn("Received a packet from player which is not on the server!");
            return;
        }
        BlockEntity entity=sender.level().getBlockEntity(notePos);
        if(entity instanceof NoteEntity note){
            note.setText(text,finalize);
            return;
        }
        TestMod.LOGGER.warn("Block position does not contain NoteEntity! ("+notePos+")");
    }
}