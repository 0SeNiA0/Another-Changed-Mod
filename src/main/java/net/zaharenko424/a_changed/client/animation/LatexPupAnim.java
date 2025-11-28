package net.zaharenko424.a_changed.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.Targeting;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.entity.AbstractLatexPup;
import net.zaharenko424.cmrs.api.AnimationComponent;
import net.zaharenko424.cmrs.client.geom.Node;
import net.zaharenko424.cmrs.client.geom.Reusable;
import org.joml.Vector3f;

//Hardcoded dl pup animation
public class LatexPupAnim extends AnimationComponent {

    private static LatexPupAnim instance;

    public static LatexPupAnim getInstance(){
        if(instance == null) instance = new LatexPupAnim();
        return instance;
    }

    @Override
    public <E extends LivingEntity> void animate(Node root, E entity, PoseStack poseStack, float partialTick, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        Node head = root.getPart("head");
        Node body = root.getPart("body");
        Node upperBody = root.getPart("upper_body");
        Node tail = root.getPart("tail");
        Node rightLegFront = root.getPart("right_leg_front");
        Node leftLegFront = root.getPart("left_leg_front");
        Node rightLegBack = root.getPart("right_leg_back");
        Node leftLegBack = root.getPart("left_leg_back");

        if(entity.isBaby()){
            root.scale().add(Reusable.VEC3F.get().set(-.5f));
            head.scale().add(Reusable.VEC3F.get().set(1));
            limbSwing /= 2;
        }

        Vector3f tailRot = tail.rotation();
        if (entity instanceof Targeting mob && mob.getTarget() != null) {
            tailRot.y = 0.0F;
        } else {
            tailRot.y = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        Vec3 movement = entity.getDeltaMovement();
        if (entity instanceof TamableAnimal animal && animal.isInSittingPose() || (entity.isCrouching() && movement.x == 0 && movement.z == 0 && entity.fallDistance == 0)) {
            if(entity instanceof TamableAnimal) root.translation().y -= entity.isBaby() ? 1 : 2;
            head.translation().y += 1.8f;

            body.translation().add(0, -2, -2);
            body.rotation().x = -(float) (Math.PI / 4);

            upperBody.rotation().x = -(float) (Math.PI * 2.0 / 5.0);

            rightLegFront.rotation().x = -5.811947F;
            leftLegFront.rotation().x = -5.811947F;

            rightLegBack.translation().add(0, -5, -4.5f);
            rightLegBack.rotation().x = -(float) (Math.PI * 3.0 / 2.0);

            leftLegBack.translation().add(0, -5, -4.5f);
            leftLegBack.rotation().x = -(float) (Math.PI * 3.0 / 2.0);

            tail.translation().add(0, -7, -2);
            tailRot.x = -100 * Mth.DEG_TO_RAD;
        } else {
            rightLegBack.rotation().x = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
            leftLegBack.rotation().x = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            rightLegFront.rotation().x = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            leftLegFront.rotation().x = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        }

        Vector3f headRot = head.rotation();
        if(entity instanceof AbstractLatexPup pup) {
            headRot.z = pup.getHeadRollAngle(partialTick) + pup.getBodyRollAngle(partialTick, 0.0F);
            upperBody.rotation().z = pup.getBodyRollAngle(partialTick, -0.08F);
            body.rotation().z = pup.getBodyRollAngle(partialTick, -0.16F);
            tailRot.x = -pup.getTailAngle();
            tailRot.z = pup.getBodyRollAngle(partialTick, -0.2F);
        }

        headRot.x = -headPitch * (float) (Math.PI / 180.0);
        headRot.y = -netHeadYaw * (float) (Math.PI / 180.0);
    }
}