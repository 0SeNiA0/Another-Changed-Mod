package net.zaharenko424.cmrs.client.property;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;

public record StringProperty(String str) implements ModelProperty {

    public static final StreamCodec<FriendlyByteBuf, StringProperty> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            StringProperty::str,
            StringProperty::new
    );

    @Override
    public DeferredHolder<ModelPropertyType<?>, ?> type() {
        return ModelPropertyRegistry.STRING;
    }
}
