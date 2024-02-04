package net.zaharenko424.a_changed.client.renderer;

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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import net.zaharenko424.a_changed.client.model.DummyModel;
import net.zaharenko424.a_changed.client.model.layers.*;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class LatexEntityRenderer<E extends LivingEntity> extends LivingEntityRenderer<E, AbstractLatexEntityModel<E>> {

    protected AbstractTransfurType transfurType=null;
    protected final EntityRendererProvider.Context context;

    protected LatexEntityRenderer(EntityRendererProvider.Context context, AbstractLatexEntityModel<E> model){
        super(context,model,.5f);
        this.context = context;
        addLayers();
        addLayer(new GlowLayer<>(this));
    }

    /**
    Only use for creating latex renderer for players!
     */
    public LatexEntityRenderer(EntityRendererProvider.Context context) {
        this(context, new DummyModel<>());
    }

    public LatexEntityRenderer(EntityRendererProvider.Context context, DeferredHolder<AbstractTransfurType,? extends AbstractTransfurType> transfurType){
        this(context);
        updateTransfurType(transfurType.get());
    }

    protected void addLayers(){
        addLayer(new ArmorLayer<>(this, context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET)));
        addLayer(new ItemInHandLayer<>(this, this.context.getItemInHandRenderer()));
        addLayer(new ElytraLayerFix<>(this, this.context.getModelSet()));
        addLayer(new SpinAttackEffect<>(this,context.getModelSet()));
        addLayer(new ArrowLayer<>(context.getEntityRenderDispatcher(), this));
        //addLayer(new CustomHeadLayer<>(this,context.getModelSet(),context.getItemInHandRenderer()));//TODO fix, more like rewrite
    }

    public boolean isTransfurTypeNonNull(){
        return transfurType != null;
    }

    public void updateTransfurType(@Nullable AbstractTransfurType transfurType){
        if(transfurType == this.transfurType) return;
        this.transfurType = transfurType;
        if(transfurType != null) {
            model = transfurType.getModel();
        }
    }

    @Override
    public void render(E entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        setModelProperties(entity);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
    }

    public void renderHand(PoseStack poseStack, MultiBufferSource source, int i, AbstractClientPlayer player, boolean right){
        if(right) {
            model.rightArm.resetPose();
            model.rightArm.x = 3; //rotation
            model.rightArm.z = 12;//<- ->
            model.rightArm.offsetScale(new Vector3f(.9f));
            model.setDrawAll(true, model.rightArm);
            model.setDrawAllAfterMatching(false, name -> name.startsWith("armor_"), model.rightArm);
            model.rightArm.render(poseStack, source.getBuffer(RenderType.entitySolid(getTextureLocation((E) player))), i, OverlayTexture.NO_OVERLAY);
        } else {
            model.leftArm.resetPose();
            model.leftArm.x = -1;
            model.leftArm.z = 12;
            model.leftArm.offsetScale(new Vector3f(.9f));
            model.setDrawAll(true, model.leftArm);
            model.setDrawAllAfterMatching(false, name -> name.startsWith("armor_"), model.leftArm);
            model.leftArm.render(poseStack, source.getBuffer(RenderType.entitySolid(getTextureLocation((E) player))), i, OverlayTexture.NO_OVERLAY);
        }
    }

    private void setModelProperties(E entity){
        model.crouching=entity.isCrouching();
        model.attackTime=entity.attackAnim;

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
        //TODO check for swimming latex
        float f = entity.getSwimAmount(ticks);
        float f1 = entity.getViewXRot(ticks);
        super.setupRotations(entity, poseStack, ageInTicks, yaw, ticks);
        if (entity.isFallFlying()&& entity instanceof AbstractClientPlayer player) {
            float f2 = (float)player.getFallFlyingTicks() + ticks;
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
        return transfurType!=null ? AChanged.textureLoc(transfurType.id.withPrefix("entity/")) : AChanged.textureLoc("entity/dummy");
    }
}