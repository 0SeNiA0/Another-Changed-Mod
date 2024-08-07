package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public record ClientboundTransfurSyncPacket(int holderId, float transfurProgress, boolean isTransfurred, TransfurType transfurType, TransfurType transfurTypeO) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("transfur_sync");

    public ClientboundTransfurSyncPacket(@NotNull FriendlyByteBuf buffer){
        this(buffer.readVarInt(), buffer.readFloat(), buffer.readBoolean(), TransfurManager.getTransfurType(buffer.readResourceLocation()),
                TransfurManager.getTransfurType(buffer.readResourceLocation()));
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeVarInt(holderId);
        buffer.writeFloat(transfurProgress);
        buffer.writeBoolean(isTransfurred);

        if(transfurType != null) {
            buffer.writeResourceLocation(transfurType.id);
        } else buffer.writeResourceLocation(Utils.NULL_LOC);

        if(transfurTypeO != null) {
            buffer.writeResourceLocation(transfurTypeO.id);
        } else buffer.writeResourceLocation(Utils.NULL_LOC);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}