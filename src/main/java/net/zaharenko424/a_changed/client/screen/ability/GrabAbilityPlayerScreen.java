package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.AbstractRadialMenuScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundAbilityPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

public class GrabAbilityPlayerScreen extends AbstractRadialMenuScreen {

    private static final int radius = 100;
    private static final int innerRadius = radius - 40;
    public static final ResourceLocation yes = AChanged.textureLoc("gui/want_to_be_grabbed");
    public static final ResourceLocation nope = AChanged.textureLoc("gui/dont_want_to_be_grabbed");

    public GrabAbilityPlayerScreen() {
        super(Component.empty(), radius, innerRadius);
    }

    @Override
    protected int buttonOffsetDeg() {

        return 6;
    }

    @Override
    protected void init() {
        super.init();
        if(TransfurManager.isTransfurred(minecraft.player)) {
            minecraft.setScreen(new GrabAbilityLatexScreen());
            return;
        }

        currentlyActive = GrabData.dataOf(minecraft.player).wantsToBeGrabbed() ? 0 : 1;

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        buttons.clear();
        addRadialButton(90, 270, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(270, 450, radius, innerRadius, halfWidth, halfHeight);
    }

    @Override
    protected void renderIcon(GuiGraphics guiGraphics, int x, int y, float partialTick, int button) {
        guiGraphics.blit(button == 0 ? yes : nope, x, y, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(super.mouseClicked(mouseX, mouseY, pButton)) return true;
        if(selectedButton == -1 || selectedButton == currentlyActive) return false;
        currentlyActive = selectedButton;

        PacketDistributor.SERVER.noArg().send(new ServerboundAbilityPacket(AbilityRegistry.GRAB_ABILITY.getId(),
                new FriendlyByteBuf(Unpooled.buffer()).writeByte(1).writeBoolean(currentlyActive == 0)));

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) Minecraft.getInstance().setScreen(null);
        if(TransfurManager.isTransfurred(minecraft.player)) {
            minecraft.setScreen(new GrabAbilityLatexScreen());
            return;
        }
        currentlyActive = GrabData.dataOf(minecraft.player).wantsToBeGrabbed() ? 0 : 1;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}