package net.zaharenko424.cmrs.network.packets;

import com.google.common.collect.ImmutableMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.ServerConfig;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record ServerboundModelPropertySync(Map<String, ModelProperty> properties) implements CustomPacketPayload {

    public static final Type<ServerboundModelPropertySync> TYPE = new Type<>(CMRS.resourceLoc("sb_model_property"));

    public static final StreamCodec<FriendlyByteBuf, ServerboundModelPropertySync> CODEC = StreamCodec.composite(
            ModelPropertyRegistry.MAP,
            ServerboundModelPropertySync::properties,
            ServerboundModelPropertySync::new
    );

    public ServerboundModelPropertySync(Map<String, ModelProperty> properties) {
        int limit = ServerConfig.MAX_PROPERTIES.getAsInt();
        if (properties.size() <= limit) {
            this.properties = Map.copyOf(properties);
            return;
        }

        ImmutableMap.Builder<String, ModelProperty> builder = new ImmutableMap.Builder<>();
        int i = 0;
        for (Map.Entry<String, ModelProperty> entry : properties.entrySet()) {
            if (i >= limit) break;
            i++;

            builder.put(entry);
        }

        this.properties = builder.build();
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
