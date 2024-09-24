package net.zaharenko424.a_changed.client.cmrs.geom;

import org.joml.Vector3f;

public class CubeDefinition {

    private final Vector3f origin;
    private final Vector3f size;
    private final Vector3f inflate;
    private final CubeUV uv;

    CubeDefinition(float x, float y, float z, float sizeX, float sizeY, float sizeZ, Vector3f inflate, CubeUV uv){
        origin = new Vector3f(x, y, z);
        size = new Vector3f(sizeX, sizeY, sizeZ);
        this.inflate = inflate;
        this.uv = uv;
    }

    public ModelPart.Cube bake(float textureWidth, float textureHeight){
        return new ModelPart.Cube(origin.x, origin.y, origin.z, size.x, size.y, size.z, inflate.x, inflate.y, inflate.z, uv, textureWidth, textureHeight);
    }
}