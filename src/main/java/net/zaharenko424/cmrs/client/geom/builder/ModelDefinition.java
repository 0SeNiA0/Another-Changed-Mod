package net.zaharenko424.cmrs.client.geom.builder;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ModelDefinition {

    public static final StreamCodec<FriendlyByteBuf, ModelDefinition> CODEC = StreamCodec.composite(
            GroupDefinition.CODEC,
            definition -> definition.root,
            ByteBufCodecs.FLOAT,
            definition -> definition.textureWidth,
            ByteBufCodecs.FLOAT,
            definition -> definition.textureHeight,
            ByteBufCodecs.FLOAT,
            definition -> definition.textureScale,
            ModelDefinition::new
    );

    private final GroupDefinition root;
    private final float textureWidth;
    private final float textureHeight;
    private final float textureScale;

    private ModelDefinition(GroupDefinition root, float textureWidth, float textureHeight, float textureScale){
        this.root = root;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureScale = textureScale;
    }

    public ModelPart bake(){
        return root.bake(textureWidth / textureScale, textureHeight / textureScale);
    }

    @Contract(value = "_,_,_ -> new", pure = true)
    public static @NotNull ModelDefinition create(Builder mesh, int textureWidth, int textureHeight){
        return new ModelDefinition(mesh.root, textureWidth, textureHeight, 1);
    }

    public static @NotNull ModelDefinition create(Builder mesh, int textureWidth, int textureHeight, float uvScale){
        return new ModelDefinition(mesh.root, textureWidth, textureHeight, uvScale);
    }

    public static class Builder {

        private final GroupDefinition root = new GroupDefinition();

        public GroupDefinition getRoot(){
            return root;
        }
    }
}