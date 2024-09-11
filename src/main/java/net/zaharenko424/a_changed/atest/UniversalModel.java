package net.zaharenko424.a_changed.atest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.world.entity.Entity;
import net.zaharenko424.a_changed.client.cmrs.animation.KeyframeAnimator;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;

import java.util.HashMap;

public class UniversalModel <E extends Entity> extends EntityModel<E> {

    protected final ModelPart root;
    protected final HashMap<String, ModelPart> searchCache = new HashMap<>();

    public UniversalModel(ModelPart root){
        this.root = root;
    }

    public ModelPart getRoot(){
        return root;
    }

    public ModelPart getPart(String name){
        return searchCache.computeIfAbsent(name, name1 ->
                KeyframeAnimator.getAnyDescendantWithName(root, name1).orElse(null));
    }

    @Override
    public void setupAnim(E pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {}

    @Override
    public void renderToBuffer(PoseStack pPoseStack, VertexConsumer pBuffer, int pPackedLight, int pPackedOverlay, int color) {
        pPoseStack.pushPose();
        pPoseStack.scale(-1, -1, 1);    //Fix vanilla stupidity with flipping model upside down in LivingEntityRenderer
        pPoseStack.translate(0, -1.501, 0);
        root.render(pPoseStack, pBuffer, pPackedLight, pPackedOverlay, color);
        pPoseStack.popPose();
    }
}