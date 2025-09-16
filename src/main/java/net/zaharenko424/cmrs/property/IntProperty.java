package net.zaharenko424.cmrs.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;

public record IntProperty(int value) implements ModelProperty {

    public static final Codec<IntProperty> CODEC_ = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("value").forGetter(IntProperty::value)
    ).apply(instance, IntProperty::new));

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
