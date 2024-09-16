package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record ServerboundTransfurChoicePacket(boolean becomeTransfur) implements CustomPacketPayload {

    public static final Type<ServerboundTransfurChoicePacket> TYPE = new Type<>(AChanged.resourceLoc("transfur_choice"));

    public ServerboundTransfurChoicePacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean());
    }

    public static final StreamCodec<FriendlyByteBuf, ServerboundTransfurChoicePacket> CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeBoolean(packet.becomeTransfur),
            ServerboundTransfurChoicePacket::new);

    @Override
    public @NotNull Type<ServerboundTransfurChoicePacket> type() {
        return TYPE;
    }
}