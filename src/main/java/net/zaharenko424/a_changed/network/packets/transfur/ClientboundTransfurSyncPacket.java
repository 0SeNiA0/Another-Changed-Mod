package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public record ClientboundTransfurSyncPacket(int holderId, ResourceLocation abilityId, float transfurProgress, boolean isTransfurred, TransfurType transfurType, TransfurType transfurTypeO) implements CustomPacketPayload {

    public static final Type<ClientboundTransfurSyncPacket> TYPE = new Type<>(AChanged.resourceLoc("transfur_sync"));

    public ClientboundTransfurSyncPacket(@NotNull FriendlyByteBuf buf){
        this(buf.readVarInt(), buf.readResourceLocation(), buf.readFloat(), buf.readBoolean(), TransfurManager.getTransfurType(buf.readResourceLocation()),
                TransfurManager.getTransfurType(buf.readResourceLocation()));
    }

    public Ability ability(){
        return abilityId.equals(Utils.NULL_LOC) ? null : Objects.requireNonNull(AbilityRegistry.ABILITY_REGISTRY.get(abilityId), "Client-Server ability desync! Compare mod lists.");
    }

    public static final StreamCodec<FriendlyByteBuf, ClientboundTransfurSyncPacket> CODEC = StreamCodec.of((buf, packet) -> {
        buf.writeVarInt(packet.holderId);
        buf.writeResourceLocation(packet.abilityId);
        buf.writeFloat(packet.transfurProgress);
        buf.writeBoolean(packet.isTransfurred);

        if(packet.transfurType != null) {
            buf.writeResourceLocation(packet.transfurType.id);
        } else buf.writeResourceLocation(Utils.NULL_LOC);

        if(packet.transfurTypeO != null) {
            buf.writeResourceLocation(packet.transfurTypeO.id);
        } else buf.writeResourceLocation(Utils.NULL_LOC);
    }, ClientboundTransfurSyncPacket::new);

    @Override
    public @NotNull Type<ClientboundTransfurSyncPacket> type() {
        return TYPE;
    }
}