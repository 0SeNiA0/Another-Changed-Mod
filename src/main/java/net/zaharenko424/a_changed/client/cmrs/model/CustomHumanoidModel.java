package net.zaharenko424.a_changed.client.cmrs.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.a_changed.client.cmrs.ModelDefinitionCache;
import net.zaharenko424.a_changed.client.cmrs.animation.AnimationUtils;
import net.zaharenko424.a_changed.client.cmrs.animation.KeyframeAnimator;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.function.Predicate;

import static net.zaharenko424.a_changed.util.Utils.quadraticArmUpdate;
import static net.zaharenko424.a_changed.util.Utils.rotlerpRad;

@ParametersAreNonnullByDefault
public abstract class CustomHumanoidModel<E extends LivingEntity> extends EntityModel<E> implements ArmedModel {

    private final ModelPart root;
    public final ModelPart head;
    public final ModelPart body;
    public final ModelPart rightArm;
    public final ModelPart leftArm;
    public final ModelPart rightLeg;
    public final ModelPart leftLeg;
    protected final ImmutableList<ModelPart> bodyParts;
    public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
    public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
    public boolean crouching;
    public float swimAmount;
    protected final ResourceLocation texture;

    public CustomHumanoidModel(ModelLayerLocation location, ResourceLocation texture) {
        this(ModelDefinitionCache.INSTANCE.bake(location), texture);
    }

    public CustomHumanoidModel(ModelPart modelRoot, ResourceLocation texture){
        super(RenderType::entityCutoutNoCull);
        this.root = modelRoot.getChild("root");
        head = root.getChild("head");
        body = root.getChild("body");
        rightArm = root.getChild("right_arm");
        leftArm = root.getChild("left_arm");
        rightLeg = root.getChild("right_leg");
        leftLeg = root.getChild("left_leg");
        bodyParts = ImmutableList.copyOf(root.getAllParts().filter(ModelPart::hasCubes).iterator());
        if(!texture.getPath().startsWith("textures/")) this.texture = AChanged.textureLoc(texture.withPrefix("entity/"));
        else this.texture = texture;
    }

    public boolean hasArmor(){
        return true;
    }

    public boolean hasGlowingArmor(){
        return false;
    }

    public boolean hasGlowParts(){
        return false;
    }

    public ResourceLocation getTexture(){
        return texture;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int light, int overlay, float r, float g, float b, float alpha) {
        root().render(poseStack, consumer, light, overlay, r, g, b, alpha);
    }

