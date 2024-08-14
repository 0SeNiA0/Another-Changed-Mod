package net.zaharenko424.a_changed.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.overlay.IGuiOverlay;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.capability.TransfurHandler;

public class AbilityOverlay {

    private static Ability lastSelected;

    public static final IGuiOverlay OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        Player player = Minecraft.getInstance().player;
        if(!player.isDeadOrDying()) lastSelected = TransfurHandler.nonNullOf(player).getSelectedAbility();
        if(lastSelected == null) return;

        lastSelected.drawIcon(player, guiGraphics, screenWidth / 16, screenHeight - screenHeight / 4, true);
    };
}