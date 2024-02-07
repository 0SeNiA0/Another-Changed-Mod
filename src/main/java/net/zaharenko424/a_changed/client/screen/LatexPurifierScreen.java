package net.zaharenko424.a_changed.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.LatexPurifierEntity;
import net.zaharenko424.a_changed.menu.LatexPurifierMenu;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class LatexPurifierScreen extends AbstractContainerScreen<LatexPurifierMenu> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/latex_purifier");
    private final LatexPurifierEntity entity;

    public LatexPurifierScreen(LatexPurifierMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        entity = menu.getEntity();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(guiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 256);

        int progress = entity.getProgress();
        if(progress > 0)
            guiGraphics.blit(TEXTURE, leftPos + 80, topPos + 35, 176, 0, 22 * progress / LatexPurifierEntity.MAX_PROGRESS, 15);

        int energy = entity.getEnergy();
        int capacity = entity.getCapacity();
        if(energy > 0)
            guiGraphics.fill(leftPos + 16,  topPos + 70 - (55 * energy / capacity),  leftPos + 36,  topPos + 70, -11605381);

        guiGraphics.drawString(font, "EU: ", leftPos + 75, topPos + 72, 4210752, false);
        String str = Utils.formatEnergy(energy);
        guiGraphics.drawString(font, str, leftPos + 105 - font.width(str) / 2, topPos + 72, 4210752, false);
        guiGraphics.drawString(font, "/" + Utils.formatEnergy(capacity), leftPos + 120, topPos + 72, 4210752, false);

    }
}