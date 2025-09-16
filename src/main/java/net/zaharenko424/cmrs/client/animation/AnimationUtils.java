package net.zaharenko424.cmrs.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.zaharenko424.cmrs.client.geom.ModelPart;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface AnimationUtils {

    static void animateCrossbowHold(ModelPart rightArm, ModelPart leftArm, ModelPart head, boolean right) {
        ModelPart modelpart = right ? rightArm : leftArm;
        ModelPart modelpart1 = right ? leftArm : rightArm;
        modelpart.yRot = (right ? 0.3F : -0.3F) + head.yRot;
        modelpart1.yRot = (right ? -0.6F : 0.6F) + head.yRot;
        modelpart.xRot = (float) (-Math.PI / 2) + head.xRot + 0.1F;
        modelpart1.xRot = -1.5F + head.xRot;
    }

    static void animateCrossbowCharge(ModelPart rightArm, ModelPart leftArm, LivingEntity livingEntity, boolean right) {
        ModelPart modelpart = right ? rightArm : leftArm;
        ModelPart modelpart1 = right ? leftArm : rightArm;
        modelpart.yRot = right ? 0.8F : -0.8F;
        modelpart.xRot = 0.97079635F;
        modelpart1.xRot = modelpart.xRot;
        float f = (float) CrossbowItem.getChargeDuration(livingEntity.getUseItem(), livingEntity);
        float f1 = Mth.clamp((float)livingEntity.getTicksUsingItem(), 0.0F, f);
        float f2 = f1 / f;
        modelpart1.yRot = Mth.lerp(f2, 0.4F, 0.85F) * (float)(right ? -1 : 1);
        modelpart1.xRot = Mth.lerp(f2, modelpart1.xRot, (float) Math.PI / 2);
    }

    static void bobModelPart(ModelPart part, float ageInTicks, float p_170344_) {
        part.zRot += p_170344_ * (Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F);
        part.xRot += p_170344_ * Mth.sin(ageInTicks * 0.067F) * 0.05F;
    }

    static HumanoidModel.ArmPose getArmPose(LivingEntity entity, InteractionHand hand) {
        ItemStack itemstack = entity.getItemInHand(hand);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (entity.getUsedItemHand() == hand && entity.getUseItemRemainingTicks() > 0) {
                UseAnim useanim = itemstack.getUseAnimation();
                if (useanim == UseAnim.BLOCK) {
                    return HumanoidModel.ArmPose.BLOCK;
                }

                if (useanim == UseAnim.BOW) {
                    return HumanoidModel.ArmPose.BOW_AND_ARROW;
                }

                if (useanim == UseAnim.SPEAR) {
                    return HumanoidModel.ArmPose.THROW_SPEAR;
                }

                if (useanim == UseAnim.CROSSBOW && hand == entity.getUsedItemHand()) {
                    return HumanoidModel.ArmPose.CROSSBOW_CHARGE;
                }

                if (useanim == UseAnim.SPYGLASS) {
                    return HumanoidModel.ArmPose.SPYGLASS;
                }

                if (useanim == UseAnim.TOOT_HORN) {
                    return HumanoidModel.ArmPose.TOOT_HORN;
                }

                if (useanim == UseAnim.BRUSH) {
                    return HumanoidModel.ArmPose.BRUSH;
                } else if (!entity.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                    return HumanoidModel.ArmPose.CROSSBOW_HOLD;
                }
            }

            HumanoidModel.ArmPose forgeArmPose = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(itemstack).getArmPose(entity, hand, itemstack);
            if (forgeArmPose != null) return forgeArmPose;

            return HumanoidModel.ArmPose.ITEM;
        }
    }
}