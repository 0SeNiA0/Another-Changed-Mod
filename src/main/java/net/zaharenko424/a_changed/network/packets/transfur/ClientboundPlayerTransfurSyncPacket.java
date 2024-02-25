package net.zaharenko424.a_changed.network.packets.transfur;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.ITransfurHandler;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.util.NBTUtils;
import org.jetbrains.annotations.NotNull;

import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TYPE_KEY;

public record ClientboundPlayerTransfurSyncPacket(float transfurProgress, AbstractTransfurType transfurType, boolean isTransfurred) implements CustomPacketPayload {

    public static final ResourceLocation ID = AChanged.resourceLoc("player_transfur_sync");

    public ClientboundPlayerTransfurSyncPacket(@NotNull ITransfurHandler handler){
        this(handler.getTransfurProgress(), handler.getTransfurType(), handler.isTransfurred());
    }

    public ClientboundPlayerTransfurSyncPacket(@NotNull FriendlyByteBuf buffer){
        this(buffer.readFloat(), NBTUtils.readTransfurType(buffer.readNbt()), buffer.readBoolean());
    }

    public void write(@NotNull FriendlyByteBuf buffer){
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