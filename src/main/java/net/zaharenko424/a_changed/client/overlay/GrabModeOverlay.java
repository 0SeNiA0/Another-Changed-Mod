package net.zaharenko424.a_changed.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

import static net.zaharenko424.a_changed.client.screen.GrabModeSelectionScreen.*;
import static net.zaharenko424.a_changed.client.screen.WantToBeGrabbedScreen.nope;
import static net.zaharenko424.a_changed.client.screen.WantToBeGrabbedScreen.yes;

public class GrabModeOverlay {

    public static final IGuiOverlay OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Player player = Minecraft.getInstance().player;
        ResourceLocation toBlit;
        if(TransfurManager.isTransfurred(player)){
            if(TransfurManager.isOrganic(player)) return;
            toBlit = switch(TransfurManager.getGrabMode(player)){
                case ASSIMILATE -> assimilate;
                case REPLICATE -> replicate;
                case FRIENDLY -> friendly;
                case NONE -> none;
            };
        } else toBlit = TransfurManager.wantsToBeGrabbed(player) ? yes : nope;

        guiGraphics.blit(toBlit,  screenWidth / 16,  screenHeight - screenHeight / 4, 0, 0, 32, 32, 32, 32);
    };
}