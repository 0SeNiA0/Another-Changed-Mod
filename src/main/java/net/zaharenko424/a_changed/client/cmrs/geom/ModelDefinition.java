package net.zaharenko424.a_changed.client.cmrs.geom;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModelDefinition {
    private final Builder mesh;
    private final float textureWidth;
    private final float textureHeight;

    private ModelDefinition(Builder mesh, float textureWidth, float textureHeight){
        this.mesh = mesh;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public ModelPart bake(){
        return mesh.getRoot().bake(textureWidth, textureHeight);
    }

    @Contract(value = "_,_,_ -> new", pure = true)
    public static @NotNull ModelDefinition create(Builder mesh, int textureWidth, int textureHeight){
        return new ModelDefinition(mesh,textureWidth,textureHeight);
    }

    public static @NotNull ModelDefinition create(Builder mesh, int textureWidth, int textureHeight, float uvScale){
        return new ModelDefinition(mesh,textureWidth / uvScale,textureHeight / uvScale);
    }

    public static class Builder {

        private final GroupDefinition root = new GroupDefinition();

        public GroupDefinition getRoot(){
            return root;
        }
    }
}