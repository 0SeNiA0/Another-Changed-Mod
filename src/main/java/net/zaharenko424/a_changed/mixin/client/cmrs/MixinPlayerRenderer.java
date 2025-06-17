package net.zaharenko424.a_changed.mixin.client.cmrs;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.a_changed.entity.SeatEntity;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.client.model.UniversalCustomModel;
import net.zaharenko424.cmrs.client.renderer.CustomModelRenderer;
import net.zaharenko424.cmrs.client.renderer.DynamicModelRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    @Unique
    private CustomModelRenderer<LivingEntity, UniversalCustomModel<LivingEntity>> cmrs$renderer;

    public MixinPlayerRenderer(EntityRendererProvider.Context context, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(at = @At("RETURN"),method = "<init>")
    private void onInit(EntityRendererProvider.Context context, boolean p_174558_, CallbackInfo ci){
        cmrs$renderer = new DynamicModelRenderer<>(context, player -> CustomModelManager.getInstance().getModel((AbstractClientPlayer) player), .5f);
    }

    @Inject(at = @At("HEAD"),
            method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            cancellable = true)
    private void onRender(@NotNull AbstractClientPlayer player, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource source, int packedLight, CallbackInfo ci){
        if(player.getVehicle() instanceof SeatEntity seat && !seat.renderPlayer()){
            ci.cancel();
            return;
        }

        if(cmrs$hasNoCustomModel(player)) return;
        cmrs$renderer.render(player, entityYaw, partialTicks, poseStack, source, packedLight);

        ci.cancel();
    }

    @Inject(at = @At(value = "HEAD"),method = "renderRightHand", cancellable = true)
    private void onRenderRightHand(PoseStack poseStack, MultiBufferSource source, int packedLight, AbstractClientPlayer player, CallbackInfo ci){
        if(cmrs$hasNoCustomModel(player)) return;
        cmrs$renderer.renderHand(player, poseStack, packedLight, HumanoidArm.RIGHT);
        ci.cancel();
    }

    @Inject(at = @At(value = "HEAD"), method = "renderLeftHand", cancellable = true)
    private void onRenderLeftHand(PoseStack poseStack, MultiBufferSource source, int packedLight, AbstractClientPlayer player, CallbackInfo ci){
        if(cmrs$hasNoCustomModel(player)) return;
        cmrs$renderer.renderHand(player, poseStack, packedLight, HumanoidArm.LEFT);
        ci.cancel();
    }

    @Unique
    private boolean cmrs$hasNoCustomModel(@NotNull AbstractClientPlayer player){
        return !CustomModelManager.getInstance().hasCustomModel(player);
    }
}