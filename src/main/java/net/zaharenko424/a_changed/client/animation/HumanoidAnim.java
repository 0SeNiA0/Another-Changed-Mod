package net.zaharenko424.a_changed.client.animation;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.cmrs.client.animation.AnimationUtils;
import net.zaharenko424.cmrs.api.AnimationComponent;
import net.zaharenko424.cmrs.client.geom.Node;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.joml.Vector3f;

import static net.zaharenko424.a_changed.util.Utils.quadraticArmUpdate;
import static net.zaharenko424.a_changed.util.Utils.rotlerpRad;

//Hardcoded animations. Bad but will do for now. Will be split later on.
public class HumanoidAnim extends AnimationComponent {

    private static HumanoidAnim instance;

    public static HumanoidAnim getInstance() {
        if(instance == null) instance = new HumanoidAnim();
        return instance;
    }

    @Override
    public <E extends LivingEntity> void animate(Node root, E entity, PoseStack poseStack, float partialTick, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        Node head = root.getPart("head");

        float swimAmount = entity.getSwimAmount(partialTick);

        headYaw *= -1;//fix flipped angles
        headPitch *= -1;

        boolean flag = entity.getFallFlyingTicks() > 4;
        boolean flag1 = entity.isVisuallySwimming();
        Vector3f headRot = head.rotation();
        headRot.y = headYaw * Mth.DEG_TO_RAD;
        if (flag) {
            headRot.x = (float) Math.PI / 4;
        } else if (swimAmount > 0.0F) {
            if (flag1) {
                headRot.x = rotlerpRad(swimAmount, headRot.x, (float) Math.PI / 4);
            } else {
                headRot.x = rotlerpRad(swimAmount, headRot.x, headPitch * -Mth.DEG_TO_RAD);
            }
        } else {
            headRot.x = headPitch * Mth.DEG_TO_RAD;
        }

        float f = 1.0F;
        if (flag) {
            f = (float)entity.getDeltaMovement().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }

        if (f < 1.0F) f = 1.0F;

        Node rightArm = root.getPart("right_arm");
        Node leftArm = root.getPart("left_arm");
        Node rightLeg = root.getPart("right_leg");
        Node leftLeg = root.getPart("left_leg");

        rightArm.rotation().x = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        leftArm.rotation().x = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        rightLeg.rotation().x = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        leftLeg.rotation().x = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;

        if (entity.isPassenger() && (entity.getVehicle() != null && entity.getVehicle().shouldRiderSit()))
            setupRiding(rightArm, leftArm, rightLeg, leftLeg);

        HumanoidModel.ArmPose humanoidmodel$armpose = AnimationUtils.getArmPose(entity, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose leftArmPose = AnimationUtils.getArmPose(entity, InteractionHand.OFF_HAND);
        if (humanoidmodel$armpose.isTwoHanded()) {
            leftArmPose = entity.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }

        HumanoidModel.ArmPose rightArmPose;

        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            rightArmPose = humanoidmodel$armpose;
        } else {
            rightArmPose = leftArmPose;
            leftArmPose = humanoidmodel$armpose;
        }

        setupArms(entity, head, rightArm, leftArm, rightArmPose, leftArmPose);

        Node body = root.getPart("body");
        float attackTime = entity.getAttackAnim(partialTick);

        setupAttackAnimation(entity, head, body, rightArm, leftArm, attackTime);

        if (entity.isCrouching()) setupCrouching(head, body, rightArm, leftArm, rightLeg, leftLeg);

        if (rightArmPose != HumanoidModel.ArmPose.SPYGLASS) AnimationUtils.bobNode(rightArm, ageInTicks, 1.0F);

        if (leftArmPose != HumanoidModel.ArmPose.SPYGLASS) AnimationUtils.bobNode(leftArm, ageInTicks, -1.0F);

        if (swimAmount > 0.0F) setupSwimAnimation(entity, rightArm, leftArm, rightLeg, leftLeg, limbSwing, attackTime, swimAmount);
    }

