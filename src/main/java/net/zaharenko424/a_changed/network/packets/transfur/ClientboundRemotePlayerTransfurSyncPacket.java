package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TYPE_KEY;

public record ClientboundRemotePlayerTransfurSyncPacket(UUID playerId, float transfurProgress, TransfurType transfurType, boolean isTransfurred) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("remote_player_transfur_sync");

    public ClientboundRemotePlayerTransfurSyncPacket(@NotNull ITransfurHandler handler, @NotNull UUID uuid){
        this(uuid, handler.getTransfurProgress(), handler.getTransfurType(), handler.isTransfurred());
    }

    public ClientboundRemotePlayerTransfurSyncPacket(@NotNull FriendlyByteBuf buffer){
        this(buffer.readUUID(), buffer.readFloat(), NBTUtils.readTransfurType(buffer.readNbt()), buffer.readBoolean());
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buffer) {
        buffer.writeUUID(playerId);
        buffer.writeFloat(transfurProgress);

        CompoundTag tag = new CompoundTag();
        if(transfurType != null) tag.putString(TRANSFUR_TYPE_KEY, transfurType.id.toString());
        buffer.writeNbt(tag);

        buffer.writeBoolean(isTransfurred);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}