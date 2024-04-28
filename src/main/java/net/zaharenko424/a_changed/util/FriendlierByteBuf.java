package net.zaharenko424.a_changed.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class FriendlierByteBuf extends FriendlyByteBuf {

    public FriendlierByteBuf(ByteBuf pSource) {
        super(pSource);
    }

    public FriendlierByteBuf unreadBytes(int bytes){
        readerIndex(readerIndex() - bytes);
        return this;
    }

    public char peekChar(){
        char c = readChar();
        unreadChar();
        return c;
    }

    public FriendlierByteBuf skipChar(){
        skipBytes(2);
        return this;
    }

    public FriendlierByteBuf unreadChar(){
        return unreadBytes(2);
    }

    public float[] readFloatArray(){
        int j = readVarInt();
        float[] ar = new float[j];
        for(int i = 0; i < j; i++){
            ar[i] = readFloat();
        }
        return ar;
    }
}