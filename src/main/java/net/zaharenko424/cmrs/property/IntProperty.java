package net.zaharenko424.cmrs.property;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;

public record IntProperty(int value) implements ModelProperty {

    public static final StreamCodec<FriendlyByteBuf, IntProperty> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            IntProperty::value,
            IntProperty::new
    );

    @Override
    public DeferredHolder<ModelPropertyType<?>, ?> type() {
        return ModelPropertyRegistry.INT;
    }
}
