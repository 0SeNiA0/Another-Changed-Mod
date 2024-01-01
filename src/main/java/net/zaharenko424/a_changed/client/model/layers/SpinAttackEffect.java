package net.zaharenko424.a_changed.client.model.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.model.HierarchicalHumanoidModel;
import net.zaharenko424.a_changed.client.renderer.LatexEntityRenderer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class SpinAttackEffect<E extends LivingEntity,M extends HierarchicalHumanoidModel<E>> extends RenderLayer<E,M> {
    private final ModelPart box;

    public SpinAttackEffect(LatexEntityRenderer<E> renderer, EntityModelSet modelSet) {
        super((RenderLayerParent<E, M>) renderer);
        ModelPart modelpart = modelSet.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK);
        this.box = modelpart.getChild("box");
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, E p_117352_, float limbSwing, float limbSwingAmount, float ticks, float ageInTicks, float headYaw, float pitch) {
        if (p_117352_.isAutoSpinAttack()) {
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(SpinAttackEffectLayer.TEXTURE));
            for(int i = 0; i < 3; ++i) {
                poseStack.pushPose();
                float f = ageInTicks * (float)(-(45 + i * 5));
                poseStack.mulPose(Axis.YP.rotationDegrees(f));
                float f1 = 0.75F * (float)i;
                poseStack.scale(f1, f1, f1);
                poseStack.translate(0.0F, -0.2F + 0.6F * (float)i, 0.0F);
                this.box.render(poseStack, vertexconsumer, light, OverlayTexture.NO_OVERLAY);
                poseStack.popPose();
            }
        }
    }
}
