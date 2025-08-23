package net.zaharenko424.a_changed.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.ClientConfig;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.attachment.TransfurHandler;

public class AbilityOverlay {

    private static Ability lastSelected;

    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, partialTick) -> {
        Player player = Minecraft.getInstance().player;
        TransfurHandler handler = TransfurHandler.nonNullOf(player);
        if(!handler.isTransfurred() && ClientConfig.HIDE_GRAB_ABILITY_WHEN_NON_TF.getAsBoolean()) return;

        if(!player.isDeadOrDying()) lastSelected = handler.getSelectedAbility();
        if(lastSelected == null || !lastSelected.isSelectable()) return;

        int screenHeight = guiGraphics.guiHeight();
        lastSelected.drawIcon(player, guiGraphics, guiGraphics.guiWidth() / 16, screenHeight - screenHeight / 4, true);
    };
}