    protected <E extends LivingEntity> void setupArms(E entity, Node head, Node rightArm, Node leftArm , HumanoidModel.ArmPose rightArmPose, HumanoidModel.ArmPose leftArmPose){
        boolean flag2 = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity.isUsingItem()) {
            boolean flag3 = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (flag3 == flag2) {
                poseRightArm(entity, head, rightArm, leftArm, rightArmPose);
            } else {
                poseLeftArm(entity, head, rightArm, leftArm, leftArmPose);
            }
        } else {
            boolean flag4 = flag2 ? leftArmPose.isTwoHanded() : rightArmPose.isTwoHanded();
            if (flag2 != flag4) {
                poseLeftArm(entity, head, rightArm, leftArm, leftArmPose);
                poseRightArm(entity, head, rightArm, leftArm, rightArmPose);
            } else {
                poseRightArm(entity, head, rightArm, leftArm, rightArmPose);
                poseLeftArm(entity, head, rightArm, leftArm, leftArmPose);
            }
        }
    }

    protected void setupRiding(Node rightArm, Node leftArm, Node rightLeg, Node leftLeg){
        rightArm.rotation().x += (float) Math.PI / 5;
        leftArm.rotation().x += (float) Math.PI / 5;
        rightLeg.rotation().set(1.4137167F, (float) -Math.PI / 10, 0.07853982F);
        leftLeg.rotation().set(1.4137167F, (float) Math.PI / 10, -0.07853982F);
    }

    protected void setupCrouching(Node head, Node body, Node rightArm, Node leftArm, Node rightLeg, Node leftLeg){
        body.translation().z += 5;
        body.rotation().x -= 0.5f;

        rightArm.translation().y -= 2;
        rightArm.rotation().x -= 0.4f;

        leftArm.translation().y -= 2;
        leftArm.rotation().x -= 0.4f;

        rightLeg.translation().add(0, .2f, 4);
        leftLeg.translation().add(0, .2f, 4);
        head.translation().y -= 3;
    }

    protected <E extends LivingEntity> HumanoidArm getAttackArm(E entity) {
        HumanoidArm humanoidarm = entity.getMainArm();
        return entity.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }

    protected <E extends LivingEntity> void setupSwimAnimation(E entity, Node rightArm, Node leftArm, Node rightLeg, Node leftLeg, float limbSwing, float attackTime, float swimAmount){
        float f5 = limbSwing % 26.0F;
        HumanoidArm humanoidarm = getAttackArm(entity);
        float f1 = humanoidarm == HumanoidArm.RIGHT && attackTime > 0.0F ? 0.0F : swimAmount;
        float f2 = humanoidarm == HumanoidArm.LEFT && attackTime > 0.0F ? 0.0F : swimAmount;
        Vector3f leftArmRot = leftArm.rotation(), rightArmRot = rightArm.rotation();
        if (!entity.isUsingItem()) {
            if (f5 < 14.0F) {
                leftArmRot.set(-rotlerpRad(f2, -leftArmRot.x, 0.0F),
                        -rotlerpRad(f2, -leftArmRot.y, (float) Math.PI),
                        rotlerpRad(f2, leftArmRot.z, (float) Math.PI + 1.8707964F * quadraticArmUpdate(f5) / quadraticArmUpdate(14.0F)));

                rightArmRot.set(-Mth.lerp(f1, -rightArmRot.x, 0.0F),
                        -Mth.lerp(f1, -rightArmRot.y, (float) Math.PI),
                        Mth.lerp(f1, rightArmRot.z, (float) Math.PI - 1.8707964F * quadraticArmUpdate(f5) / quadraticArmUpdate(14.0F)));
            } else if (f5 >= 14.0F && f5 < 22.0F) {
                float f6 = (f5 - 14.0F) / 8.0F;

                leftArmRot.set(-rotlerpRad(f2, -leftArmRot.x, (float) (Math.PI / 2) * f6),
                        -rotlerpRad(f2, -leftArmRot.y, (float) Math.PI),
                        rotlerpRad(f2, leftArmRot.z, 5.012389F - 1.8707964F * f6));

                rightArmRot.set(-Mth.lerp(f1, -rightArmRot.x, (float) (Math.PI / 2) * f6),
                        -Mth.lerp(f1, -rightArmRot.y, (float) Math.PI),
                        Mth.lerp(f1, rightArmRot.z, 1.2707963F + 1.8707964F * f6));
            } else if (f5 >= 22.0F && f5 < 26.0F) {
                float f3 = (f5 - 22.0F) / 4.0F;

                leftArmRot.set(-rotlerpRad(f2, -leftArmRot.x, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3),
                        -rotlerpRad(f2, -leftArmRot.y, (float) Math.PI),
                        rotlerpRad(f2, leftArmRot.z, (float) Math.PI));

                rightArmRot.set(-Mth.lerp(f1, -rightArmRot.x, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3),
                        -Mth.lerp(f1, -rightArmRot.y, (float) Math.PI),
                        Mth.lerp(f1, rightArmRot.z, (float) Math.PI));
            }
        }

        Vector3f leftLegRot = leftLeg.rotation(), rightLegRot = rightLeg.rotation();
        leftLegRot.x = Mth.lerp(swimAmount, leftLegRot.x, 0.3F * Mth.cos(limbSwing * 0.33333334F + (float) Math.PI));
        rightLegRot.x = Mth.lerp(swimAmount, rightLegRot.x, 0.3F * Mth.cos(limbSwing * 0.33333334F));
    }

    protected <E extends LivingEntity> void setupAttackAnimation(E entity, Node head, Node body, Node rightArm, Node leftArm, float attackTime) {
        if (attackTime > 0.0F) {
            HumanoidArm humanoidarm = getAttackArm(entity);
            Node arm = humanoidarm == HumanoidArm.RIGHT ? rightArm : leftArm;
            float f = attackTime;
            body.rotation().y -= Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2)) * 0.2F;
            if (humanoidarm == HumanoidArm.LEFT) {
                body.rotation().y *= -1.0F;
            }

            float bodyYRot = body.rotation().y;
            rightArm.rotation().y = bodyYRot;
            leftArm.rotation().y = bodyYRot;
            leftArm.rotation().x = bodyYRot;
            f = 1.0F - attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(attackTime * (float) Math.PI) * (head.rotation().x + 0.7F) * 0.75F;
            arm.rotation().set(f1 * 1.2F + f2, bodyYRot * 2.0F, Mth.sin(attackTime * (float) Math.PI) * -0.4F);
        }
    }

    protected <E extends LivingEntity> void poseRightArm(E entity, Node head, Node rightArm, Node leftArm, HumanoidModel.ArmPose pose) {
        Vector3f rightArmRot = rightArm.rotation(), leftArmRot = leftArm.rotation(), headRot = head.rotation();
        if(TransfurManager.isHoldingEntity(entity) && TransfurManager.getGrabMode(entity) != GrabMode.FRIENDLY){
            rightArmRot.x = Mth.PI / 2f + headRot.x;
            rightArmRot.y = 0.1F + headRot.y;
            return;
        }

        switch(pose) {
            case EMPTY -> rightArmRot.y = 0.0F;
            case BLOCK -> {
                rightArmRot.x = rightArmRot.x * -0.5F + 0.9424779F;
                rightArmRot.y = (float) Math.PI / 6;
            }
            case ITEM -> {
                rightArmRot.x = rightArmRot.x * 0.5F + (float) (Math.PI / 10);
                rightArmRot.y = 0.0F;
            }
            case THROW_SPEAR -> {
                rightArmRot.x = rightArmRot.x * -0.5F + (float) Math.PI;
                rightArmRot.y = 0.0F;
            }
            case BOW_AND_ARROW -> {
                rightArmRot.y = 0.1F + headRot.y;
                leftArmRot.y = -0.5F + headRot.y;
                rightArmRot.x = (float) Math.PI / 2 + headRot.x;
                leftArmRot.x = (float) Math.PI / 2 + headRot.x;
            }
            case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(rightArm, leftArm, entity, true);
            case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(rightArm, leftArm, head, true);
            case BRUSH -> {
                rightArmRot.x = rightArmRot.x * -0.5F + (float) (Math.PI / 5);
                rightArmRot.y = 0.0F;
            }
            case SPYGLASS -> {
                rightArmRot.x = Mth.clamp(headRot.x + 1.9198622F - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                rightArmRot.y = headRot.y + (float) (Math.PI / 12);
            }
            case TOOT_HORN -> {
                rightArmRot.x = (Mth.clamp(headRot.x, -1.2F, 1.2F) + 1.4835298F);
                rightArmRot.y = headRot.y + (float) (Math.PI / 6);
            }
        }
    }

    protected <E extends LivingEntity> void poseLeftArm(E entity, Node head, Node rightArm, Node leftArm, HumanoidModel.ArmPose pose) {
        Vector3f rightArmRot = rightArm.rotation(), leftArmRot = leftArm.rotation(), headRot = head.rotation();
        if(TransfurManager.isHoldingEntity(entity) && TransfurManager.getGrabMode(entity) != GrabMode.FRIENDLY){
            leftArmRot.x = Mth.PI / 2f + headRot.x;
            leftArmRot.y = -0.1F + headRot.y;
            return;
        }

        switch(pose) {
            case EMPTY -> leftArmRot.y = 0.0F;
            case BLOCK -> {
                leftArmRot.x = leftArmRot.x * -0.5F + 0.9424779F;
                leftArmRot.y = (float) -Math.PI / 6;
            }
            case ITEM -> {
                leftArmRot.x = leftArmRot.x * 0.5F + (float) (Math.PI / 10);
                leftArmRot.y = 0.0F;
            }
            case THROW_SPEAR -> {
                leftArmRot.x = leftArmRot.x * -0.5F + (float) Math.PI;
                leftArmRot.y = 0.0F;
            }
            case BOW_AND_ARROW -> {
                rightArmRot.y = 0.5F + headRot.y;
                leftArmRot.y = -0.1F + headRot.y;
                rightArmRot.x = (float) Math.PI / 2 + headRot.x;
                leftArmRot.x = (float) Math.PI / 2 + headRot.x;
            }
            case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(rightArm, leftArm, entity, false);
            case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(rightArm, leftArm, head, false);
            case BRUSH -> {
                leftArmRot.x = leftArmRot.x * -0.5F + (float) (Math.PI / 5);
                leftArmRot.y = 0.0F;
            }
            case SPYGLASS -> {
                leftArmRot.x = Mth.clamp(headRot.x + 1.9198622F - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                leftArmRot.y = headRot.y - (float) (Math.PI / 12);
            }
            case TOOT_HORN -> {
                leftArmRot.x = -(Mth.clamp(headRot.x, -1.2F, 1.2F) - 1.4835298F);
                leftArmRot.y = headRot.y - (float) (Math.PI / 6);
            }
        }
    }
}