    public void prepareMobModel(@NotNull E entity, float limbSwing, float limbSwingAmount, float tick) {
        swimAmount = entity.getSwimAmount(tick);
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, tick);
    }

    @Override
    public void setupAnim(E entity, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch) {
        setAllVisible(true);
        setDrawAll(true);
        hideArmorAndGlow();
        root.getAllParts().forEach(ModelPart::resetPose);

        boolean flag = entity.getFallFlyingTicks() > 4;
        boolean flag1 = entity.isVisuallySwimming();
        head.yRot = headYaw * Mth.DEG_TO_RAD;
        if (flag) {
            head.xRot = (float) Math.PI / 4;
        } else if (swimAmount > 0.0F) {
            if (flag1) {
                head.xRot = rotlerpRad(swimAmount, head.xRot, (float) Math.PI / 4);
            } else {
                head.xRot = rotlerpRad(swimAmount, head.xRot, headPitch * -Mth.DEG_TO_RAD);
            }
        } else {
            head.xRot = headPitch * Mth.DEG_TO_RAD;
        }

        float f = 1.0F;
        if (flag) {
            f = (float)entity.getDeltaMovement().lengthSqr();
            f /= 0.2F;
            f *= f * f;
        }

        if (f < 1.0F) f = 1.0F;

        rightArm.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F / f;
        leftArm.xRot = Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
        rightLeg.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
        leftLeg.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount / f;

        if (riding) setupRiding();

        setupArms(entity);

        setupAttackAnimation(entity);

        if (crouching) setupCrouching();

        if (rightArmPose != HumanoidModel.ArmPose.SPYGLASS) AnimationUtils.bobModelPart(rightArm, ageInTicks, 1.0F);

        if (leftArmPose != HumanoidModel.ArmPose.SPYGLASS) AnimationUtils.bobModelPart(leftArm, ageInTicks, -1.0F);

        if (swimAmount > 0.0F) setupSwimAnimation(entity,limbSwing);
    }

    protected void setupArms(E entity){
        boolean flag2 = entity.getMainArm() == HumanoidArm.RIGHT;
        if (entity.isUsingItem()) {
            boolean flag3 = entity.getUsedItemHand() == InteractionHand.MAIN_HAND;
            if (flag3 == flag2) {
                poseRightArm(entity);
            } else {
                poseLeftArm(entity);
            }
        } else {
            boolean flag4 = flag2 ? leftArmPose.isTwoHanded() : rightArmPose.isTwoHanded();
            if (flag2 != flag4) {
                poseLeftArm(entity);
                poseRightArm(entity);
            } else {
                poseRightArm(entity);
                poseLeftArm(entity);
            }
        }
    }

    protected void setupRiding(){
        rightArm.xRot += (float) Math.PI / 5;
        leftArm.xRot += (float) Math.PI / 5;
        rightLeg.xRot = 1.4137167F;
        rightLeg.yRot = (float) -Math.PI / 10;
        rightLeg.zRot = 0.07853982F;
        leftLeg.xRot = 1.4137167F;
        leftLeg.yRot = (float) Math.PI / 10;
        leftLeg.zRot = -0.07853982F;
    }

    protected void setupCrouching(){
        body.xRot -= 0.5f;
        body.z += 5;
        rightArm.xRot -= 0.4f;
        rightArm.y -= 2;
        leftArm.xRot -= 0.4f;
        leftArm.y -= 2;
        rightLeg.z += 4;
        leftLeg.z += 4;
        rightLeg.y += .2F;
        leftLeg.y += .2F;
        head.y -= 3;
    }

    protected void setupSwimAnimation(E entity, float limbSwing){
        float f5 = limbSwing % 26.0F;
        HumanoidArm humanoidarm = getAttackArm(entity);
        float f1 = humanoidarm == HumanoidArm.RIGHT && attackTime > 0.0F ? 0.0F : swimAmount;
        float f2 = humanoidarm == HumanoidArm.LEFT && attackTime > 0.0F ? 0.0F : swimAmount;
        if (!entity.isUsingItem()) {
            if (f5 < 14.0F) {
                leftArm.xRot = -rotlerpRad(f2, -leftArm.xRot, 0.0F);
                rightArm.xRot = -Mth.lerp(f1, -rightArm.xRot, 0.0F);
                leftArm.yRot = -rotlerpRad(f2, -leftArm.yRot, (float) Math.PI);
                rightArm.yRot = -Mth.lerp(f1, -rightArm.yRot, (float) Math.PI);
                leftArm.zRot = rotlerpRad(f2, leftArm.zRot, (float) Math.PI + 1.8707964F * quadraticArmUpdate(f5) / quadraticArmUpdate(14.0F));
                rightArm.zRot = Mth.lerp(f1, rightArm.zRot, (float) Math.PI - 1.8707964F * quadraticArmUpdate(f5) / quadraticArmUpdate(14.0F));
            } else if (f5 >= 14.0F && f5 < 22.0F) {
                float f6 = (f5 - 14.0F) / 8.0F;
                leftArm.xRot = -rotlerpRad(f2, -leftArm.xRot, (float) (Math.PI / 2) * f6);
                rightArm.xRot = -Mth.lerp(f1, -rightArm.xRot, (float) (Math.PI / 2) * f6);
                leftArm.yRot = -rotlerpRad(f2, -leftArm.yRot, (float) Math.PI);
                rightArm.yRot = -Mth.lerp(f1, -rightArm.yRot, (float) Math.PI);
                leftArm.zRot = rotlerpRad(f2, leftArm.zRot, 5.012389F - 1.8707964F * f6);
                rightArm.zRot = Mth.lerp(f1, rightArm.zRot, 1.2707963F + 1.8707964F * f6);
            } else if (f5 >= 22.0F && f5 < 26.0F) {
                float f3 = (f5 - 22.0F) / 4.0F;
                leftArm.xRot = -rotlerpRad(f2, -leftArm.xRot, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3);
                rightArm.xRot = -Mth.lerp(f1, -rightArm.xRot, (float) (Math.PI / 2) - (float) (Math.PI / 2) * f3);
                leftArm.yRot = -rotlerpRad(f2, -leftArm.yRot, (float) Math.PI);
                rightArm.yRot = -Mth.lerp(f1, -rightArm.yRot, (float) Math.PI);
                leftArm.zRot = rotlerpRad(f2, leftArm.zRot, (float) Math.PI);
                rightArm.zRot = Mth.lerp(f1, rightArm.zRot, (float) Math.PI);
            }
        }

        leftLeg.xRot = Mth.lerp(swimAmount, leftLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F + (float) Math.PI));
        rightLeg.xRot = Mth.lerp(swimAmount, rightLeg.xRot, 0.3F * Mth.cos(limbSwing * 0.33333334F));
    }

    protected void setupAttackAnimation(E entity) {
        if (attackTime > 0.0F) {
            HumanoidArm humanoidarm = getAttackArm(entity);
            ModelPart arm = getArm(humanoidarm);
            float f = attackTime;
            body.yRot -= Mth.sin(Mth.sqrt(f) * (float) (Math.PI * 2)) * 0.2F;
            if (humanoidarm == HumanoidArm.LEFT) {
                body.yRot *= -1.0F;
            }

            rightArm.yRot = body.yRot;
            leftArm.yRot = body.yRot;
            leftArm.xRot = body.yRot;
            f = 1.0F - attackTime;
            f *= f;
            f *= f;
            f = 1.0F - f;
            float f1 = Mth.sin(f * (float) Math.PI);
            float f2 = Mth.sin(attackTime * (float) Math.PI) * (head.xRot + 0.7F) * 0.75F;
            arm.xRot = f1 * 1.2F + f2;
            arm.yRot = body.yRot * 2.0F;
            arm.zRot = Mth.sin(attackTime * (float) Math.PI) * -0.4F;
        }
    }

    public void setupArmorPart(EquipmentSlot slot, boolean glowing){
        setDrawAll(false);
        switch(slot){
            case HEAD -> setupArmorPartsIn(glowing, head);
            case CHEST -> {
                setupArmorPartsIn(glowing, body);
                setupArmorPartsIn(glowing, rightArm);
                setupArmorPartsIn(glowing, leftArm);
                if(body.hasChild("tail")) setDrawAll(false, body.getChild("tail"));
            }
            case LEGS -> {
                setupArmorPartsIn(glowing, body);//TODO remove or add a new armor part for legs -> chest armor
                setupArmorPartsIn(glowing, rightLeg);
                setupArmorPartsIn(glowing, leftLeg);
            }
            case FEET -> {
                setupArmorPartsIn(glowing, "right_leg_");
                setupArmorPartsIn(glowing, "left_leg_");
            }
        }
    }

    protected void setupArmorPartsIn(boolean glowing, ModelPart bodyPart){
        setupDrawArmor(glowing, bodyPart);
    }

    protected void setupArmorPartsIn(boolean glowing, String name){
        Optional<ModelPart> part = KeyframeAnimator.getAnyDescendantWithName(root, name);
        if(part.isEmpty()) return;
        setupArmorPartsIn(glowing, part.get());
    }

    @Override
    public void translateToHand(HumanoidArm arm, PoseStack poseStack) {
        getArm(arm).translateAndRotate(poseStack);
        poseStack.scale(-1,-1,1);
        if(arm == HumanoidArm.RIGHT) poseStack.translate(-1 / 16f, 0, 0);
        else poseStack.translate(1 / 16f, 0, 0);
    }

    protected HumanoidArm getAttackArm(E entity) {
        HumanoidArm humanoidarm = entity.getMainArm();
        return entity.swingingArm == InteractionHand.MAIN_HAND ? humanoidarm : humanoidarm.getOpposite();
    }

    public ModelPart getArm(HumanoidArm arm) {
        return arm == HumanoidArm.LEFT ? leftArm : rightArm;
    }

    public ModelPart getRandomModelPart(RandomSource p_233439_) {
        return bodyParts.get(p_233439_.nextInt(bodyParts.size()));
    }

    /**
     * Sets whether to draw all parts of the model
     */
    public void setDrawAll(boolean b){
        setDrawAll(b, root);
    }

    /**
     * Sets whether to draw all parts after provided part (including it)
     * @param part starting ModelPart
     */
    public void setDrawAll(boolean b, ModelPart part){
        part.getAllParts().forEach(part0 -> part0.draw = b);
    }

    /**
     * Sets whether to draw all children of provided modelPart until matching predicate.
     * @param draw whether to draw modelParts.
     * @param predicate if true, stops setting draw.
     * @param part starting modelPart.
     */
    public void setDrawAllUntil(boolean draw, Predicate<ModelPart> predicate, ModelPart part){
        part.draw = draw;
        part.getChildren().forEach((name, part1) -> {
            if(predicate.test(part1)) return;
            setDrawAllUntil(draw, predicate, part1);
        });
    }

    /**
     * Sets whether to draw all armor parts.
     * @param glowing should glowing armor parts also be drawn?
     */
    public void setupDrawArmor(boolean glowing){
        setupDrawArmor(glowing, root);
    }

    public void setupDrawArmor(boolean glowing, ModelPart part){
        part.getChildren().forEach((name, part1) -> {
            if(part1.isGlowing() && !glowing) return;
            if(part1.isArmor()) setDrawAllUntil(true, part2 -> part2.isGlowing() && !glowing, part1);
            else setupDrawArmor(glowing, part1);
        });
    }

    /**
     * Sets whether to draw all glowing parts.
     * @param armor should glowing armor parts also be drawn?
     */
    public void setupDrawGlow(boolean armor){
        setupDrawGlow(armor, root());
    }

    public void setupDrawGlow(boolean armor, ModelPart part){
        part.getChildren().forEach((name, part1) -> {
            if(part1.isArmor() && !armor) return;
            if(part1.isGlowing()) setDrawAllUntil(true, part2 -> part2.isArmor() && !armor, part1);
            else setupDrawGlow(armor, part1);
        });
    }

    /**
     * Sets all armor and glow parts to be invisible.
     */
    public void hideArmorAndGlow(){
        hideArmorAndGlow(root());
    }

    /**
     * Sets all armor and glow parts to be invisible.
     * @param part starting modelPart.
     */
    public void hideArmorAndGlow(ModelPart part){
        part.getChildren().forEach((name, part1) -> {
            if(part1.isArmor() || part1.isGlowing()){
                part1.visible = false;
                return;
            }
            hideArmorAndGlow(part1);
        });
    }

    /**
     * Sets visibility of all parts except for root.
     */
    public void setAllVisible(boolean visibility){
        root().getAllParts().filter(part -> part != root()).forEach(child -> child.visible = visibility);
    }

    /**
     * Sets visibility of all children of provided modelPart.
     */
    public void setAllVisible(boolean b, ModelPart part){
        part.getAllParts().forEach(child -> child.visible = b);
    }

    protected void poseRightArm(E entity) {
        if(entity instanceof Player player && TransfurManager.isHoldingEntity(player) && TransfurManager.getGrabMode(player) != GrabMode.FRIENDLY){
            rightArm.xRot = Mth.PI / 1.9f;
            rightArm.yRot = 0.1F + head.yRot;
            return;
        }
        switch(rightArmPose) {
            case EMPTY -> rightArm.yRot = 0.0F;
            case BLOCK -> {
                rightArm.xRot = rightArm.xRot * -0.5F + 0.9424779F;
                rightArm.yRot = (float) Math.PI / 6;
            }
            case ITEM -> {
                rightArm.xRot = rightArm.xRot * 0.5F + (float) (Math.PI / 10);
                rightArm.yRot = 0.0F;
            }
            case THROW_SPEAR -> {
                rightArm.xRot = rightArm.xRot * -0.5F + (float) Math.PI;
                rightArm.yRot = 0.0F;
            }
            case BOW_AND_ARROW -> {
                rightArm.yRot = 0.1F + head.yRot;
                leftArm.yRot = -0.5F + head.yRot;
                rightArm.xRot = (float) Math.PI / 2 + head.xRot;
                leftArm.xRot = (float) Math.PI / 2 + head.xRot;
            }
            case CROSSBOW_CHARGE -> AnimationUtils.animateCrossbowCharge(rightArm, leftArm, entity, true);
            case CROSSBOW_HOLD -> AnimationUtils.animateCrossbowHold(rightArm, leftArm, head, true);
            case BRUSH -> {
                rightArm.xRot = rightArm.xRot * -0.5F + (float) (Math.PI / 5);
                rightArm.yRot = 0.0F;
            }
            case SPYGLASS -> {
                rightArm.xRot = Mth.clamp(head.xRot + 1.9198622F - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                rightArm.yRot = head.yRot + (float) (Math.PI / 12);
            }
            case TOOT_HORN -> {
                rightArm.xRot = (Mth.clamp(head.xRot, -1.2F, 1.2F) + 1.4835298F);
                rightArm.yRot = head.yRot + (float) (Math.PI / 6);
            }
        }
    }

    protected void poseLeftArm(E entity) {//TODO for some reason doesn't work for remote players ...
        if(entity instanceof Player player && TransfurManager.isHoldingEntity(player) && TransfurManager.getGrabMode(player) != GrabMode.FRIENDLY){
            leftArm.xRot = Mth.PI / 1.9f;
            leftArm.yRot = -0.1F + head.yRot;
            return;
        }
        switch(leftArmPose) {
            case EMPTY:
                leftArm.yRot = 0.0F;
                break;
            case BLOCK:
                leftArm.xRot = leftArm.xRot * -0.5F + 0.9424779F;
                leftArm.yRot = (float) -Math.PI / 6;
                break;
            case ITEM:
                leftArm.xRot = leftArm.xRot * 0.5F + (float) (Math.PI / 10);
                leftArm.yRot = 0.0F;
                break;
            case THROW_SPEAR:
                leftArm.xRot = leftArm.xRot * -0.5F + (float) Math.PI;
                leftArm.yRot = 0.0F;
                break;
            case BOW_AND_ARROW:
                rightArm.yRot = 0.5F + head.yRot;
                leftArm.yRot = -0.1F + head.yRot;
                rightArm.xRot = (float) Math.PI / 2 + head.xRot;
                leftArm.xRot = (float) Math.PI / 2 + head.xRot;
                break;
            case CROSSBOW_CHARGE:
                AnimationUtils.animateCrossbowCharge(rightArm, leftArm, entity, false);
                break;
            case CROSSBOW_HOLD:
                AnimationUtils.animateCrossbowHold(rightArm, leftArm, head, false);
                break;
            case BRUSH:
                leftArm.xRot = leftArm.xRot * -0.5F + (float) (Math.PI / 5);
                leftArm.yRot = 0.0F;
                break;
            case SPYGLASS:
                leftArm.xRot = Mth.clamp(head.xRot + 1.9198622F - (entity.isCrouching() ? (float) (Math.PI / 12) : 0.0F), -2.4F, 3.3F);
                leftArm.yRot = head.yRot - (float) (Math.PI / 12);
                break;
            case TOOT_HORN:
                leftArm.xRot = -(Mth.clamp(head.xRot, -1.2F, 1.2F) - 1.4835298F);
                leftArm.yRot = head.yRot - (float) (Math.PI / 6);
        }
    }

    public ModelPart root() {
        return root;
    }
}