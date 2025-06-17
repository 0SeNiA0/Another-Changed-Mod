package net.zaharenko424.cmrs.client.property;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.SpinAttackEffectLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.api.CustomModel;
import net.zaharenko424.cmrs.api.MatrixStack;
import net.zaharenko424.cmrs.api.RenderLayerLike;
import net.zaharenko424.cmrs.client.model.PoseTransform;
import net.zaharenko424.cmrs.util.StreamCodecUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class TridentSpinEffect implements RenderLayerLike {

    public static final StreamCodec<FriendlyByteBuf, TridentSpinEffect> CODEC = StreamCodec.of((buffer, value) -> {
            StreamCodecUtils.writeOptionally(value.transform, !value.transform.isEmpty(), buffer, PoseTransform.CODEC);
    }, buffer ->
            new TridentSpinEffect(StreamCodecUtils.readOptionally(buffer, PoseTransform.CODEC))
    );

    private final ModelPart box;
    private final PoseTransform transform;

    public TridentSpinEffect(){
        this(null);
    }

    public TridentSpinEffect(@Nullable PoseTransform transform){
        box = Minecraft.getInstance().getEntityModels().bakeLayer(ModelLayers.PLAYER_SPIN_ATTACK).getChild("box");
        this.transform = transform == null ? new PoseTransform() : transform;
    }

    @Override
    public <E extends LivingEntity> void render(@NotNull E livingEntity, @NotNull CustomModel<E> model, @NotNull PoseStack matrixStack, @NotNull MultiBufferSource buffer, int packedLight, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.isAutoSpinAttack()) return;
        MatrixStack.push(matrixStack);
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.501, 0);
        transform.apply(matrixStack);
        VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(SpinAttackEffectLayer.TEXTURE));

        for (int i = 0; i < 3; i++) {
            MatrixStack.push(matrixStack);
            float f = ageInTicks * (float)(-(45 + i * 5));
            matrixStack.mulPose(Axis.YP.rotationDegrees(f));
            float f1 = 0.75F * (float)i;
            matrixStack.scale(f1, f1, f1);
            matrixStack.translate(0.0F, -0.2F + 0.6F * (float)i, 0.0F);
            this.box.render(matrixStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
            MatrixStack.pop(matrixStack);
        }
        MatrixStack.pop(matrixStack);
    }
}