package net.zaharenko424.a_changed.atest;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class ModelPropertyType <T> {

    private final StreamCodec<FriendlyByteBuf, T> codec;

    public ModelPropertyType(){
        codec = null;
    }

    public ModelPropertyType(StreamCodec<FriendlyByteBuf, T> codec){
        this.codec = codec;
    }

    public StreamCodec<FriendlyByteBuf, T> codec(){
        return codec;
    }
}