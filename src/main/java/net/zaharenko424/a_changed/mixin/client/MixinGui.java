package net.zaharenko424.a_changed.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public abstract class MixinGui {

    @Unique
    private int mod$primaryColor;
    @Unique
    private int mod$secondaryColor;

    @Inject(at = @At("HEAD"), method = "renderHeart", cancellable = true)
    private void onRenderHeart(GuiGraphics guiGraphics, Gui.HeartType heartType, int pX, int pY, boolean hardcore, boolean halfHeart, boolean blinking, @NotNull CallbackInfo ci){
        if(heartType != Gui.HeartType.NORMAL && heartType != Gui.HeartType.CONTAINER) return;

        Player player = Minecraft.getInstance().player;
        if(player.isDeadOrDying()){
            if(mod$primaryColor == 0 && mod$secondaryColor == 0) return;
        } else if(!TransfurManager.isTransfurred(Minecraft.getInstance().player)) {
            mod$primaryColor = 0;
            mod$secondaryColor = 0;
            return;
        } else {
            TransfurType transfurType = TransfurManager.getTransfurType(player);
            mod$primaryColor = transfurType.getPrimaryColor();
            mod$secondaryColor = transfurType.getSecondaryColor();
        }
        ci.cancel();

        int tmp = heartType == Gui.HeartType.NORMAL ? mod$primaryColor : mod$secondaryColor;

        guiGraphics.setColor( (0xFF & (tmp >> 16)) / 255f,
                (0xFF & (tmp >> 8)) / 255f,
                (0xFF & tmp) / 255f,
                (0xFF & (tmp >> 24)) / 255f);

        guiGraphics.blit(AChanged.textureLoc(heartType.getSprite(hardcore, blinking != halfHeart, blinking).getPath().replace("hud", "gui")), pX, pY, 0, 0, 9, 9, 9, 9);

        guiGraphics.setColor(1, 1, 1, 1);
    }
}