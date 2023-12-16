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
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import net.zaharenko424.a_changed.client.renderer.LatexEntityRenderer;
import org.jetbrains.annotations.NotNull;

public class SpinAttackEffect<E extends LivingEntity,M extends AbstractLatexEntityModel<E>> extends RenderLayer<E,M> {
    private final ModelPart box;

    public SpinAttackEffect(LatexEntityRenderer<E> p_117346_, @NotNull EntityModelSet modelSet) {
        super((RenderLayerParent<E, M>) p_117346_);
        ModelPart modelpart = modelSet.bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK);
        this.box = modelpart.getChild("box");
    }

    @Override
    public void render(@NotNull PoseStack p_117349_, @NotNull MultiBufferSource p_117350_, int p_117351_, @NotNull E p_117352_, float p_117353_, float p_117354_, float p_117355_, float p_117356_, float p_117357_, float p_117358_) {
        if (p_117352_.isAutoSpinAttack()) {
            VertexConsumer vertexconsumer = p_117350_.getBuffer(RenderType.entityCutoutNoCull(SpinAttackEffectLayer.TEXTURE));
            for(int i = 0; i < 3; ++i) {
                p_117349_.pushPose();
                float f = p_117356_ * (float)(-(45 + i * 5));
                p_117349_.mulPose(Axis.YP.rotationDegrees(f));
                float f1 = 0.75F * (float)i;
                p_117349_.scale(f1, f1, f1);
                p_117349_.translate(0.0F, -0.2F + 0.6F * (float)i, 0.0F);
                this.box.render(p_117349_, vertexconsumer, p_117351_, OverlayTexture.NO_OVERLAY);
                p_117349_.popPose();
            }
        }
    }
}
