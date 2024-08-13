package net.zaharenko424.a_changed.network.packets.ability;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

public record ServerboundActivateAbilityPacket(boolean oneShot, FriendlyByteBuf additionalData) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("activate_ability");

    public ServerboundActivateAbilityPacket(FriendlyByteBuf buf){
        this(buf.readBoolean(), new FriendlyByteBuf(Unpooled.wrappedBuffer(buf.readByteArray())));
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBoolean(oneShot);
        if(additionalData != null) {
            //If backing array is not fully used, copy the used part to dummy and write that.
            if(additionalData.readableBytes() == additionalData.array().length) {
                buf.writeByteArray(additionalData.array());
            } else {
                byte[] dummy = new byte[additionalData.readableBytes()];
                System.arraycopy(additionalData.array(), 0, dummy, 0, dummy.length);
                buf.writeByteArray(dummy);
            }
        } else buf.writeByteArray(new byte[0]);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}