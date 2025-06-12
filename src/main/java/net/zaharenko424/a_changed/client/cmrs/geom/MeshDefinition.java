package net.zaharenko424.a_changed.client.cmrs.geom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.a_changed.client.cmrs.util.StreamCodecUtils;

public class MeshDefinition {

    public static final StreamCodec<FriendlyByteBuf, MeshDefinition> CODEC = StreamCodec.of(
            (buffer, mesh) -> {
                StreamCodecUtils.FLOAT_ARR.encode(buffer, mesh.vertices);
                StreamCodecUtils.FLOAT_ARR.encode(buffer, mesh.quads);
                buffer.writeVarInt(mesh.renderId);
                buffer.writeBoolean(mesh.smooth);

                if(mesh.groups == null){
                    buffer.writeByte(0);
                    return;
                }
                buffer.writeByte(1);

                buffer.writeArray(mesh.groups, ByteBufCodecs.STRING_UTF8);
                StreamCodecUtils.FLOAT_ARR2.encode(buffer, mesh.vertexInfluence);
            }, buffer -> {
                float[] arr0 = StreamCodecUtils.FLOAT_ARR.decode(buffer);
                float[] arr1 = StreamCodecUtils.FLOAT_ARR.decode(buffer);
                boolean smooth = buffer.readBoolean();

                if(buffer.readByte() == 0) return new MeshDefinition(arr0, arr1, buffer.readVarInt(), smooth);
                String[] arr2 = buffer.readArray(String[]::new, ByteBufCodecs.STRING_UTF8);
                return new MeshDefinition(arr0, arr1, arr2, StreamCodecUtils.FLOAT_ARR2.decode(buffer), buffer.readVarInt(), smooth);}
    );

    private final float[] vertices;
    private final float[] quads;
    private final int renderId;
    private final boolean smooth;
    final String[] groups;
    final float[][] vertexInfluence;

    MeshDefinition(float[] vertices, float[] quads, int renderId, boolean smooth){
        this.vertices = vertices;
        this.quads = quads;
        this.renderId = renderId;
        this.smooth = smooth;
        groups = null;
        vertexInfluence = null;
    }

    MeshDefinition(float[] vertices, float[] quads, String[] groups, float[][] vertexInfluence, int renderId, boolean smooth){
        this.vertices = vertices;
        this.quads = quads;
        this.renderId = renderId;
        this.smooth = smooth;
        this.groups = groups;
        this.vertexInfluence = vertexInfluence;
    }

    public ModelPart.Mesh bake(float textureWidth, float textureHeight){
        return smooth ? new ModelPart.SmoothMesh(vertices, quads, textureWidth, textureHeight, renderId)
                : new ModelPart.Mesh(vertices, quads, textureWidth, textureHeight, renderId);
    }
}