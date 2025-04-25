package net.zaharenko424.a_changed.client.screen.machines;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machine.LatexEncoderEntity;
import net.zaharenko424.a_changed.menu.machines.LatexEncoderMenu;
import net.zaharenko424.a_changed.network.packets.ServerboundProcessingMachinePacket;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.NotNull;

public class LatexEncoderScreen extends AbstractMachineScreen<LatexEncoderEntity, LatexEncoderMenu> {

    public static final ResourceLocation TEXTURE = AChanged.textureLoc("gui/latex_encoder");

    public LatexEncoderScreen(LatexEncoderMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle, true);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(guiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        drawEnergySidebar(guiGraphics, entity.getEnergyConsumption(), 256, pPartialTick);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 166);

        int progress = entity.getProgress();
        if(progress > 0)
            guiGraphics.blit(TEXTURE, leftPos + 67, topPos + 28, 0,
                    176, 0, 42 * progress / entity.getRecipeProcessingTime(), 27, 256, 166);

        Gender gender = entity.getSelectedGender();
        if(gender != Gender.MALE)
            guiGraphics.blit(TEXTURE, leftPos + 123, topPos + 63, 0,
                    gender == Gender.FEMALE ? 176 : 191, 49, 14, 14, 256, 166);

        if(!entity.isEnabled()){
            guiGraphics.blit(TEXTURE, leftPos + 149, topPos + 60, 0, 176, 28, 20, 20, 256, 166);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY) {
        guiGraphics.drawWordWrap(font, title, titleLabelX, titleLabelY, 50, 4210752);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX, this.inventoryLabelY, 4210752, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        boolean buttonClicked = false;
        Gender gender = entity.getSelectedGender();
        if(areaClicked(leftPos + 149, leftPos + 168, topPos + 60, topPos + 79, mouseX, mouseY)){
            buttonClicked = true;
            sendDataPacket(0, entity.isEnabled() ? 0 : 1);
            //enable / disable
        }

        if (!entity.hasRecipe() && areaClicked(leftPos + 121, leftPos + 138, topPos + 61, topPos + 78, mouseX, mouseY)) {
            buttonClicked = true;
            int ordinal = gender.ordinal() + (pButton == InputConstants.MOUSE_BUTTON_LEFT ? 1 : -1);

            if(ordinal < 0) ordinal = 2;
            if(ordinal > 2) ordinal = 0;

            sendDataPacket(1, ordinal);
        }

        if(buttonClicked){
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    protected void sendDataPacket(int index, int data){
        PacketDistributor.sendToServer(new ServerboundProcessingMachinePacket(entity.getBlockPos(), index, data));
    }
}