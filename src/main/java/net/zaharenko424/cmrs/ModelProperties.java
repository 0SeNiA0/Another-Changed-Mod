package net.zaharenko424.cmrs;

import io.netty.buffer.Unpooled;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.IAttachmentCopyHandler;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.AttachmentRegistry;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ParametersAreNonnullByDefault
public class ModelProperties {

    public static final Sync SYNC = new Sync();
    public static final Serializer SERIALIZER = new Serializer();
    public static final IAttachmentCopyHandler<ModelProperties> COPY_HANDLER =
            (attachment, holder, provider) -> new ModelProperties(holder, attachment.properties);

    private final LivingEntity holder;

    private final Map<String, ModelProperty> properties;
    private Map<String, ModelProperty> view;

    public ModelProperties(IAttachmentHolder holder){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException("ModelPropertyAttachment: Unsupported holder " + holder);
        this.holder = entity;
        properties = entity.level().isClientSide ? new ConcurrentHashMap<>() : new HashMap<>();
    }

    private ModelProperties(IAttachmentHolder holder, Map<String, ModelProperty> properties){
        if(!(holder instanceof LivingEntity entity)) throw new IllegalArgumentException("ModelPropertyAttachment: Unsupported holder " + holder);
        this.holder = entity;
        this.properties = properties;
    }

    public Map<String, ModelProperty> properties(){
        if(view == null) view = Collections.unmodifiableMap(properties);
        return view;
    }

    @ApiStatus.Internal
    public void set(Map<String, ModelProperty> properties){
        if(this.properties.equals(properties)) return;

        this.properties.clear();
        this.properties.putAll(properties);
    }

    @ApiStatus.Internal
    public void syncPlayers(){
        if(holder.level().isClientSide) return;
        holder.syncData(AttachmentRegistry.MODEL_PROPERTIES);
    }

    public static class Sync implements AttachmentSyncHandler<ModelProperties> {

        private Sync(){}

        @Override
        public boolean sendToPlayer(IAttachmentHolder holder, ServerPlayer to) {
            return !holder.equals(to);//TODO maybe allow clients without cmrs to join & just don't send them packets?
        }

        @Override
        public void write(RegistryFriendlyByteBuf buf, ModelProperties attachment, boolean initialSync) {
            ModelPropertyRegistry.MAP.encode(buf, attachment.properties);
        }

        @Override
        public @Nullable ModelProperties read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable ModelProperties previousValue) {
            if(!(holder instanceof LivingEntity)) return null;
            if(previousValue == null) return new ModelProperties(holder, ModelPropertyRegistry.MAP.decode(buf));

            previousValue.set(ModelPropertyRegistry.MAP.decode(buf));

            return previousValue;
        }
    }

    public static class Serializer implements IAttachmentSerializer<ByteArrayTag, ModelProperties> {

        private Serializer(){}

        @Override
        public @NotNull ModelProperties read(IAttachmentHolder holder, ByteArrayTag tag, HolderLookup.Provider provider) {
            ModelProperties properties = new ModelProperties(holder);

            properties.set(ModelPropertyRegistry.MAP.decode(new FriendlyByteBuf(Unpooled.wrappedBuffer(tag.getAsByteArray()))));
            return properties;
        }

        @Override
        public @Nullable ByteArrayTag write(ModelProperties attachment, HolderLookup.Provider provider) {
            if(attachment.holder instanceof Player) return null;//Only save properties for entities

            FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
            ModelPropertyRegistry.MAP.encode(buf, attachment.properties);
            byte[] data = new byte[buf.readableBytes()];
            buf.readBytes(data);
            buf.release();

            return new ByteArrayTag(data);
        }
    }
}
