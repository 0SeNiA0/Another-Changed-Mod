package net.zaharenko424.cmrs.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;

public record FloatProperty(float f) implements ModelProperty {

    public static final Codec<FloatProperty> CODEC_ = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("value").forGetter(FloatProperty::f)
    ).apply(instance, FloatProperty::new));

    public static final StreamCodec<FriendlyByteBuf, FloatProperty> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            FloatProperty::f,
            FloatProperty::new
    );

    @Override
    public DeferredHolder<ModelPropertyType<?>, ?> type() {
        return ModelPropertyRegistry.FLOAT;
    }
}
