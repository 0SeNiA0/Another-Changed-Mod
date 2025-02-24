package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.GuiSpriteManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.Latex;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(Gui.class)
public abstract class MixinGui {

    @Shadow @Final
    private Minecraft minecraft;

    @Unique
    private static final ResourceLocation achanged$container = AChanged.resourceLoc("container");

    @Unique
    private TransfurType achanged$transfurType;
    @Unique
    private GuiSpriteManager achanged$sprites;
    @Unique
    private final float[] achanged$hsb = new float[3];

    /**
     * Update the current transfurType.
     */
    @Inject(at = @At("HEAD"), method = "renderHearts")
    private void onRenderHearts(GuiGraphics guiGraphics, Player player, int x, int y, int height, int offsetHeartIndex, float maxHealth, int currentHealth, int displayHealth, int absorptionAmount, boolean renderHighlight, CallbackInfo ci){
        achanged$transfurType = TransfurManager.getTransfurType(player);
        if(achanged$sprites == null) achanged$sprites = minecraft.getGuiSprites();
    }

    /**
     * Swap heart sprite.
     */
    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blitSprite(Lnet/minecraft/resources/ResourceLocation;IIII)V"),
            method = "renderHeart")
    private void onRenderHeart(GuiGraphics instance, ResourceLocation sprite, int x, int y, int width, int height, Operation<Void> original, @Local(argsOnly = true) Gui.HeartType type){
        if(achanged$transfurType == null) {
            original.call(instance, sprite, x, y, width, height);
            return;
        }

        if(type == Gui.HeartType.CONTAINER){
            int color = achanged$transfurType.getPrimaryColor();
            Color.RGBtoHSB(FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color), achanged$hsb);
            if(achanged$hsb[2] < .25){
                color = Color.HSBtoRGB(0, 0, (1 - achanged$hsb[2]) * .4f);
                original.call(instance, sprite, x, y, width, height);
                instance.blit(x, y, 0, width, height, achanged$sprites.getSprite(achanged$container), FastColor.ARGB32.red(color) / 255f, FastColor.ARGB32.green(color) / 255f, FastColor.ARGB32.blue(color) / 255f, 1);
                return;
            }
        }

        if(type != Gui.HeartType.NORMAL) {
            original.call(instance, sprite, x, y, width, height);
            return;
        }

        ResourceLocation spriteLoc = achanged$transfurType.id.withSuffix("/" + sprite.getPath().replace("hud/heart/", ""));

        if(achanged$sprites.getSprite(spriteLoc).contents().name().getPath().equals("missingno")) {
            spriteLoc = (achanged$transfurType.latex == Latex.WHITE ? TransfurRegistry.WHITE_LATEX_WOLF_M_TF : TransfurRegistry.DARK_LATEX_WOLF_M_TF).getId().withSuffix("/" + sprite.getPath().replace("hud/heart/", ""));
        }

        if(achanged$sprites.getSprite(spriteLoc).contents().name().getPath().equals("missingno")) {
            original.call(instance, sprite, x, y, width, height);
            return;
        }

        original.call(instance, spriteLoc, x, y, width, height);
    }
}