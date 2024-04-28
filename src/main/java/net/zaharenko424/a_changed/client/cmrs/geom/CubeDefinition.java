package net.zaharenko424.a_changed.client.cmrs.geom;

import org.joml.Vector3f;

public class CubeDefinition {

    private final Vector3f origin;
    private final Vector3f dimensions;
    private final Vector3f inflate;
    private final boolean mirror;
    private final CubeUV uv;

    CubeDefinition(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, boolean mirror, CubeUV uv){
        origin = new Vector3f(x1, y1, z1);
        dimensions = new Vector3f(x2, y2, z2);
        this.inflate = inflate;
        this.mirror = mirror;
        this.uv = uv;
    }

    public ModelPart.Cube bake(float textureWidth, float textureHeight){
        return new ModelPart.Cube(origin.x, origin.y, origin.z, dimensions.x, dimensions.y,dimensions.z, inflate.x, inflate.y, inflate.z, mirror, uv, textureWidth, textureHeight);
    }
}