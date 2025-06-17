package net.zaharenko424.a_changed.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.cmrs.api.AnimationComponent;
import net.zaharenko424.cmrs.client.geom.ModelPart;
import net.zaharenko424.cmrs.client.geom.Reusable;
import net.zaharenko424.a_changed.entity.DarkLatexPup;

//Hardcoded dl pup animation
public class DLPupAnim extends AnimationComponent {

    @Override
    public <E extends LivingEntity> void animate(ModelPart root, E entity, PoseStack poseStack, float partialTick, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        ModelPart head = root.getPart("head");
        ModelPart body = root.getPart("body");
        ModelPart upperBody = root.getPart("upper_body");
        ModelPart tail = root.getPart("tail");
        ModelPart rightLegFront = root.getPart("right_leg_front");
        ModelPart leftLegFront = root.getPart("left_leg_front");
        ModelPart rightLegBack = root.getPart("right_leg_back");
        ModelPart leftLegBack = root.getPart("left_leg_back");

        if(entity.isBaby()){
            root.offsetScale(Reusable.VEC3F.get().set(-.5f));
            head.offsetScale(Reusable.VEC3F.get().set(1));
            limbSwing /= 2;
        }

        if (entity instanceof PathfinderMob mob && mob.getTarget() != null) {
            tail.yRot = 0.0F;
        } else {
            tail.yRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        Vec3 movement = entity.getDeltaMovement();
        if (entity instanceof TamableAnimal animal && animal.isInSittingPose() || (entity.isCrouching() && movement.x == 0 && movement.z == 0 && entity.fallDistance == 0)) {
            if(entity instanceof TamableAnimal) root.y -= entity.isBaby() ? 1 : 2;
            head.y += 1.8f;

            body.y -= 2;
            body.z -= 2;
            body.xRot = -(float) (Math.PI / 4);

            upperBody.xRot = -(float) (Math.PI * 2.0 / 5.0);

            rightLegFront.xRot = -5.811947F;
            leftLegFront.xRot = -5.811947F;

            rightLegBack.y -= 5;
            rightLegBack.z -= 4.5f;
            rightLegBack.xRot = -(float) (Math.PI * 3.0 / 2.0);
            leftLegBack.y -= 5;
            leftLegBack.z -= 4.5f;
            leftLegBack.xRot = -(float) (Math.PI * 3.0 / 2.0);

            tail.y -= 7;
            tail.z -= 2;
            tail.xRot = -100 * Mth.DEG_TO_RAD;
        } else {
            rightLegBack.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            leftLegBack.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            rightLegFront.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            leftLegFront.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        if(entity instanceof DarkLatexPup pup) {
            head.zRot = pup.getHeadRollAngle(partialTick) + pup.getBodyRollAngle(partialTick, 0.0F);
            upperBody.zRot = pup.getBodyRollAngle(partialTick, -0.08F);
            body.zRot = pup.getBodyRollAngle(partialTick, -0.16F);
            tail.xRot = -pup.getTailAngle();
            tail.zRot = pup.getBodyRollAngle(partialTick, -0.2F);
        }

        head.xRot = -headPitch * (float) (Math.PI / 180.0);
        head.yRot = -netHeadYaw * (float) (Math.PI / 180.0);


        if(!entity.isFallFlying() || !(entity instanceof AbstractClientPlayer player)) return;

        if (!player.isAutoSpinAttack()) {
            float f2 = (float) player.getFallFlyingTicks() + ageInTicks;
            float f3 = Mth.clamp(f2 * f2 / 100.0F, 0.0F, 1.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(f3 * -entity.getViewXRot(partialTick)));
        }

        Vec3 vec3 = player.getViewVector(partialTick);
        Vec3 vec31 = player.getDeltaMovementLerped(partialTick);
        double d0 = vec31.horizontalDistanceSqr();
        double d1 = vec3.horizontalDistanceSqr();
        if (d0 > 0.0 && d1 > 0.0) {
            double d2 = (vec31.x * vec3.x + vec31.z * vec3.z) / Math.sqrt(d0 * d1);
            double d3 = vec31.x * vec3.z - vec31.z * vec3.x;
            poseStack.mulPose(Axis.YP.rotation((float)(Math.signum(d3) * Math.acos(d2))));
        }
    }
}