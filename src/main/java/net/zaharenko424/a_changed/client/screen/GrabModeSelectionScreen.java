package net.zaharenko424.a_changed.client.screen;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.capability.GrabMode;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.network.PacketHandler;
import net.zaharenko424.a_changed.network.packets.grab.ServerboundGrabModePacket;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GrabModeSelectionScreen extends AbstractRadialMenuScreen {

    private static final List<GrabMode> grabMode = List.of(GrabMode.NONE, GrabMode.FRIENDLY, GrabMode.ASSIMILATE, GrabMode.REPLICATE);
    public static final ResourceLocation none = AChanged.textureLoc("gui/grab_none");
    public static final ResourceLocation friendly = AChanged.textureLoc("gui/grab_friendly");
    public static final ResourceLocation assimilate = AChanged.textureLoc("gui/grab_assimilate");
    public static final ResourceLocation replicate = AChanged.textureLoc("gui/grab_replicate");

    public GrabModeSelectionScreen() {
        super(Component.empty());
    }

    @Override
    protected int buttonOffsetDeg() {
        return 4;
    }

    @Override
    protected void init() {
        currentlyActive = grabMode.indexOf(minecraft.player.getCapability(GrabCapability.CAPABILITY).orElseThrow(GrabCapability.NO_CAPABILITY_EXC).grabMode());

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        int radius = 100;
        int innerRadius = radius - 40;

        buttons.clear();
        addRadialButton(45, 135, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(135, 225, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(225, 315, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(315, 405, radius, innerRadius, halfWidth, halfHeight);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        super.render(guiGraphics, mouseX, mouseY, pPartialTick);
        guiGraphics.blit(none, width / 2 - 16, height / 2 - 32 + 95, 0, 0, 32, 32, 32, 32);
        guiGraphics.blit(friendly, width / 2 - 95, height / 2 - 16, 0, 0, 32, 32, 32, 32);
        guiGraphics.blit(assimilate, width / 2 - 16, height / 2 - 95, 0, 0, 32, 32, 32, 32);
        guiGraphics.blit(replicate, width / 2 - 32 + 95, height / 2 - 16, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(super.mouseClicked(mouseX, mouseY, pButton)) return true;
        if(selectedButton == -1 || selectedButton == currentlyActive) return false;
        currentlyActive = selectedButton;
        PacketHandler.INSTANCE.sendToServer(new ServerboundGrabModePacket(grabMode.get(currentlyActive)));
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