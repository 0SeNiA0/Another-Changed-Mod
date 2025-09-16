package net.zaharenko424.cmrs.client.geom;

import net.zaharenko424.cmrs.client.model.Texture;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Only use with models that don't have baked uv(% instead of pixels).
 */
@ParametersAreNonnullByDefault
public class RemappingVertexConsumer extends SimpleVertexConsumer {

    protected int textureWidth = 1;
    protected int textureHeight = 1;

    public RemappingVertexConsumer texture(int textureWidth, int textureHeight){
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        return this;
    }

    public RemappingVertexConsumer texture(Texture texture){
        return texture(texture.getScaledWidth(), texture.getScaledHeight());
    }

    @Override
    public void addVertex(Vector3f pos, Vector3f normal, float u, float v) {
        super.addVertex(pos, normal, u / textureWidth, v / textureHeight);
    }
}