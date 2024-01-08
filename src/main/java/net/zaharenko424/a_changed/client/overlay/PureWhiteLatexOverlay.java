package net.zaharenko424.a_changed.client.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.registry.TransfurRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

public class PureWhiteLatexOverlay {
    private static final ResourceLocation LATEX_OVERLAY = AChanged.textureLoc("overlay/pure_white_latex");
    private static boolean applyOverlay = false;

    public static final IGuiOverlay OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Player player=gui.getMinecraft().player;
        if(player == null) return;
        if(!player.isDeadOrDying()){
            if(!TransfurManager.isTransfurred(player) || TransfurManager.getTransfurType(player) != TransfurRegistry.PURE_WHITE_LATEX_WOLF_TF.get()) {
                applyOverlay = false;
                return;
            }
            applyOverlay = true;
        }
        if(!applyOverlay) return;
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        guiGraphics.blit(LATEX_OVERLAY,0,0,0,0,0,screenWidth,screenHeight,screenWidth,screenHeight);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
    };
}