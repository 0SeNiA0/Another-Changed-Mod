package net.zaharenko424.cmrs.client.geom;

import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import org.joml.Vector3f;

public class CubeDefinition {

    public static final StreamCodec<FriendlyByteBuf, CubeDefinition> CODEC = StreamCodec.composite(
            ByteBufCodecs.VECTOR3F,
            cube -> cube.origin,
            ByteBufCodecs.VECTOR3F,
            cube -> cube.size,
            ByteBufCodecs.VECTOR3F,
            cube -> cube.inflate,
            ByteBufCodecs.map(Object2ObjectArrayMap::new, NeoForgeStreamCodecs.enumCodec(Direction.class), StreamCodec.<FriendlyByteBuf, UVData>of(
                    (buffer, uv) -> buffer.writeFloat(uv.u1()).writeFloat(uv.v1()).writeFloat(uv.u2()).writeFloat(uv.v2()),
                    buffer -> new UVData(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat()))).map(CubeUV::new, uv -> uv.uv),
            cube -> cube.uv,
            ByteBufCodecs.VAR_INT,
            cube -> cube.renderId,
            CubeDefinition::new
    );

    private final Vector3f origin;
    private final Vector3f size;
    private final Vector3f inflate;
    private final CubeUV uv;
    private final int renderId;

    CubeDefinition(Vector3f origin, Vector3f size, Vector3f inflate, CubeUV uv, int renderId){
        this.origin = origin;
        this.size = size;
        this.inflate = inflate;
        this.uv = uv;
        this.renderId = renderId;
    }

    CubeDefinition(float x, float y, float z, float sizeX, float sizeY, float sizeZ, Vector3f inflate, CubeUV uv, int renderId){
        origin = new Vector3f(x, y, z);
        size = new Vector3f(sizeX, sizeY, sizeZ);
        this.inflate = inflate;
        this.uv = uv;
        this.renderId = renderId;
    }

    public ModelPart.Cube bake(float textureWidth, float textureHeight){
        return new ModelPart.Cube(origin.x, origin.y, origin.z, size.x, size.y, size.z, inflate.x, inflate.y, inflate.z, uv, textureWidth, textureHeight, renderId);
    }
}