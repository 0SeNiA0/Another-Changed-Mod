package net.zaharenko424.a_changed.client.cmrs.geom;

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
    private final boolean mirror = false;
    boolean armor = false;
    boolean glowing = false;

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, CubeUV uv){
        return addBox(x1, y1, z1, x2, y2, z2, new Vector3f(), mirror, uv);
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, CubeUV uv){
        return addBox(x1, y1, z1, x2, y2, z2, inflate, mirror, uv);
    }

    public GroupBuilder addBox(float x1, float y1, float z1, float x2, float y2, float z2, Vector3f inflate, boolean mirror, CubeUV uv){
        cubes.add(new CubeDefinition(x1, y1, z1, x2, y2, z2, inflate, mirror, uv));
        return this;
    }

    public GroupBuilder addMesh(float[] vertices, float[] quads){
        return addMesh(vertices, quads, false);
    }

    public GroupBuilder addMesh(float[] vertices, float[] quads, boolean smooth){
        if(vertices.length % 3 != 0){
            throw new IllegalArgumentException("Vertices in mesh are defined incorrectly!");
        }
        if(quads.length % 12 != 0){
            throw new IllegalArgumentException("Quads in mesh are defined incorrectly!");
        }
        meshes.add(new MeshDefinition(vertices, quads, smooth));
        return this;
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
        return new GroupBuilder();
    }
}