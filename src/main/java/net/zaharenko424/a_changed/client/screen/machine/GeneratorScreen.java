package net.zaharenko424.a_changed.client.screen.machine;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machine.GeneratorEntity;
import net.zaharenko424.a_changed.menu.machine.GeneratorMenu;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class GeneratorScreen extends AbstractContainerScreen<GeneratorMenu> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/generator");
    private final GeneratorEntity generatorEntity;

    public GeneratorScreen(GeneratorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        generatorEntity = pMenu.getEntity();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        super.render(guiGraphics, mouseX, mouseY, pPartialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 166);

        int burnTicks = generatorEntity.getBurnTicks();
        if(burnTicks > 0){
            float offset = 14f * burnTicks / (float) generatorEntity.getMaxBurnTicks();
            float y = 14 - offset;

            WidgetHelper.blit(TEXTURE, guiGraphics.pose(), leftPos + 45, topPos + 57 + y, 14, offset,
                    177, y, 14, offset, 256, 166);
            guiGraphics.blit(TEXTURE, leftPos + 73, topPos + 33, 32, 21,
                    177, 15, 32, 21, 256, 166);
        }

        int energy = generatorEntity.getEnergy();
        int capacity = generatorEntity.getCapacity();

        if(energy > 0) {
            float size = 56f * energy / (float) capacity;

            WidgetHelper.fill(guiGraphics.pose(), leftPos + 113, topPos + 15 + (56 - size), leftPos + 135, topPos + 71, 0, Color.GREEN.getRGB());
            guiGraphics.bufferSource().endLastBatch();
        }

        WidgetHelper.blit(AbstractMachineScreen.SIDEBAR, guiGraphics.pose(), leftPos + 113, topPos + 15, 22, 56,
                74, 8, 21, 56, 128, 96);

        guiGraphics.drawString(font, "EU: ", leftPos + 105, topPos + 72, 4210752, false);
        String str = Utils.formatEnergy(energy);
        guiGraphics.drawString(font, str, leftPos + 135 - font.width(str) / 2, topPos + 72, 4210752, false);
        guiGraphics.drawString(font, "/" + Utils.formatEnergy(capacity), leftPos + 150, topPos + 72, 4210752, false);
    }
}