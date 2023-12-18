package net.zaharenko424.a_changed.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.a_changed.client.screen.NoteScreen;
import net.zaharenko424.a_changed.util.Utils;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
@ParametersAreNonnullByDefault
public class ClientboundOpenNotePacket implements SimpleMessage {

    private final List<String> text;
    private final BlockPos notePos;
    private final boolean finalized;
    private final int guiId;

    public ClientboundOpenNotePacket(List<String> text, BlockPos notePos, boolean isFinalized, int guiId){
        this.text=text;
        this.notePos=notePos;
        finalized=isFinalized;
        this.guiId=guiId;
    }

    public ClientboundOpenNotePacket(FriendlyByteBuf buffer){
        text=new ArrayList<>();
        Utils.readFromTag(buffer.readNbt(),text);
        notePos=buffer.readBlockPos();
        finalized=buffer.readBoolean();
        guiId=buffer.readInt();
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        CompoundTag tag=new CompoundTag();
        Utils.writeToTag(tag,text);
        buffer.writeNbt(tag);
        buffer.writeBlockPos(notePos);
        buffer.writeBoolean(finalized);
        buffer.writeInt(guiId);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void handleMainThread(NetworkEvent.Context context) {
        LocalPlayer player= Minecraft.getInstance().player;
        if(player==null) return;
        Minecraft.getInstance().setScreen(new NoteScreen(notePos,text,finalized,guiId));
    }
}
