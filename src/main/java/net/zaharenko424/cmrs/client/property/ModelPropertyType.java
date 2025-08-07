package net.zaharenko424.cmrs.client.property;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ModelPropertyType <T> {

    public final StreamCodec<FriendlyByteBuf, T> codec;

    public ModelPropertyType(StreamCodec<FriendlyByteBuf, T> codec){
        this.codec = codec;
    }

    public static class Overridable <T extends net.zaharenko424.cmrs.api.Overridable<O>, O> extends ModelPropertyType <T> {

        public final StreamCodec<FriendlyByteBuf, O> override;

        public Overridable(StreamCodec<FriendlyByteBuf, T> codec, StreamCodec<FriendlyByteBuf, O> override) {
            super(codec);
            this.override = override;
        }
    }
}