package net.zaharenko424.a_changed.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class CodecUtils {

    public static final StreamCodec<FriendlyByteBuf, float[]> FLOAT_ARR = StreamCodec.of(
            (buffer , arr) -> {
                int length = arr.length;
                buffer.writeVarInt(length);
                for (float v : arr) {
                    buffer.writeFloat(v);
                }
            }, buffer -> {
                int length = buffer.readVarInt();
                float[] arr = new float[length];
                for (int i = 0; i < length; i++){
                    arr[i] = buffer.readFloat();
                }
                return arr;}
    );

    public static final StreamCodec<FriendlyByteBuf, float[][]> FLOAT_ARR2 = StreamCodec.of(
            (buffer, arr) -> {
                int length = arr.length;
                buffer.writeVarInt(length);
                for(float[] arr1 : arr){
                    FLOAT_ARR.encode(buffer, arr1);
                }
            }, buffer -> {
                int length = buffer.readVarInt();
                float[][] arr = new float[length][];
                for(int i = 0; i < length; i++){
                    arr[i] = FLOAT_ARR.decode(buffer);
                }
                return arr;}
    );
}