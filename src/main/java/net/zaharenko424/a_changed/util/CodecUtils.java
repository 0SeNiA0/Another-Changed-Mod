package net.zaharenko424.a_changed.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.StreamDecoder;
import net.minecraft.network.codec.StreamEncoder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static final StreamCodec<FriendlyByteBuf, PartPose> POSE_CODEC = StreamCodec.of((buffer, pose) -> {
        int flag = pose.xRot == 0 && pose.yRot == 0 && pose.zRot == 0 ? 1 : 3;
        flag += pose.x == 0 && pose.y == 0 && pose.z == 0 ? -1 : 0;
        buffer.writeByte(flag);

        if(flag == 1 || flag == 3) buffer.writeFloat(pose.x).writeFloat(pose.y).writeFloat(pose.z);
        if(flag == 2 || flag == 3) buffer.writeFloat(pose.xRot).writeFloat(pose.yRot).writeFloat(pose.zRot);
    }, buffer -> switch(buffer.readByte()){
        case 0 -> PartPose.ZERO;
        case 1 -> PartPose.offset(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        case 2 -> PartPose.rotation(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
        default -> PartPose.offsetAndRotation(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    });

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