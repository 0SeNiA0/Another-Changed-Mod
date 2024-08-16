package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.AbstractRadialMenuScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundAbilityPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

import java.util.List;

public class GrabAbilityLatexScreen extends AbstractRadialMenuScreen {

    private static final List<GrabMode> grabMode = List.of(GrabMode.NONE, GrabMode.FRIENDLY, GrabMode.ASSIMILATE, GrabMode.REPLICATE);
    private static final int radius = 100;
    private static final int innerRadius = radius - 40;

    public GrabAbilityLatexScreen() {
        super(Component.empty(), radius, innerRadius);
    }

    @Override
    protected void init() {
        super.init();
        if(!TransfurManager.isTransfurred(minecraft.player)) {
            minecraft.setScreen(new GrabAbilityPlayerScreen());
            return;
        }

        currentlyActive = grabMode.indexOf(GrabData.dataOf(minecraft.player).getMode());

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        buttons.clear();
        addRadialButton(45, 135, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(135, 225, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(225, 315, radius, innerRadius, halfWidth, halfHeight);
        addRadialButton(315, 405, radius, innerRadius, halfWidth, halfHeight);
    }

    @Override
    protected void renderIcon(GuiGraphics guiGraphics, int x, int y, float partialTick, int button) {
        guiGraphics.blit(grabMode.get(button).texture, x, y, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(super.mouseClicked(mouseX, mouseY, pButton)) return true;
        if(selectedButton == -1 || selectedButton == currentlyActive) return false;
        currentlyActive = selectedButton;//TODO test -> might have problems when high ping but then again tick() changes it right after anyway

        PacketDistributor.SERVER.noArg().send(new ServerboundAbilityPacket(AbilityRegistry.GRAB_ABILITY.getId(),
                new FriendlyByteBuf(Unpooled.buffer(2)).writeByte(0).writeEnum(grabMode.get(currentlyActive))));

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) {
            minecraft.setScreen(null);
            return;
        }
        if(!TransfurManager.isTransfurred(minecraft.player)) {
            minecraft.setScreen(new GrabAbilityPlayerScreen());
            return;
        }
        currentlyActive = grabMode.indexOf(GrabData.dataOf(minecraft.player).getMode());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}