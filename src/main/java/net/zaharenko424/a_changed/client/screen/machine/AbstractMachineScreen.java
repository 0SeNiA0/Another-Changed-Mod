package net.zaharenko424.a_changed.client.screen.machine;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machine.AbstractMachineEntity;
import net.zaharenko424.a_changed.menu.machine.AbstractMachineMenu;
import net.zaharenko424.a_changed.util.Utils;
import net.zaharenko424.cmrs.client.gui.widget.WidgetHelper;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public abstract class AbstractMachineScreen <T extends AbstractMachineEntity<?, ?>, C extends AbstractMachineMenu<T>> extends AbstractContainerScreen<C> {

    public static final ResourceLocation SIDEBAR = AChanged.textureLoc("gui/energy_sidebar");
    protected final T entity;
    protected boolean sideBarActive;
    protected static boolean open;
    protected int pos = open ? 60 : 0;
    protected int posO = pos;

    public AbstractMachineScreen(C menu, Inventory playerInventory, Component title, boolean sideBarActive) {
        super(menu, playerInventory, title);
        entity = menu.getEntity();
        this.sideBarActive = sideBarActive;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!sideBarActive) return super.mouseClicked(mouseX, mouseY, button);
        if(areaClicked(leftPos - pos - 14, leftPos, topPos + 4, topPos + 82, mouseX, mouseY)){
            open = !open;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void containerTick() {
        if(!sideBarActive) return;
        if(posO != pos) posO = pos;
        if(open && pos < 60) pos += Math.min(8, 60 - pos);
        if(!open && pos > 0) pos -= Math.min(8, pos);
    }

    protected boolean areaClicked(float minX, float maxX, float minY, float maxY, double mouseX, double mouseY){
        return mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY;
    }

    protected void drawEnergySidebar(@NotNull GuiGraphics guiGraphics, int usage, int maxIn, float partialTick){
        float lerpPos = posO == pos ? pos : Mth.lerp(partialTick, posO, pos);

        WidgetHelper.blit(SIDEBAR, guiGraphics.pose(), leftPos - 14 - lerpPos, topPos + 4, 14 + Math.min(lerpPos, 60), 78,
                0, 0, 14 + Math.min(lerpPos, 60), 78, 128, 96);

        int energy = entity.getEnergy();
        int capacity = entity.getCapacity();

        if(energy > 0) {
            float size = 56f * energy / (float) capacity;

            WidgetHelper.fill(guiGraphics.pose(), leftPos - 4 - lerpPos, topPos + 12 + (56 - size), leftPos - lerpPos + 1 + Math.min(lerpPos, 16), topPos + 68, 0, Color.GREEN.getRGB());
            guiGraphics.bufferSource().endLastBatch();
        }

        WidgetHelper.blit(SIDEBAR, guiGraphics.pose(), leftPos - 4 - lerpPos, topPos + 12, 5 + Math.min(lerpPos, 16), 56,
                74, 8, 5 + Math.min(lerpPos, 16), 56, 128, 96);

        if(lerpPos < 15) return;

        float x = leftPos - lerpPos + 20;
        if(lerpPos > 20){
            int offset = 0;
            if(usage > 0) {
                offset = 25;
                guiGraphics.drawString(font, "Usage:", x, topPos + 15, 4210752, false);
                guiGraphics.drawString(font, usage + " EU/t", x, topPos + 25, 4210752, false);
            }
            if(maxIn > 0) {
                guiGraphics.drawString(font, "Max in:", x, topPos + 15 + offset, 4210752, false);
                guiGraphics.drawString(font, maxIn + " EU", x, topPos + 25 + offset, 4210752, false);
            }
        }

        guiGraphics.drawString(font, "EU: ", x - 30, topPos + 70, 4210752, false);
        String str = Utils.formatEnergy(energy);
        guiGraphics.drawString(font, str, x - font.width(str) / 2f, topPos + 70, 4210752, false);
        guiGraphics.drawString(font, "/" + Utils.formatEnergy(capacity), x + 15, topPos + 70, 4210752, false);
    }
}