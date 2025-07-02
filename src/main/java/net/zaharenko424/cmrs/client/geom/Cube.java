package net.zaharenko424.cmrs.client.geom;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.zaharenko424.cmrs.client.geom.builder.CubeUV;
import net.zaharenko424.cmrs.client.geom.builder.UVData;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.Map;

public class Cube extends Mesh {

    public final float minX;
    public final float minY;
    public final float minZ;
    public final float maxX;
    public final float maxY;
    public final float maxZ;

    public Cube(float x, float y, float z, float sizeX, float sizeY, float sizeZ,
                float inflateX, float inflateY, float inflateZ, CubeUV uv, float textureWidth, float textureHeight, int renderId) {
        super(buildVertices(x - inflateX, y - inflateY, z - inflateZ,
                        x + sizeX + inflateX, y + sizeY + inflateY, z + sizeZ + inflateZ),
                new Quad[uv.faces().size()], renderId);
        minX = x;
        minY = y;
        minZ = z;
        maxX = x + sizeX;
        maxY = y + sizeY;
        maxZ = z + sizeZ;

        int i = 0;
        Direction direction;
        for (Map.Entry<Direction, UVData> entry : uv.faces().entrySet()) {
            direction = entry.getKey();
            quads[i++] = new Quad(
                    switch (direction) {
                        case DOWN -> quadVertices(4, 3, 7, 0);
                        case UP -> quadVertices(1, 2, 6, 5);
                        case WEST -> quadVertices(7, 3, 6, 2);
                        case NORTH -> quadVertices(0, 7, 2, 1);
                        case EAST -> quadVertices(4, 0, 1, 5);
                        case SOUTH -> quadVertices(3, 4, 5, 6);
                    }, entry.getValue(), textureWidth, textureHeight, direction
            );
        }
    }

    private static @NotNull ImmutableList<VertexData> buildVertices(float x1, float y1, float z1, float x2, float y2, float z2) {
        ImmutableList.Builder<VertexData> builder = ImmutableList.builder();
        Quad[] empty = new Quad[0];
        builder.add(new VertexData(new Vector3f(x2, y1, z1), empty));
        builder.add(new VertexData(new Vector3f(x2, y2, z1), empty));
        builder.add(new VertexData(new Vector3f(x1, y2, z1), empty));
        builder.add(new VertexData(new Vector3f(x1, y1, z2), empty));
        builder.add(new VertexData(new Vector3f(x2, y1, z2), empty));
        builder.add(new VertexData(new Vector3f(x2, y2, z2), empty));
        builder.add(new VertexData(new Vector3f(x1, y2, z2), empty));
        builder.add(new VertexData(new Vector3f(x1, y1, z1), empty));
        return builder.build();
    }

    private Vertex @NotNull [] quadVertices(int v0, int v1, int v2, int v3) {
        return new Vertex[]{new Vertex(vertexData.get(v0)), new Vertex(vertexData.get(v1)),
                new Vertex(vertexData.get(v2)), new Vertex(vertexData.get(v3))};
    }
}
