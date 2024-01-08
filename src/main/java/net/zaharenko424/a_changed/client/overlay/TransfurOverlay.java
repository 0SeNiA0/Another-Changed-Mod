package net.zaharenko424.a_changed.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

public class TransfurOverlay {

    private static final ResourceLocation TRANSFUR_OVERLAY = AChanged.textureLoc("overlay/transfur");
//TODO make better looking overlay texture
    public static final IGuiOverlay OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Player player=gui.getMinecraft().player;
        if(player==null || player.isDeadOrDying()) return;
        if(TransfurManager.isTransfurred(player) || TransfurManager.getTransfurProgress(player)==0) return;
        float progress=TransfurManager.getTransfurProgress(player);
        guiGraphics.drawString(gui.getFont(),"Transfur progress: "+progress,10,10,16777215);
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        guiGraphics.setColor(1,1,1, TransfurManager.getTransfurProgress(player)/TransfurManager.TRANSFUR_TOLERANCE);
        guiGraphics.blit(TRANSFUR_OVERLAY,0,0,-90,0,0,screenWidth,screenHeight,screenWidth,screenHeight);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        guiGraphics.setColor(1,1,1,1);
    };
}