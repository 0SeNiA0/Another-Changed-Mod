package net.zaharenko424.a_changed.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ElytraLayer;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.model.AbstractLatexEntityModel;
import net.zaharenko424.a_changed.client.model.DummyModel;
import net.zaharenko424.a_changed.client.model.layers.ArmorLayer;
import net.zaharenko424.a_changed.client.model.layers.SpinAttackEffect;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class LatexEntityRenderer<E extends LivingEntity> extends LivingEntityRenderer<E, AbstractLatexEntityModel<E>> {

    protected AbstractTransfurType transfurType=null;
    protected AbstractLatexEntityModel<E> armorModel;

    protected final EntityRendererProvider.Context context;

    protected LatexEntityRenderer(EntityRendererProvider.Context context, AbstractLatexEntityModel<E> model){
        super(context,model,1);
        this.context = context;
        //addLayer(new PerFaceArmorLayer<>(this,context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET)));
        addLayer(new ArmorLayer<>(this, context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET)));
        addLayer(new ItemInHandLayer<>(this, this.context.getItemInHandRenderer()));
        //addLayer(new CustomHeadLayer<>(this,context.getModelSet(),context.getItemInHandRenderer()));//TODO fix, more like rewrite
        addLayer(new ElytraLayer<>(this, this.context.getModelSet()));
    }

    /**
    Only use for creating latex renderer for players!
     */
    public LatexEntityRenderer(EntityRendererProvider.Context context) {
        this(context, new DummyModel<>());
        addLayer(new SpinAttackEffect<>(this,context.getModelSet()));
    }

    public LatexEntityRenderer(EntityRendererProvider.Context context, @NotNull DeferredHolder<AbstractTransfurType,? extends AbstractTransfurType> transfurType){
        this(context);
        updateTransfurType(transfurType.get());
    }

    public boolean isTransfurTypeNonNull(){
        return transfurType != null;
    }

    public void updateTransfurType(@Nullable AbstractTransfurType transfurType){
        if(transfurType==this.transfurType) return;
        this.transfurType=transfurType;
        if(transfurType!=null) {
            model = transfurType.getModel();
            armorModel = transfurType.getArmorModel();
        }
    }

    public AbstractLatexEntityModel<E> getArmorModel(){
        return armorModel;
    }

    public void renderHand(@NotNull PoseStack poseStack, MultiBufferSource source, int i, AbstractClientPlayer player, boolean right){
        if(right) {
            model.rightArm.resetPose();
            model.rightArm.y=2;
            model.rightArm.render(poseStack, source.getBuffer(RenderType.entitySolid(getTextureLocation(player))), i, OverlayTexture.NO_OVERLAY);
        } else {
            model.leftArm.resetPose();
            model.leftArm.y=2;
            model.leftArm.render(poseStack, source.getBuffer(RenderType.entitySolid(getTextureLocation(player))), i, OverlayTexture.NO_OVERLAY);
        }
    }

    @Override
    public void render(@NotNull E entity, float entityYaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int light) {
        setModelProperties(entity);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, light);
        //this.addLayer(new ArrowLayer<>(p_174557_, this));
        //this.addLayer(new SpinAttackEffectLayer<>(this, p_174557_.getModelSet()));
    }

    private void setModelProperties(@NotNull E entity){
        model.crouching=entity.isCrouching();
        model.attackTime=entity.attackAnim;
        if(entity instanceof Player player){
            if(player.isSpectator()){
                model.setAllVisible(false);
                model.setAllVisible(model.head,true);
            } else {
                model.setAllVisible(true);
            }
        }
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

    private HumanoidModel.ArmPose getArmPose(@NotNull E p_117795_, InteractionHand p_117796_) {
        ItemStack itemstack = p_117795_.getItemInHand(p_117796_);
        if (itemstack.isEmpty()) {
            return HumanoidModel.ArmPose.EMPTY;
        } else {
            if (p_117795_.getUsedItemHand() == p_117796_ && p_117795_.getUseItemRemainingTicks() > 0) {
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

                if (useanim == UseAnim.CROSSBOW && p_117796_ == p_117795_.getUsedItemHand()) {
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
                } else if (!p_117795_.swinging && itemstack.getItem() instanceof CrossbowItem && CrossbowItem.isCharged(itemstack)) {
                    return HumanoidModel.ArmPose.CROSSBOW_HOLD;
                }
            }

            HumanoidModel.ArmPose forgeArmPose = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(itemstack).getArmPose(p_117795_, p_117796_, itemstack);
            if (forgeArmPose != null) return forgeArmPose;

            return HumanoidModel.ArmPose.ITEM;
        }
    }

    @Override
    protected void setupRotations(@NotNull E entity, @NotNull PoseStack poseStack, float ageInTicks, float yaw, float ticks) {
        //TODO or check for swimming latex || MAKE BETTER CHECK
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

    protected boolean shouldShowName(@NotNull E entity) {
        return super.shouldShowName(entity)
                && (entity.shouldShowName() || entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LivingEntity p_114482_) {
        //return transfurType!=null ? AChanged.textureLoc("texture") : AChanged.textureLoc("entity/dummy");
        return transfurType!=null ? AChanged.textureLoc(transfurType.id.withPrefix("entity/")) : AChanged.textureLoc("entity/dummy");
    }
}