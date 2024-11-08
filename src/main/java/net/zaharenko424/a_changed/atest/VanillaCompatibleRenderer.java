package net.zaharenko424.a_changed.atest;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.client.cmrs.layers.GlowLayer;
import net.zaharenko424.a_changed.client.cmrs.model.CustomModel;
import org.jetbrains.annotations.NotNull;

public class VanillaCompatibleRenderer<E extends LivingEntity, M extends /*EntityModel<E>*/UniversalCustomModel<E> & CustomModel> extends LivingEntityRenderer<E, M> {

    static final ModelPropertyType<Void> GLOWING = new ModelPropertyType<>();
    static final ModelPropertyType<Void> VANILLA_ARMOR = new ModelPropertyType<>();// vanilla armor layer -> type in property
    static final ModelPropertyType<Void> EMBEDDED_ARMOR = new ModelPropertyType<>();// lists of parts mapped to armor slots
    static final ModelPropertyType<Void> ARMED = new ModelPropertyType<>();//translateToArm + poseArm?
    static final ModelPropertyType<Void> PLAYER_COMPATIBLE = new ModelPropertyType<>();//renderHand(FPV)
    static final ModelPropertyType<Void> SWAP_MODEL = new ModelPropertyType<>();//? mb use with predicate to swap latex pup model to latex wolf. make this a non-serializable hardcoded thing

    protected final EntityRendererProvider.Context context;

    public VanillaCompatibleRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
        this.context = context;
        addLayers();
        if(model.hasProperty(GLOWING)) addLayer(new GlowLayer<>(this));
    }

    protected void addLayers(){}

    /*private void setModelProperties(E entity){//TODO Move to model.prepareMobModel()
        model.crouching = entity.isCrouching();

        if(entity instanceof Player player && player.isSpectator()){
            model.setAllVisible(false);
            model.setAllVisible(true, model.head);
        } else model.setAllVisible(true);
        model.setDrawAll(true);

        HumanoidModel.ArmPose humanoidmodel$armpose = getArmPose(entity, InteractionHand.MAIN_HAND);
        HumanoidModel.ArmPose humanoidmodel$armpose1 = getArmPose(entity, InteractionHand.OFF_HAND);
        if (humanoidmodel$armpose.isTwoHanded()) {
            humanoidmodel$armpose1 = entity.getOffhandItem().isEmpty() ? HumanoidModel.ArmPose.EMPTY : HumanoidModel.ArmPose.ITEM;
        }

        if (entity.getMainArm() == HumanoidArm.RIGHT) {
            model.rightArmPose = humanoidmodel$armpose;
            model.leftArmPose = humanoidmodel$armpose1;
        } else {
            model.rightArmPose = humanoidmodel$armpose1;
            model.leftArmPose = humanoidmodel$armpose;
        }
    }*/

    @Override
    protected void setupRotations(E entity, @NotNull PoseStack poseStack, float bob, float yBodyRot, float partialTick, float scale) {
        float f = entity.getSwimAmount(partialTick);
        float f1 = entity.getViewXRot(partialTick);
        super.setupRotations(entity, poseStack, bob, yBodyRot, partialTick, scale);
        if (entity.isFallFlying() && entity instanceof AbstractClientPlayer player) {
            float f2 = (float) player.getFallFlyingTicks() + partialTick;
            float f3 = Mth.clamp(f2 * f2 / 100.0F, 0.0F, 1.0F);
            if (!player.isAutoSpinAttack()) {
                poseStack.mulPose(Axis.XP.rotationDegrees(f3 * (-90.0F - f1)));
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
        } else if (f > 0.0F) {
            float f3 = entity.isInWater() || entity.isInFluidType((fluidType, height) -> entity.canSwimInFluidType(fluidType)) ? -90.0F - entity.getXRot() : -90.0F;
            float f5 = Mth.lerp(f, 0.0F, f3);
            poseStack.mulPose(Axis.XP.rotationDegrees(f5));
            if (entity.isVisuallySwimming()) {
                poseStack.translate(0.0F, -1.0F, 0.3F);
            }
        }
    }

    protected boolean shouldShowName(@NotNull E entity) {
        return super.shouldShowName(entity)
                && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull E entity) {
        return model.getTexture();
    }
}