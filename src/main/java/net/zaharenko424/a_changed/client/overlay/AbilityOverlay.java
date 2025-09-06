package net.zaharenko424.a_changed.client.overlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.ClientConfig;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.ability.api.AbilityHolder;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.util.AbilityUtils;

public class AbilityOverlay {

    private static Ability lastSelected;

    public static final LayeredDraw.Layer OVERLAY = (guiGraphics, partialTick) -> {
        Player player = Minecraft.getInstance().player;
        TransfurHandler handler = TransfurHandler.nonNullOf(player);
        if(!handler.isTransfurred() && ClientConfig.HIDE_GRAB_ABILITY_WHEN_NON_TF.getAsBoolean()) return;

        AbilityHolder holder = AbilityUtils.of(player);

        if(!player.isDeadOrDying()) lastSelected = holder.getSelectedAbility();
        if(lastSelected == null || lastSelected.isPassive()) return;

        int screenHeight = guiGraphics.guiHeight();
        lastSelected.drawIcon(player, guiGraphics, guiGraphics.guiWidth() / 16, screenHeight - screenHeight / 4, true);
    };
}