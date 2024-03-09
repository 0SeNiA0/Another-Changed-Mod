package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public record ClientboundTransfurToleranceSyncPacket(float transfurTolerance) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("transfur_tolerance_sync");

    public ClientboundTransfurToleranceSyncPacket(){
        this(TransfurManager.TRANSFUR_TOLERANCE);
    }

    public ClientboundTransfurToleranceSyncPacket(FriendlyByteBuf buffer){
        this(buffer.readFloat());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeFloat(transfurTolerance);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}