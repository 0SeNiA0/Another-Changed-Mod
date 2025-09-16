package net.zaharenko424.a_changed.ability.api;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;

public interface AttachmentSelfSyncHandler <T> extends AttachmentSyncHandler<T> {

    /**
     * Writes attachment data to a buffer.
     *
     * <p>If {@code initialSync} is {@code true},
     * the data should be written in full because the client does not have any previous data.
     *
     * <p>If {@code initialSync} is {@code false},
     * the client already received a previous version of the data.
     * In this case, this method is only called once for the attachment,
     * and the resulting data is broadcast only to the holder.
     *
     * <p>If nothing is written to the buffer, nothing is sent to the client at all,
     * and {@link #read} will not be called on the client side.
     */
    default void writeToSelf(RegistryFriendlyByteBuf buf, T attachment, boolean initialSync){
        write(buf, attachment, initialSync);
    }
}
