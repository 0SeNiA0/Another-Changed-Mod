package net.zaharenko424.a_changed.client.screen.machine;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machine.CapacitorEntity;
import net.zaharenko424.a_changed.menu.machine.CapacitorMenu;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.cmrs.client.gui.widget.WidgetHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CapacitorScreen extends AbstractMachineScreen<CapacitorEntity, CapacitorMenu> {

    private static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/capacitor");

    public CapacitorScreen(CapacitorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, false);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(guiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 166);

        int energy = entity.getEnergy();
        int capacity = entity.getCapacity();

        if(energy > 0) {
            float size = 56f * energy / (float) capacity;

            WidgetHelper.fill(guiGraphics.pose(), leftPos + 139, topPos + 15 + (56 - size), leftPos + 160, topPos + 71, 0, Color.GREEN.getRGB());
            guiGraphics.bufferSource().endLastBatch();
        }

        WidgetHelper.blit(AbstractMachineScreen.SIDEBAR, guiGraphics.pose(), leftPos + 139, topPos + 15, 21, 56,
                74, 8, 21, 56, 128, 96);

        guiGraphics.drawString(font, "EU: ", leftPos + 75, topPos + 72, 4210752, false);
        String str = Utils.formatEnergy(energy);
        guiGraphics.drawString(font, str, leftPos + 105 - font.width(str) / 2, topPos + 72, 4210752, false);
        guiGraphics.drawString(font, "/" + Utils.formatEnergy(capacity), leftPos + 120, topPos + 72, 4210752, false);
    }
}