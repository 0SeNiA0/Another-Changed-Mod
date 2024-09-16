package net.zaharenko424.a_changed.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.capability.TransfurHandler;

public class AbilityOverlay {

    private static Ability lastSelected;

    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, partialTick) -> {
        Player player = Minecraft.getInstance().player;

        if(!player.isDeadOrDying()) lastSelected = TransfurHandler.nonNullOf(player).getSelectedAbility();
        if(lastSelected == null) return;

        int screenHeight = guiGraphics.guiHeight();
        lastSelected.drawIcon(player, guiGraphics, guiGraphics.guiWidth() / 16, screenHeight - screenHeight / 4, true);
    };
}