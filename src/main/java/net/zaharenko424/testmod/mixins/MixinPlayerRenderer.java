package net.zaharenko424.testmod.mixins;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.zaharenko424.testmod.TransfurManager;
import net.zaharenko424.testmod.client.model.AbstractLatexEntityModel;
import net.zaharenko424.testmod.client.renderer.LatexEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class MixinPlayerRenderer extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    @Unique
    private LatexEntityRenderer<AbstractClientPlayer, AbstractLatexEntityModel<AbstractClientPlayer>> mod$renderer;

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
        if(!TransfurManager.isTransfurred(p_117788_)) {
            if(!mod$renderer.isTransfurTypeNull()) mod$renderer.updateTransfurType(null);
            return;
        }
        mod$renderer.updateTransfurType(TransfurManager.getTransfurType(p_117788_));
        mod$renderer.render(p_117788_,p_117789_,p_117790_,p_117791_,p_117792_,p_117793_);
        ci.cancel();
    }
}