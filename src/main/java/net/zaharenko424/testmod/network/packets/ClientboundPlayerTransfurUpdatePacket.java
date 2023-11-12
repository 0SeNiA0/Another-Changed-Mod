package net.zaharenko424.testmod.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.simple.SimpleMessage;
import net.zaharenko424.testmod.TransfurType;
import net.zaharenko424.testmod.entity.TransfurHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static net.zaharenko424.testmod.TransfurManager.*;

public class ClientboundPlayerTransfurUpdatePacket implements SimpleMessage {
    private final int transfurProgress;
    private final ResourceLocation transfurType;
    private final boolean isTransfurred;

    public ClientboundPlayerTransfurUpdatePacket(int transfurProgress, @Nullable TransfurType transfurType, boolean isTransfurred){
        this.transfurProgress=transfurProgress;
        this.transfurType=transfurType!=null?transfurType.resourceLocation:null;
        this.isTransfurred=isTransfurred;
    }

    public ClientboundPlayerTransfurUpdatePacket(@NotNull FriendlyByteBuf buffer){
        CompoundTag tag=buffer.readNbt();
        if(tag==null) throw new RuntimeException("Empty or corrupted packet received!");
        transfurProgress = tag.getInt(TRANSFUR_PROGRESS_KEY);
        if(tag.contains(TRANSFUR_TYPE_KEY)) transfurType = new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY)); else transfurType=null;
        isTransfurred = tag.getBoolean(TRANSFURRED_KEY);
    }

    public void encode(@NotNull FriendlyByteBuf buffer){
        CompoundTag tag=new CompoundTag();
        tag.putInt(TRANSFUR_PROGRESS_KEY, transfurProgress);
        if(transfurType!=null) tag.putString(TRANSFUR_TYPE_KEY, transfurType.toString());
        tag.putBoolean(TRANSFURRED_KEY, isTransfurred);
        buffer.writeNbt(tag);
    }

    @Override
    public void handleMainThread(NetworkEvent.Context context) {
        TransfurHolder holder=(TransfurHolder) Minecraft.getInstance().player;
        if(holder==null) return;
        Minecraft.getInstance().player.sendSystemMessage(Component.literal("Transfur progress is "+transfurProgress));
        if(!isTransfurred) {
            if (holder.mod$isTransfurred()) {
                holder.mod$unTransfur();
            } else holder.mod$setTransfurProgress(transfurProgress, transfurType);
            return;
        }
        holder.mod$transfur(transfurType);
    }
}
