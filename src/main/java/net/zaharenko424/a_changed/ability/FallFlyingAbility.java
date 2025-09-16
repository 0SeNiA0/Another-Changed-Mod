package net.zaharenko424.a_changed.ability;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.ability.api.PassiveAbility;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FallFlyingAbility implements PassiveAbility {

    @Override
    public void drawIcon(@NotNull Player player, @NotNull GuiGraphics graphics, int x, int y, boolean overlay) {
        graphics.drawCenteredString(Minecraft.getInstance().font, "Fall Flying", x + 16, y + 12, Color.YELLOW.getRGB());
        //TODO
    }
}