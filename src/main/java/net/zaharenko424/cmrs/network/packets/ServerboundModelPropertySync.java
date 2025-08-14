package net.zaharenko424.cmrs.network.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.zaharenko424.cmrs.CMRS;
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

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
