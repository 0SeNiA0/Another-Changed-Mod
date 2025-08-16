package net.zaharenko424.cmrs.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamEncoder;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StreamCodecUtils {

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

    public static final StreamCodec<FriendlyByteBuf, ResourceLocation> RESOURCE_LOC = ResourceLocation.STREAM_CODEC.cast();

    public static <T, B extends ByteBuf> void writeOptionally(T value, boolean write, @NotNull B buffer, @NotNull StreamEncoder<B, T> writer){
        buffer.writeBoolean(write);
        if(write) writer.encode(buffer, value);
    }

    public static <T, B extends ByteBuf> @Nullable T readOptionally(@NotNull B buffer, @NotNull StreamDecoder<B, T> reader){
        if(buffer.readBoolean()){
            return reader.decode(buffer);
        }
        return null;
    }
}