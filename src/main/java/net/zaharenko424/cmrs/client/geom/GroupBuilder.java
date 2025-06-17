package net.zaharenko424.cmrs.client.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class GroupBuilder {

    private final List<CubeDefinition> cubes = new ArrayList<>();
    private final List<MeshDefinition> meshes = new ArrayList<>();
    boolean armor = false;
    boolean glowing = false;
    final int defRenderId;

    public GroupBuilder(int defRenderId){
        this.defRenderId = defRenderId;
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, CubeUV uv){
        return addBox(x1, y1, z1, x2, y2, z2, new Vector3f(), uv, defRenderId);
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, CubeUV uv, int renderId){
        return addBox(x1, y1, z1, x2, y2, z2, new Vector3f(), uv, renderId);
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, CubeUV uv){
        return addBox(x1, y1, z1, x2, y2, z2, inflate, uv, defRenderId);
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, CubeUV uv, int renderId){
        cubes.add(new CubeDefinition(x1, y1, z1, x2, y2, z2, inflate, uv, renderId));
        return this;
    }

    public GroupBuilder addMesh(float[] vertices, float[] quads){
        return addMesh(vertices, quads, defRenderId, false);
    }

    public GroupBuilder addMesh(float[] vertices, float[] quads, int renderId){
        return addMesh(vertices, quads, renderId, false);
    }

    public GroupBuilder addMesh(float[] vertices, float[] quads, boolean smooth){
        return addMesh(vertices, quads, defRenderId, smooth);
    }

    public GroupBuilder addMesh(float[] vertices, float[] quads, int renderId, boolean smooth){
        verifyMesh(vertices, quads);
        meshes.add(new MeshDefinition(vertices, quads, renderId, smooth));
        return this;
    }

    public GroupBuilder addAnimatedMesh(float[] vertices, float[] quads, String[] groups, float[][] vertexFactors){
        return addAnimatedMesh(vertices, quads, groups, vertexFactors, defRenderId, false);
    }

    public GroupBuilder addAnimatedMesh(float[] vertices, float[] quads, String[] groups, float[][] vertexFactors, int renderId){
        return addAnimatedMesh(vertices, quads, groups, vertexFactors, renderId, false);
    }

    public GroupBuilder addAnimatedMesh(float[] vertices, float[] quads, String[] groups, float[][] vertexFactors, boolean smooth){
        return addAnimatedMesh(vertices, quads, groups, vertexFactors, defRenderId, smooth);
    }

    public GroupBuilder addAnimatedMesh(float[] vertices, float[] quads, String[] groups, float[][] vertexFactors, int renderId, boolean smooth){
        verifyMesh(vertices, quads);
        if(groups.length != vertexFactors.length) throw new IllegalArgumentException();
        for(float[] ar : vertexFactors){
            if(ar.length % 2 != 0) throw new IllegalArgumentException("Vertex factors must be in format: vertex index, factor");
        }
        meshes.add(new MeshDefinition(vertices, quads, groups, vertexFactors, renderId, smooth));
        return this;
    }

    private void verifyMesh(float[] vertices, float[] quads){
        if(vertices.length % 3 != 0)
            throw new IllegalArgumentException("Vertices in mesh are defined incorrectly!");

        if(quads.length % 12 != 0)
            throw new IllegalArgumentException("Quads in mesh are defined incorrectly!");
    }

    public GroupBuilder armor(){
        armor = true;
        return this;
    }

    public GroupBuilder glowing(){
        glowing = true;
        return this;
    }

    public List<CubeDefinition> cubes(){
        return cubes;
    }

    public List<MeshDefinition> meshes(){
        return meshes;
    }

    @Contract(" -> new")
    public static @NotNull GroupBuilder create(){
        return new GroupBuilder(0);
    }

    public static @NotNull GroupBuilder create(int defRenderId){
        return new GroupBuilder(defRenderId);
    }
}