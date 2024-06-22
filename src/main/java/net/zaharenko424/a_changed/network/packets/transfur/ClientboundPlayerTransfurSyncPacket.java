package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;

public record ClientboundPlayerTransfurSyncPacket(int holderId, float transfurProgress, TransfurType transfurType, boolean isTransfurred) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("transfur_sync");

    public ClientboundPlayerTransfurSyncPacket(@NotNull FriendlyByteBuf buffer){
        this(buffer.readVarInt(), buffer.readFloat(), TransfurManager.getTransfurType(buffer.readResourceLocation()),
                buffer.readBoolean());
    }

    public ClientboundPlayerTransfurSyncPacket(int holderId, @NotNull ITransfurHandler handler){
        this(holderId, handler.getTransfurProgress(), handler.getTransfurType(), handler.isTransfurred());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeVarInt(holderId);
        buffer.writeFloat(transfurProgress);
        buffer.writeResourceLocation(transfurType.id);
        buffer.writeBoolean(isTransfurred);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}