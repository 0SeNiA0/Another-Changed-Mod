package net.zaharenko424.cmrs.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.CMRS;
import net.zaharenko424.cmrs.api.BufferSourceAccess;
import net.zaharenko424.cmrs.api.MatrixStack;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class RenderUtil {

    private static final ModelPart.Quad quad = new ModelPart.Quad(new ModelPart.Vertex[]{
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(8, 8, 0), new ModelPart.Quad[0]), 1, 1),
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(-8, 8, 0), new ModelPart.Quad[0]), 0, 1),
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(-8, -8, 0), new ModelPart.Quad[0]), 0, 0),
            new ModelPart.Vertex(new ModelPart.VertexData(new Vector3f(8, -8, 0), new ModelPart.Quad[0]), 1, 0)});
    private static final Quaternionf rot = new Quaternionf();
    private static final ResourceLocation tex = CMRS.textureLoc("misc/loading");

    public static void renderLoadingAnim(@NotNull LivingEntity entity, @NotNull PoseStack stack, float partialTicks, int packedLight){
        MatrixStack.push(stack);
        stack.translate(0, 1, 0);
        stack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        int ticks = entity.tickCount;
        int degCurrent = ticks % 90 * 4;
        int degPrev = (degCurrent > 0 ? ticks -1 : 89) % 90 * 4;

        float prev = degPrev * -Mth.DEG_TO_RAD;
        float current = (degPrev == 356 ? 360 : degCurrent) * -Mth.DEG_TO_RAD;
        stack.mulPose(rot.identity().rotateZYX(Mth.lerp(partialTicks, prev, current), 0, 0));

        stack.scale(entity.getScale(), -entity.getScale(), entity.getScale());
        PoseStack.Pose pose = stack.last();
        for (ModelPart.Vertex vertex : quad.vertices) {
            vertex.data().resetTransform();
        }
        quad.resetTransform();
        quad.compile(pose.pose(), pose.normal(), BufferSourceAccess.get().getBuffer(RenderType.entityTranslucent(tex)), packedLight, OverlayTexture.NO_OVERLAY, -1);
        MatrixStack.pop(stack);
    }
}
