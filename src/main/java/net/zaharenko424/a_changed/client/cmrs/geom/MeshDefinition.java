package net.zaharenko424.a_changed.client.cmrs.geom;

public class MeshDefinition {

    private final float[] vertices;
    private final float[] quads;

    MeshDefinition(float[] vertices, float[] quads){
        this.vertices = vertices;
        this.quads = quads;
    }

    public ModelPart.Mesh bake(float textureWidth, float textureHeight){
        return new ModelPart.Mesh(vertices, quads, textureWidth, textureHeight);
    }
}