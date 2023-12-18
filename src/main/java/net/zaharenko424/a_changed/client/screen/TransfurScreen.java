package net.zaharenko424.a_changed.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.ServerboundTransfurChoicePacket;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class TransfurScreen extends Screen {

    int time=200;

    public TransfurScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        int centerX=width/2;
        int centerY=height/2;
        addRenderableWidget(Button.builder(Component.translatable("misc.a_changed.transfur"), button ->{
            minecraft.setScreen(null);
            sendPacket(true);
        }).bounds(centerX-65,centerY,80,20).build());
        addRenderableWidget(Button.builder(Component.translatable("misc.a_changed.transfur_die"), button ->{
            minecraft.setScreen(null);
            sendPacket(false);
        }).bounds(centerX+25,centerY,40,20).build());
    }

    private void sendPacket(boolean becomeTransfur){
        PacketHandler.INSTANCE.sendToServer(new ServerboundTransfurChoicePacket(becomeTransfur));
    }

    @Override
    public boolean mouseClicked(double p_94695_, double p_94696_, int p_94697_) {
        return super.mouseClicked(p_94695_, p_94696_, p_94697_);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(font,"Time left: "+time,width/2,height/2-10,16777215);
    }

    @Override
    public void tick() {
        if(time==0){
            minecraft.setScreen(null);
            sendPacket(false);
        }
        time--;
    }

    @Override
    public void onClose() {
        sendPacket(false);
    }
}