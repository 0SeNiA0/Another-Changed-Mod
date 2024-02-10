package net.zaharenko424.a_changed.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.DNAExtractorEntity;
import net.zaharenko424.a_changed.menu.DNAExtractorMenu;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorScreen extends AbstractContainerScreen<DNAExtractorMenu> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/dna_extractor");
    private final DNAExtractorEntity extractor;

    public DNAExtractorScreen(DNAExtractorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        extractor = pMenu.getEntity();
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 166);

        if(extractor.hasAnyProgress()){
            guiGraphics.blit(TEXTURE, leftPos + 82, topPos + 36, 0, 176, 0, 16, 17, 256, 166);
        }

        int energy = extractor.getEnergy();
        int capacity = extractor.getCapacity();
        if(energy > 0)
            guiGraphics.fill(leftPos + 16,  topPos + 70 - (55 * energy / capacity),  leftPos + 36,  topPos + 70, -11605381);

        guiGraphics.drawString(font, "EU: ", leftPos + 75, topPos + 72, 4210752, false);
        String str = Utils.formatEnergy(energy);
        guiGraphics.drawString(font, str, leftPos + 105 - font.width(str) / 2, topPos + 72, 4210752, false);
        guiGraphics.drawString(font, "/" + Utils.formatEnergy(capacity), leftPos + 120, topPos + 72, 4210752, false);
    }
}