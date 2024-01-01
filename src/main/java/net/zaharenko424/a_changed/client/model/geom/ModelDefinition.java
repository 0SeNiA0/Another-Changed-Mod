package net.zaharenko424.a_changed.client.model.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModelDefinition {
    private final MeshDefinition mesh;
    private final int textureWidth;
    private final int textureHeight;

    private ModelDefinition(MeshDefinition mesh, int textureWidth, int textureHeight){
        this.mesh=mesh;
        this.textureWidth=textureWidth;
        this.textureHeight=textureHeight;
    }

    public ModelPart bake(){
        return mesh.getRoot().bake(textureWidth,textureHeight);
    }

    @Contract(value = "_,_,_ -> new", pure = true)
    public static @NotNull ModelDefinition create(MeshDefinition mesh, int textureWidth, int textureHeight){
        return new ModelDefinition(mesh,textureWidth,textureHeight);
    }

    public static @NotNull ModelDefinition create(MeshDefinition mesh, int textureWidth, int textureHeight, int uvScale){
        return new ModelDefinition(mesh,textureWidth/uvScale,textureHeight/uvScale);
    }
}