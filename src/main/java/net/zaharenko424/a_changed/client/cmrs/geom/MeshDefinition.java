package net.zaharenko424.a_changed.client.cmrs.geom;

public class MeshDefinition {

    private final float[] vertices;
    private final float[] quads;
    private final boolean smooth;

    MeshDefinition(float[] vertices, float[] quads, boolean smooth){
        this.vertices = vertices;
        this.quads = quads;
        this.smooth = smooth;
    }

    public ModelPart.Mesh bake(float textureWidth, float textureHeight){
        return smooth ? new ModelPart.SmoothMesh(vertices, quads, textureWidth, textureHeight)
                : new ModelPart.Mesh(vertices, quads, textureWidth, textureHeight);
    }
}