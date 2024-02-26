package net.zaharenko424.a_changed.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.network.packets.grab.ServerboundWantToBeGrabbedPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

public class WantToBeGrabbedScreen extends AbstractRadialMenuScreen {

    public static final ResourceLocation yes = AChanged.textureLoc("gui/want_to_be_grabbed");
    public static final ResourceLocation nope = AChanged.textureLoc("gui/dont_want_to_be_grabbed");

    public WantToBeGrabbedScreen() {
        super(Component.empty());
    }

    @Override
    protected int buttonOffsetDeg() {
        return 6;
    }

    @Override
    protected void init() {
        AChanged.LOGGER.warn("wants to be grabbed "+ GrabCapability.nonNullOf(minecraft.player).wantsToBeGrabbed());
        currentlyActive = TransfurManager.wantsToBeGrabbed(minecraft.player) ? 0 : 1;

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        int radius = 100;
        int innerRadius = radius - 40;

        buttons.clear();
        addRadialButton(90, 270, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(270, 450, radius, innerRadius, halfWidth, halfHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        guiGraphics.blit(yes, width / 2 - 95, height / 2 - 16, 0, 0, 32, 32, 32, 32);
        guiGraphics.blit(nope, width / 2 - 32 + 95, height / 2 - 16, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(super.mouseClicked(mouseX, mouseY, pButton)) return true;
        if(selectedButton == -1 || selectedButton == currentlyActive) return false;
        currentlyActive = selectedButton;
        PacketDistributor.SERVER.noArg().send(new ServerboundWantToBeGrabbedPacket(currentlyActive == 0));
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.GRAB_MODE_KEY.getKey().getValue())) Minecraft.getInstance().setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}