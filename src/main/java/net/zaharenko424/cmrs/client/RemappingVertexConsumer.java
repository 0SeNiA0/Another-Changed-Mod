package net.zaharenko424.cmrs.client;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.zaharenko424.cmrs.util.Pool;
import org.jetbrains.annotations.NotNull;

/**
 * Only use with models that don't have baked uv(% instead of pixels).
 */
public final class RemappingVertexConsumer implements VertexConsumer, Pool.Poolable {

    private VertexConsumer consumer;
    private int textureWidth;
    private int textureHeight;

    public RemappingVertexConsumer(VertexConsumer consumer, int textureWidth, int textureHeight){
        this.consumer = consumer;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    public RemappingVertexConsumer wrap(@NotNull VertexConsumer consumer){
        this.consumer = consumer;
        return this;
    }

    public RemappingVertexConsumer wrap(@NotNull VertexConsumer consumer, int textureWidth, int textureHeight){
        this.consumer = consumer;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        return this;
    }

    public RemappingVertexConsumer texture(int textureWidth, int textureHeight){
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        return this;
    }

    @Override
    public @NotNull VertexConsumer addVertex(float x, float y, float z) {
        consumer.addVertex(x, y, z);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setColor(int red, int green, int blue, int alpha) {
        consumer.setColor(red, green, blue, alpha);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv(float u, float v) {
        consumer.setUv(u / textureWidth, v / textureHeight);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv1(int u, int v) {
        consumer.setUv1(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setUv2(int u, int v) {
        consumer.setUv2(u, v);
        return this;
    }

    @Override
    public @NotNull VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
        consumer.setNormal(normalX, normalY, normalZ);
        return this;
    }

    @Override
    public void addVertex(float x, float y, float z, int color, float u, float v, int packedOverlay, int packedLight, float normalX, float normalY, float normalZ) {
        consumer.addVertex(x, y, z, color, u / textureWidth, v / textureHeight, packedOverlay, packedLight, normalX, normalY, normalZ);
    }

    @Override
    public void reset() {
        consumer = null;
        textureWidth = 0;
        textureHeight = 0;
    }
}