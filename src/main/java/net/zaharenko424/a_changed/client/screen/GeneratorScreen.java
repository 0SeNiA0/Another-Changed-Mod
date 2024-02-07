package net.zaharenko424.a_changed.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.GeneratorEntity;
import net.zaharenko424.a_changed.menu.GeneratorMenu;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/generator");
    private final GeneratorEntity generatorEntity;

    public GeneratorScreen(GeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        generatorEntity = pMenu.getGeneratorEntity();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        super.render(guiGraphics, mouseX, mouseY, pPartialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 256);

        int burnTicks = generatorEntity.getBurnTicks();
        if(burnTicks > 0){
            int offset = 14 * burnTicks / generatorEntity.getMaxBurnTicks();
            int y = 14 - offset;
            guiGraphics.blit(TEXTURE, leftPos + 45, topPos + 57 + y, 177, y, 14, offset);
        }

        int energy = generatorEntity.getEnergy();
        int capacity = generatorEntity.getCapacity();
        if(energy > 0)
            guiGraphics.fill(leftPos + 139,  topPos + 70 - (55 * energy / capacity),  leftPos + 159,  topPos + 70, -11605381);

        guiGraphics.drawString(font, "EU: ", leftPos + 75, topPos + 72, 4210752, false);
        String str = Utils.formatEnergy(energy);
        guiGraphics.drawString(font, str, leftPos + 105 - font.width(str) / 2, topPos + 72, 4210752, false);
        guiGraphics.drawString(font, "/" + Utils.formatEnergy(capacity), leftPos + 120, topPos + 72, 4210752, false);
    }
}