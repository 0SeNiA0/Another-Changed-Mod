package net.zaharenko424.cmrs.property;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.cmrs.api.ModelProperty;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;

public final class UnitProperty implements ModelProperty {

    public static final UnitProperty INSTANCE = new UnitProperty();

    public static final Codec<UnitProperty> CODEC_ = Codec.unit(INSTANCE);

    public static final StreamCodec<FriendlyByteBuf, UnitProperty> CODEC = StreamCodec.unit(INSTANCE);

    private UnitProperty(){}

    @Override
    public DeferredHolder<ModelPropertyType<?>, ?> type() {
        return ModelPropertyRegistry.UNIT;
    }

    @Override
    public String toString() {
        return "UnitProperty";
    }
}
