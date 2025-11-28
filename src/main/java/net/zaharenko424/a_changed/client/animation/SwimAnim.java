package net.zaharenko424.a_changed.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.api.AnimationComponent;
import net.zaharenko424.cmrs.client.geom.Node;

public class SwimAnim extends AnimationComponent {

    private static SwimAnim instance;

    public static SwimAnim getInstance() {
        if(instance == null) instance = new SwimAnim();
        return instance;
    }

    @Override
    public <E extends LivingEntity> void animate(Node root, E entity, PoseStack poseStack, float partialTick, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float swimAmount = entity.getSwimAmount(partialTick);
        if(!entity.isFallFlying() && swimAmount > 0.0F) {
            float f3 = entity.isInWater() || entity.isInFluidType((fluidType, height) -> entity.canSwimInFluidType(fluidType)) ? -90.0F - entity.getXRot() : -90.0F;
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(swimAmount, 0.0F, f3)));
            if (entity.isVisuallySwimming()) {
                poseStack.translate(0.0F, -1.0F, 0.3F);
            }
        }
    }
}