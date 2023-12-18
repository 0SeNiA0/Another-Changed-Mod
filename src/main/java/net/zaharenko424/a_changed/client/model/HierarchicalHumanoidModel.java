package net.zaharenko424.a_changed.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.client.test.geom.ArmorModel;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public abstract class HierarchicalHumanoidModel<T extends LivingEntity> extends AgeableHierarchicalModel<T> implements ArmedModel, HeadedModel {

    private final ModelPart root;
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
    public boolean crouching;
    public float swimAmount;

    public HierarchicalHumanoidModel(@NotNull ModelPart root, float scaleFactor, float yOffset) {
        super(scaleFactor, yOffset);
        this.root=root.getChild("root");
        this.head = this.root.getChild("head");
        this.body = this.root.getChild("body");
        this.rightArm = this.root.getChild("right_arm");
        this.leftArm = this.root.getChild("left_arm");
        this.rightLeg = this.root.getChild("right_leg");
        this.leftLeg = this.root.getChild("left_leg");
    }

    public void prepareMobModel(@NotNull T p_102861_, float p_102862_, float p_102863_, float p_102864_) {
        this.swimAmount = p_102861_.getSwimAmount(p_102864_);
        super.prepareMobModel(p_102861_, p_102862_, p_102863_, p_102864_);
    }

    public void setupAnim(@NotNull T p_102866_, float p_102867_, float p_102868_, float p_102869_, float p_102870_, float p_102871_) {
        root.getAllParts().forEach(ModelPart::resetPose);
        boolean flag = p_102866_.getFallFlyingTicks() > 4;
        boolean flag1 = p_102866_.isVisuallySwimming();
        this.head.yRot = p_102870_ * (float) (Math.PI / 180.0);
        if (flag) {
            this.head.xRot = (float) (-Math.PI / 4);
        } else if (this.swimAmount > 0.0F) {
            if (flag1) {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, (float) (-Math.PI / 4));
            } else {
                this.head.xRot = this.rotlerpRad(this.swimAmount, this.head.xRot, p_102871_ * (float) (Math.PI / 180.0));
            }
        } else {
            this.head.xRot = p_102871_ * (float) (Math.PI / 180.0);
        }

        float f = 1.0F;
        if (flag) {
            f = (float)p_102866_.getDeltaMovement().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }

        if (f < 1.0F) {
            f = 1.0F;
        }

        this.rightArm.xRot = Mth.cos(p_102867_ * 0.6662F + (float) Math.PI) * 2.0F * p_102868_ * 0.5F / f;
        this.leftArm.xRot = Mth.cos(p_102867_ * 0.6662F) * 2.0F * p_102868_ * 0.5F / f;
        this.rightLeg.xRot = Mth.cos(p_102867_ * 0.6662F) * 1.4F * p_102868_ / f;
        this.leftLeg.xRot = Mth.cos(p_102867_ * 0.6662F + (float) Math.PI) * 1.4F * p_102868_ / f;
        if (this.riding) {
            this.rightArm.xRot += (float) (-Math.PI / 5);
            this.leftArm.xRot += (float) (-Math.PI / 5);
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = (float) (Math.PI / 10);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (float) (-Math.PI / 10);
            this.leftLeg.zRot = -0.07853982F;
        }

        boolean flag2 = p_102866_.getMainArm() == HumanoidArm.RIGHT;
        if (p_102866_.isUsingItem()) {
            boolean flag3 = p_102866_.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (flag3 == flag2) {
                this.poseRightArm(p_102866_);
            } else {
                this.poseLeftArm(p_102866_);
            }
        } else {
            boolean flag4 = flag2 ? this.leftArmPose.isTwoHanded() : this.rightArmPose.isTwoHanded();
            if (flag2 != flag4) {
                this.poseLeftArm(p_102866_);
                this.poseRightArm(p_102866_);
            } else {
                this.poseRightArm(p_102866_);
                this.poseLeftArm(p_102866_);
            }
        }

        this.setupAttackAnimation(p_102866_);
        if (this.crouching) {
            this.body.xRot += 0.5F;
            this.rightArm.xRot += 0.4F;
            this.leftArm.xRot += 0.4F;
            this.rightLeg.z+= 4.0F;
            this.leftLeg.z+= 4.0F;
            this.rightLeg.y+= .2F;
            this.leftLeg.y+= .2F;
            this.head.y+= 4.2F;
            this.body.y+= 3.2F;
            this.leftArm.y+= 3.2F;
            this.rightArm.y+= 3.2F;
        }

        if (this.rightArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.rightArm, p_102869_, 1.0F);
        }

        if (this.leftArmPose != HumanoidModel.ArmPose.SPYGLASS) {
            AnimationUtils.bobModelPart(this.leftArm, p_102869_, -1.0F);
        }

        if (this.swimAmount > 0.0F) {
            float f5 = p_102867_ % 26.0F;
            HumanoidArm humanoidarm = this.getAttackArm(p_102866_);
            float f1 = humanoidarm == HumanoidArm.RIGHT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            float f2 = humanoidarm == HumanoidArm.LEFT && this.attackTime > 0.0F ? 0.0F : this.swimAmount;
            if (!p_102866_.isUsingItem()) {
                if (f5 < 14.0F) {
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, 0.0F);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, 0.0F);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(
                            f2, this.leftArm.zRot, (float) Math.PI + 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F)
                    );
                    this.rightArm.zRot = Mth.lerp(
                            f1, this.rightArm.zRot, (float) Math.PI - 1.8707964F * this.quadraticArmUpdate(f5) / this.quadraticArmUpdate(14.0F)
                    );
                } else if (f5 >= 14.0F && f5 < 22.0F) {
                    float f6 = (f5 - 14.0F) / 8.0F;
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, (float) (Math.PI / 2) * f6);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, (float) (Math.PI / 2) * f6);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, 5.012389F - 1.8707964F * f6);
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, 1.2707963F + 1.8707964F * f6);
                } else if (f5 >= 22.0F && f5 < 26.0F) {
                    float f3 = (f5 - 22.0F) / 4.0F;
                    this.leftArm.xRot = this.rotlerpRad(f2, this.leftArm.xRot, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3);
                    this.rightArm.xRot = Mth.lerp(f1, this.rightArm.xRot, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3);
                    this.leftArm.yRot = this.rotlerpRad(f2, this.leftArm.yRot, (float) Math.PI);
                    this.rightArm.yRot = Mth.lerp(f1, this.rightArm.yRot, (float) Math.PI);
                    this.leftArm.zRot = this.rotlerpRad(f2, this.leftArm.zRot, (float) Math.PI);
                    this.rightArm.zRot = Mth.lerp(f1, this.rightArm.zRot, (float) Math.PI);
                }
            }

            this.leftLeg.xRot = Mth.lerp(this.swimAmount, this.leftLeg.xRot, 0.3F * Mth.cos(p_102867_ * 0.33333334F + (float) Math.PI));
            this.rightLeg.xRot = Mth.lerp(this.swimAmount, this.rightLeg.xRot, 0.3F * Mth.cos(p_102867_ * 0.33333334F));
        }
    }

    private void poseRightArm(LivingEntity p_102876_) {
        switch(this.rightArmPose) {
            case EMPTY:
                this.rightArm.yRot = 0.0F;
                break;
            case BLOCK:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - 0.9424779F;
                this.rightArm.yRot = (float) (-Math.PI / 6);
                break;
            case ITEM:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) (Math.PI / 10);
                this.rightArm.yRot = 0.0F;
                break;
            case THROW_SPEAR:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) Math.PI;
                this.rightArm.yRot = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1F + this.head.yRot;
                this.leftArm.yRot = 0.1F + this.head.yRot + 0.4F;
                this.rightArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                this.leftArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                break;
            case CROSSBOW_CHARGE:
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102876_, true);
                break;
            case CROSSBOW_HOLD:
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
                break;
            case BRUSH:
                this.rightArm.xRot = this.rightArm.xRot * 0.5F - (float) (Math.PI / 5);
                this.rightArm.yRot = 0.0F;
                break;
            case SPYGLASS:
                this.rightArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102876_.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                this.rightArm.yRot = this.head.yRot - (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                this.rightArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
                this.rightArm.yRot = this.head.yRot - (float) (Math.PI / 6);
        }
    }

    private void poseLeftArm(LivingEntity p_102879_) {
        switch(this.leftArmPose) {
            case EMPTY:
                this.leftArm.yRot = 0.0F;
                break;
            case BLOCK:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - 0.9424779F;
                this.leftArm.yRot = (float) (Math.PI / 6);
                break;
            case ITEM:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) (Math.PI / 10);
                this.leftArm.yRot = 0.0F;
                break;
            case THROW_SPEAR:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) Math.PI;
                this.leftArm.yRot = 0.0F;
                break;
            case BOW_AND_ARROW:
                this.rightArm.yRot = -0.1F + this.head.yRot - 0.4F;
                this.leftArm.yRot = 0.1F + this.head.yRot;
                this.rightArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                this.leftArm.xRot = (float) (-Math.PI / 2) + this.head.xRot;
                break;
            case CROSSBOW_CHARGE:
                AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, p_102879_, false);
                break;
            case CROSSBOW_HOLD:
                AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, false);
                break;
            case BRUSH:
                this.leftArm.xRot = this.leftArm.xRot * 0.5F - (float) (Math.PI / 5);
                this.leftArm.yRot = 0.0F;
                break;
            case SPYGLASS:
                this.leftArm.xRot = Mth.clamp(this.head.xRot - 1.9198622F - (p_102879_.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                this.leftArm.yRot = this.head.yRot + (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                this.leftArm.xRot = Mth.clamp(this.head.xRot, -1.2F, 1.2F) - 1.4835298F;
                this.leftArm.yRot = this.head.yRot + (float) (Math.PI / 6);
        }
    }

    protected void setupAttackAnimation(LivingEntity p_102858_) {
        if (!(this.attackTime <= 0.0F)) {
            HumanoidArm humanoidarm = this.getAttackArm(p_102858_);
            ModelPart modelpart = this.getArm(humanoidarm);
            float f = this.attackTime;
            this.body.yRot = Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2)) * 0.2F;
            if (humanoidarm == HumanoidArm.LEFT) {
                this.body.yRot *= -1.0F;
            }

            this.rightArm.yRot += this.body.yRot;
            this.leftArm.yRot += this.body.yRot;
            this.leftArm.xRot += this.body.yRot;
            f = 1.0F - this.attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(this.attackTime * (float) Math.PI) * -(this.head.xRot - 0.7F) * 0.75F;
            modelpart.xRot -= f1 * 1.2F + f2;
            modelpart.yRot += this.body.yRot * 2.0F;
            modelpart.zRot += Mth.sin(this.attackTime * (float) Math.PI) * -0.4F;
        }
    }

    protected float rotlerpRad(float p_102836_, float p_102837_, float p_102838_) {
        float f = (p_102838_ - p_102837_) % (float) (Math.PI * 2);
        if (f < (float) -Math.PI) {
            f += (float) (Math.PI * 2);
        }

        if (f >= (float) Math.PI) {
            f -= (float) (Math.PI * 2);
        }

        return p_102837_ + p_102836_ * f;
    }

    private float quadraticArmUpdate(float p_102834_) {
        return -65.0F * p_102834_ + p_102834_ * p_102834_;
    }

    public void copyPropertiesTo(HierarchicalHumanoidModel<T> copyTo) {
        super.copyPropertiesTo(copyTo);
        copyTo.leftArmPose = this.leftArmPose;
        copyTo.rightArmPose = this.rightArmPose;
        copyTo.crouching = this.crouching;
        copyTo.head.copyFrom(this.head);
        copyTo.body.copyFrom(this.body);
        copyTo.rightArm.copyFrom(this.rightArm);
        copyTo.leftArm.copyFrom(this.leftArm);
        copyTo.rightLeg.copyFrom(this.rightLeg);
        copyTo.leftLeg.copyFrom(this.leftLeg);
    }

    public void copyPropertiesToArmor(ArmorModel<T> copyTo) {
        copyTo.head.copyFrom(this.head);
        copyTo.body.copyFrom(this.body);
        copyTo.rightArm.copyFrom(this.rightArm);
        copyTo.leftArm.copyFrom(this.leftArm);
        copyTo.rightLeg.copyFrom(this.rightLeg);
        copyTo.leftLeg.copyFrom(this.leftLeg);
    }

    @Override
    public void translateToHand(HumanoidArm p_102854_, PoseStack p_102855_) {
        this.getArm(p_102854_).translateAndRotate(p_102855_);
    }

    protected ModelPart getArm(HumanoidArm p_102852_) {
        return p_102852_ == HumanoidArm.LEFT ? this.leftArm : this.rightArm;
    }

    @Override
    public @NotNull ModelPart getHead() {
        return this.head;
    }

    private HumanoidArm getAttackArm(LivingEntity p_102857_) {
        HumanoidArm humanoidarm = p_102857_.getMainArm();
        return p_102857_.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }

    @Override
    public @NotNull ModelPart root() {
        return root;
    }
}
