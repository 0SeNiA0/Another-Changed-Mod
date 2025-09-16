package net.zaharenko424.a_changed.mixin.cmrs;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.zaharenko424.cmrs.client.renderer.MultiBufferSource;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class MixinInventoryScreen {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;flush()V"), method = "renderEntityInInventory")
    private static void endBatch(GuiGraphics guiGraphics, float x, float y, float scale, Vector3f translate, Quaternionf pose, Quaternionf cameraOrientation, LivingEntity entity, CallbackInfo ci){
        MultiBufferSource.getInstance().endBatch();
    }
}
