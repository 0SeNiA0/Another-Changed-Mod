package net.zaharenko424.cmrs.property;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;

public record BooleanProperty(boolean bool) implements ModelProperty {

    public static final Codec<BooleanProperty> CODEC_ = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("value").forGetter(BooleanProperty::bool)
    ).apply(instance, BooleanProperty::new));

    public static final StreamCodec<FriendlyByteBuf, BooleanProperty> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            BooleanProperty::bool,
            BooleanProperty::new
    );

    @Override
    public DeferredHolder<ModelPropertyType<?>, ?> type() {
        return ModelPropertyRegistry.BOOLEAN;
    }
}
