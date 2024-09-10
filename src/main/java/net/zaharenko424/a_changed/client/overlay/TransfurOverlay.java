package net.zaharenko424.a_changed.client.overlay;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

public class TransfurOverlay {

    private static final ResourceLocation TRANSFUR_OVERLAY = AChanged.textureLoc("overlay/transfur");
    private static float progress = 0;
    private static int primaryColor;
//TODO make better looking overlay texture
    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, partialTick) -> {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if(!player.isDeadOrDying()){
            if(TransfurManager.isTransfurred(player) || TransfurManager.getTransfurProgress(player) == 0) return;
            progress = TransfurManager.getTransfurProgress(player);
            primaryColor = TransfurManager.getTransfurType(player).getPrimaryColor();
        }

        guiGraphics.drawString(minecraft.font, "Transfur progress: " + progress, 10, 10, 16777215);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        guiGraphics.setColor((0xFF & (primaryColor >> 16)) / 255f,
                (0xFF & (primaryColor >> 8)) / 255f,
                (0xFF & primaryColor) / 255f,
                TransfurManager.getTransfurProgress(player) / TransfurManager.TRANSFUR_TOLERANCE);

        Window window = minecraft.getWindow();
        int screenWidth = window.getScreenWidth();
        int screenHeight = window.getScreenHeight();
        guiGraphics.blit(TRANSFUR_OVERLAY,0,0,-90,0,0, screenWidth, screenHeight, screenWidth, screenHeight);
        RenderSystem.disableBlend();
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1,1,1,1);
    };
}