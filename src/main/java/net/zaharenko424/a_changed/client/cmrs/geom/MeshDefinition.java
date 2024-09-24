package net.zaharenko424.a_changed.client.cmrs.geom;

public class MeshDefinition {

    private final float[] vertices;
    private final float[] quads;
    private final boolean smooth;
    final String[] groups;
    final float[][] vertexInfluence;

    MeshDefinition(float[] vertices, float[] quads, boolean smooth){
        this.vertices = vertices;
        this.quads = quads;
        this.smooth = smooth;
        groups = null;
        vertexInfluence = null;
    }

    MeshDefinition(float[] vertices, float[] quads, String[] groups, float[][] vertexInfluence, boolean smooth){
        this.vertices = vertices;
        this.quads = quads;
        this.smooth = smooth;
        this.groups = groups;
        this.vertexInfluence = vertexInfluence;
    }
    public ModelPart.Mesh bake(float textureWidth, float textureHeight){
        return smooth ? new ModelPart.SmoothMesh(vertices, quads, textureWidth, textureHeight)
                : new ModelPart.Mesh(vertices, quads, textureWidth, textureHeight);
    }
}