package net.zaharenko424.cmrs.api;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

/**
 * Allows to render with multiple renderTypes at the same time.
 */
public interface BufferSourceAccess extends MultiBufferSource {

    static BufferSourceAccess get(){
        return (BufferSourceAccess) Minecraft.getInstance().renderBuffers().bufferSource();
    }

    void cmrs$startSubBatch();

    /**
     * @param subBatchIndex starts at 0.
     */
    VertexConsumer cmrs$getBuffer(@NotNull RenderType type, int subBatchIndex);

    void cmrs$finishBatched();
}