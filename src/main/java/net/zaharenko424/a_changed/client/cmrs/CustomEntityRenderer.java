package net.zaharenko424.a_changed.client.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.phys.Vec3;
import net.zaharenko424.a_changed.client.cmrs.geom.ModelPart;
import net.zaharenko424.a_changed.client.cmrs.layers.*;
import net.zaharenko424.a_changed.client.cmrs.model.CustomEntityModel;
import net.zaharenko424.a_changed.client.cmrs.model.DummyModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CustomEntityRenderer<E extends LivingEntity> extends LivingEntityRenderer<E, CustomEntityModel<E>> {

    protected final EntityRendererProvider.Context context;

    public CustomEntityRenderer(EntityRendererProvider.Context context, CustomEntityModel<E> model){
        super(context,model,.5f);
        this.context = context;
        addLayers();
        addLayer(new GlowLayer<>(this));
    }

    /**
    Only use for creating latex renderer for players!
     */
    public CustomEntityRenderer(EntityRendererProvider.Context context) {
        this(context, new DummyModel<>());
    }

    protected void addLayers(){
        addLayer(new ArmorLayer<>(this, context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET)));
        addLayer(new ItemInHandLayer<>(this, this.context.getItemInHandRenderer()));
        addLayer(new ElytraLayerFix<>(this, this.context.getModelSet()));
        addLayer(new SpinAttackEffect<>(this, context.getModelSet()));
        addLayer(new ArrowLayer<>(context.getEntityRenderDispatcher(), this));
        //addLayer(new CustomHeadLayer<>(this,context.getModelSet(),context.getItemInHandRenderer()));//TODO fix, more like rewrite
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        if(entity instanceof AbstractClientPlayer player && CustomModelManager.getInstance().hasCustomModel(player))
            model = CustomModelManager.getInstance().getModel(player);
        setModelProperties(entity);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
    }

    public void renderHand(PoseStack poseStack, MultiBufferSource source, int light, AbstractClientPlayer player, boolean right){
        if(CustomModelManager.getInstance().hasCustomModel(player))
            model = CustomModelManager.getInstance().getModel(player);
        assert model != null;
        ModelPart arm = right ? model.rightArm : model.leftArm;
        arm.resetPose();
        if(right) {
            arm.x = -6;
            arm.y = 1.5f;
            arm.z = -2;
            arm.yRot = Mth.DEG_TO_RAD * 5;
        } else {
            arm.x = 5;
            arm.y = 1.5f;
            arm.z = 0;
            arm.yRot = Mth.DEG_TO_RAD * -5;
        }
        arm.zRot = Mth.DEG_TO_RAD * 180;
        arm.offsetScale(new Vector3f(-.1f));

        model.setDrawAll(false, arm);
        boolean[] hasGlow = {false};
        model.setDrawAllUntil(true, part -> {
            if(part.isGlowing() && !hasGlow[0]) hasGlow[0] = true;
            return part.isArmor() || part.isGlowing();
        }, arm);
        arm.render(poseStack, source.getBuffer(RenderType.entitySolid(getTextureLocation((E) player))), light, OverlayTexture.NO_OVERLAY);

        if(!hasGlow[0]) return;
        model.setDrawAll(false, arm);
        model.setupDrawGlow(false, arm);
        arm.render(poseStack, source.getBuffer(RenderType.eyes(getTextureLocation((E) player))), light, OverlayTexture.NO_OVERLAY);
    }

    private void setModelProperties(E entity){
        model.crouching = entity.isCrouching();
        model.attackTime = entity.attackAnim;

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
    }

    @Override
    protected void setupRotations(E entity, PoseStack poseStack, float ageInTicks, float yaw, float ticks) {
        float f = entity.getSwimAmount(ticks);
        float f1 = entity.getViewXRot(ticks);
        super.setupRotations(entity, poseStack, ageInTicks, yaw, ticks);
        if (entity.isFallFlying() && entity instanceof AbstractClientPlayer player) {
            float f2 = (float) player.getFallFlyingTicks() + ticks;
            float f3 = Mth.clamp(f2 * f2 / 100.0F, 0.0F, 1.0F);
            if (!player.isAutoSpinAttack()) {
                poseStack.mulPose(Axis.XP.rotationDegrees(f3 * (-90.0F - f1)));
            }

            Vec3 vec3 = player.getViewVector(ticks);
            Vec3 vec31 = player.getDeltaMovementLerped(ticks);
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

    private HumanoidModel.ArmPose getArmPose(E entity, InteractionHand hand) {
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

    protected boolean shouldShowName(E entity) {
        return super.shouldShowName(entity)
                && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@Nullable E entity) {
        return model.getTexture();
    }
}