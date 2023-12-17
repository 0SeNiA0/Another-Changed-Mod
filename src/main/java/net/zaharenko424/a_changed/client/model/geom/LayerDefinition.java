package net.zaharenko424.a_changed.client.model.geom;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
@OnlyIn(Dist.CLIENT)
public class LayerDefinition {
    private final MeshDefinition mesh;
    private final int textureWidth;
    private final int textureHeight;

    private LayerDefinition(MeshDefinition mesh,int textureWidth, int textureHeight){
        this.mesh=mesh;
        this.textureWidth=textureWidth;
        this.textureHeight=textureHeight;
    }

    public ModelPart bake(){
        return mesh.getRoot().bake(textureWidth,textureHeight);
    }

    @Contract(value = "_,_,_ -> new", pure = true)
    public static @NotNull LayerDefinition create(MeshDefinition mesh,int textureWidth, int textureHeight){
        return new LayerDefinition(mesh,textureWidth,textureHeight);
    }
}