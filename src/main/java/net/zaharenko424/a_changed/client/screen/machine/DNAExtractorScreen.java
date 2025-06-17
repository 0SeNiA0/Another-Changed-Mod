package net.zaharenko424.a_changed.client.screen.machine;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machine.DNAExtractorEntity;
import net.zaharenko424.a_changed.menu.machine.DNAExtractorMenu;
import net.zaharenko424.a_changed.network.packets.ServerboundProcessingMachinePacket;
import org.jetbrains.annotations.NotNull;

public class DNAExtractorScreen extends AbstractMachineScreen<DNAExtractorEntity, DNAExtractorMenu> {

    public static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/dna_extractor");

    public DNAExtractorScreen(DNAExtractorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, true);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        drawEnergySidebar(guiGraphics, entity.getEnergyConsumption(), 256, pPartialTick);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 166);

        int progress = entity.getProgress();
        if(entity.getProgress() > 0){
            guiGraphics.blit(TEXTURE, leftPos + 76, topPos + 31, 0, 176, 0, 24 * progress / entity.getRecipeProcessingTime(), 25, 256, 166);
        }

        if(!entity.isEnabled()){
            guiGraphics.blit(TEXTURE, leftPos + 149, topPos + 60, 0, 176, 26, 20, 20, 256, 166);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        guiGraphics.drawWordWrap(font, title, titleLabelX, titleLabelY, 50, 4210752);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(areaClicked(leftPos + 149, leftPos + 168, topPos + 60, topPos + 79, mouseX, mouseY)){
            PacketDistributor.sendToServer(new ServerboundProcessingMachinePacket(entity.getBlockPos(), 0, entity.isEnabled() ? 0 : 1));
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
            //enable / disable
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }
}