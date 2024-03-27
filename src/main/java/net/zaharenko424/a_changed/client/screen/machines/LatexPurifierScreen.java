package net.zaharenko424.a_changed.client.screen.machines;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.LatexPurifierEntity;
import net.zaharenko424.a_changed.menu.machines.LatexPurifierMenu;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierScreen extends AbstractMachineScreen<LatexPurifierEntity, LatexPurifierMenu> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/latex_purifier");

    public LatexPurifierScreen(LatexPurifierMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, true);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(guiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        drawEnergySidebar(guiGraphics, 48, 256, pPartialTick);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 166);

        int progress = entity.getProgress();
        if(progress > 0)
            guiGraphics.blit(TEXTURE, leftPos + 80, topPos + 35, 0, 176, 0, 22 * progress / LatexPurifierEntity.MAX_PROGRESS, 15, 256, 166);
    }
}