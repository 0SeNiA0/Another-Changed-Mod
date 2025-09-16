package net.zaharenko424.cmrs.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;

public record StringProperty(String str) implements ModelProperty {

    public static final Codec<StringProperty> CODEC_ = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("value").forGetter(StringProperty::str)
    ).apply(instance, StringProperty::new));

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
