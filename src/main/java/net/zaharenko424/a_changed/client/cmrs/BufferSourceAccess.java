package net.zaharenko424.a_changed.client.cmrs;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

/**
 * Allows to render with multiple renderTypes at the same time.
 */
public interface BufferSourceAccess extends MultiBufferSource {

    VertexConsumer a_changed$getPooledBuffer(RenderType renderType);

    void a_changed$endPooledBatch();

    static BufferSourceAccess get(){
        return (BufferSourceAccess) Minecraft.getInstance().renderBuffers().bufferSource();
    }
}