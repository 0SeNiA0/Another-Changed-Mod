package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record ServerboundTransfurChoicePacket(boolean becomeTransfur) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("transfur_choice");

    public ServerboundTransfurChoicePacket(FriendlyByteBuf buffer){
        this(buffer.readBoolean());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBoolean(becomeTransfur);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}