package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.AbstractRadialMenuScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundAbilityPacket;

import java.awt.*;
import java.util.List;

public abstract class SoundAbilityScreen extends AbstractRadialMenuScreen {

    protected final List<Pair<String, SoundEvent>> sounds;

    protected SoundAbilityScreen(Component pTitle, List<Pair<String, SoundEvent>> sounds, int radius, int innerRadius) {
        super(pTitle, radius, innerRadius);
        this.sounds = sounds;
    }

    protected abstract DeferredHolder<Ability, ? extends Ability> ability();

    @Override
    protected void init() {
        super.init();
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        buttons.clear();

        int amount = sounds.size();
        int sizeDeg = 360 / amount;
        //int i = 90 - sizeDeg / 2;
        int i = 90 + sizeDeg / 2;

        for(int ii = 0; ii < amount; ii++){
            addRadialButton(i, i += sizeDeg, halfWidth, halfHeight);
        }
    }

    @Override
    protected int buttonColor(int button) {
        return Color.ORANGE.getRGB();
    }

    @Override
    protected void renderIcon(GuiGraphics guiGraphics, int x, int y, float partialTick, int button) {
        guiGraphics.drawCenteredString(minecraft.font, sounds.get(button).left(), x + 16, y + 12, Color.GREEN.getRGB());
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(super.mouseClicked(pMouseX, pMouseY, pButton)) return true;
        if(selectedButton != -1){
            PacketDistributor.SERVER.noArg().send(new ServerboundAbilityPacket(ability().getId(),
                    new FriendlyByteBuf(Unpooled.wrappedBuffer(new byte[]{(byte) selectedButton}))));

            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) {
            minecraft.setScreen(null);
            return;
        }

        if(!TransfurHandler.nonNullOf(minecraft.player).hasAbility(ability())){
            minecraft.setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}