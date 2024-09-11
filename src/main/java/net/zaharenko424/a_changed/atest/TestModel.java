package net.zaharenko424.a_changed.atest;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.model.HypnoCatModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TestModel <E extends LivingEntity> extends UniversalModel<E> implements ArmedModel {

    public TestModel(){
        super(ModelDefinitionCache.INSTANCE.bake(HypnoCatModel.bodyLayer).getChild("root"));
    }

    ProceduralAnimation<LivingEntity> walkAnim = new ProceduralAnimation<>() {
        final List<String> list = List.of("right_arm", "left_arm", "right_leg", "left_leg");
        @Override
        public List<String> requirements() {
            return list;
        }

        @Override
        public void animate(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            float f = 1.0F;
            if (pEntity.getFallFlyingTicks() > 4) {
                f = (float)pEntity.getDeltaMovement().lengthSqr();
                f /= 0.2F;
                f *= f * f;
            }

            if (f < 1.0F) {
                f = 1.0F;
            }
            getPart("right_arm").xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 2.0F * pLimbSwingAmount * 0.5F / f;
            getPart("left_arm").xRot = Mth.cos(pLimbSwing * 0.6662F) * 2.0F * pLimbSwingAmount * 0.5F / f;
            getPart("right_leg").xRot = Mth.cos(pLimbSwing * 0.6662F) * 1.4F * pLimbSwingAmount / f;
            getPart("left_leg").xRot = Mth.cos(pLimbSwing * 0.6662F + (float) Math.PI) * 1.4F * pLimbSwingAmount / f;
        }
    };

    ProceduralAnimation<LivingEntity> headAnim = new ProceduralAnimation<>() {
        final List<String> list = List.of("head");

        @Override
        public List<String> requirements() {
            return list;
        }

        @Override
        public void animate(LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
            ModelPart head = getPart("head");
            head.yRot = pNetHeadYaw * -Mth.DEG_TO_RAD;
            float swimAmount = pEntity.getSwimAmount(Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());
            if (pEntity.getFallFlyingTicks() > 4) {
                head.xRot = Mth.PI / 4;
            } else if (swimAmount > 0.0F) {
                if (pEntity.isVisuallySwimming()) {
                    head.xRot = rotlerpRad(swimAmount, head.xRot, Mth.PI / 4);
                } else {
                    head.xRot = rotlerpRad(swimAmount, head.xRot, pHeadPitch * -Mth.DEG_TO_RAD);
                }
            } else {
                head.xRot = pHeadPitch * -Mth.DEG_TO_RAD;
            }
        }
    };

    @Override
    public void setupAnim(@NotNull LivingEntity pEntity, float pLimbSwing, float pLimbSwingAmount, float pAgeInTicks, float pNetHeadYaw, float pHeadPitch) {
        root.getAllParts().forEach(ModelPart::resetPose);
        root.getAllParts().forEach(part -> {
            if(part.isArmor()) part.draw = false;
        });

        headAnim.animate(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);

        walkAnim.animate(pEntity, pLimbSwing, pLimbSwingAmount, pAgeInTicks, pNetHeadYaw, pHeadPitch);
    }

    protected float rotlerpRad(float pAngle, float pMaxAngle, float pMul) {
        float f = (pMul - pMaxAngle) % (float) (Math.PI * 2);
        if (f < (float) -Math.PI) {
            f += (float) (Math.PI * 2);
        }

        if (f >= (float) Math.PI) {
            f -= (float) (Math.PI * 2);
        }

        return pMaxAngle + pAngle * f;
    }

    @Override
    public void translateToHand(HumanoidArm pSide, PoseStack pPoseStack) {
        if(pSide == HumanoidArm.RIGHT){
            getPart("right_arm").translateAndRotate(pPoseStack);
        } else getPart("left_arm").translateAndRotate(pPoseStack);
        pPoseStack.translate(-.55, -1.251, 0);
    }
}