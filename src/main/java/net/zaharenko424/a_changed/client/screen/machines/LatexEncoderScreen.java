package net.zaharenko424.a_changed.client.screen.machines;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.entity.block.machines.LatexEncoderEntity;
import net.zaharenko424.a_changed.menu.machines.LatexEncoderMenu;
import net.zaharenko424.a_changed.network.packets.ServerboundLatexEncoderScreenPacket;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import org.jetbrains.annotations.ApiStatus;
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
        drawEnergySidebar(guiGraphics, 96, 256, pPartialTick);
        guiGraphics.blit(TEXTURE, leftPos, topPos, 175, 165, 0, 0, 175, 165, 256, 166);

        int progress = entity.getProgress();
        if(progress > 0)
            guiGraphics.blit(TEXTURE, leftPos + 81, topPos + 28, 0,
                    176, 0, 42 * progress / LatexEncoderEntity.MAX_PROGRESS, 24, 256, 166);

        Gender gender = entity.getSelectedGender();
        if(gender != Gender.NONE)
            guiGraphics.blit(TEXTURE, leftPos + (gender.ordinal() == 0 ? 129 : 149), topPos + 61, 0,
                    176, 24, 18, 18, 256, 166);

        if(!entity.isEnabled())
            guiGraphics.blit(TEXTURE,  leftPos + 17, topPos + 34, 0,
                    194, 24, 20, 20, 256, 166);
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
        if(areaClicked(leftPos + 17, leftPos + 36, topPos + 34, topPos + 53, mouseX, mouseY)){
            buttonClicked = true;
            sendDataPacket(1, entity.isEnabled() ? 0 : 1);
            //enable / disable
        }

        if(areaClicked(leftPos + 131, leftPos + 144, topPos + 63, topPos + 76, mouseX, mouseY)){
            buttonClicked = true;
            sendDataPacket(0, gender == Gender.FEMALE ? 2 : 0);
            //button F
        }

        if(areaClicked(leftPos + 151, leftPos + 164, topPos + 63, topPos + 76, mouseX, mouseY)){
            buttonClicked = true;
            sendDataPacket(0, gender == Gender.MALE ? 2 : 1);
            //button M
        }

        if(buttonClicked){
            minecraft.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, pButton);
    }

    @ApiStatus.Internal
    public void sendDataPacket(int index, int data){
        PacketDistributor.SERVER.noArg().send(new ServerboundLatexEncoderScreenPacket(entity.getBlockPos(), index, data));
    }
}