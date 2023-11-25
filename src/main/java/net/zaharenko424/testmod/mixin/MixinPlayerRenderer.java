package net.zaharenko424.testmod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.client.renderer.LatexEntityRenderer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    @Unique
    private LatexEntityRenderer<AbstractClientPlayer> mod$renderer;

    public MixinPlayerRenderer(EntityRendererProvider.Context p_174289_, PlayerModel<AbstractClientPlayer> p_174290_, float p_174291_) {
        super(p_174289_, p_174290_, p_174291_);
    }

    @Inject(at = @At("RETURN"),method = "<init>")
    private void onInit(EntityRendererProvider.Context p_174557_, boolean p_174558_, CallbackInfo ci){
        mod$renderer=new LatexEntityRenderer<>(p_174557_);
    }

    @Inject(at = @At("HEAD"),
            method = "render(Lnet/minecraft/client/player/AbstractClientPlayer;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            cancellable = true)
    private void onRender(AbstractClientPlayer p_117788_, float p_117789_, float p_117790_, PoseStack p_117791_, MultiBufferSource p_117792_, int p_117793_, CallbackInfo ci){
        if(mod$check(p_117788_)) {
            mod$renderer.render(p_117788_, p_117789_, p_117790_, p_117791_, p_117792_, p_117793_);
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "HEAD"),method = "renderRightHand", cancellable = true)
    private void onRenderRightHand(PoseStack p_117771_, MultiBufferSource p_117772_, int p_117773_, AbstractClientPlayer p_117774_, CallbackInfo ci){
        if(mod$check(p_117774_)) {
            mod$renderer.renderHand(p_117771_, p_117772_, p_117773_, p_117774_, true);
            ci.cancel();
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "renderLeftHand", cancellable = true)
    private void onRenderLeftHand(PoseStack p_117814_, MultiBufferSource p_117815_, int p_117816_, AbstractClientPlayer p_117817_, CallbackInfo ci){
        if(mod$check(p_117817_)) {
            mod$renderer.renderHand(p_117814_, p_117815_, p_117816_, p_117817_, false);
            ci.cancel();
        }
    }

    @Unique
    private boolean mod$check(@NotNull AbstractClientPlayer player){
        if(!TransfurManager.hasCapability(player)) return mod$renderer.isTransfurTypeNonNull();
        if(!TransfurManager.isTransfurred(player)) {
            if(mod$renderer.isTransfurTypeNonNull()) mod$renderer.updateTransfurType(null);
            return false;
        }
        mod$renderer.updateTransfurType(TransfurManager.getTransfurType(player));
        return true;
    }
}