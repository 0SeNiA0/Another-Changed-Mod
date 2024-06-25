package net.zaharenko424.a_changed.attachments;

import net.minecraft.network.FriendlyByteBuf;

public interface SyncedAttachment {

    void read(FriendlyByteBuf buf);

    void write(FriendlyByteBuf buf);
}