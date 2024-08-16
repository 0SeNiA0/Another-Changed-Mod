package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.AbstractRadialMenuScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.List;

public class AbilitySelectionScreen extends AbstractRadialMenuScreen {

    private static final int radius = 100;
    private static final int innerRadius = radius - 40;

    private TransfurType transfurType;
    private Ability selected;

    public AbilitySelectionScreen() {
        super(Component.empty(), radius, innerRadius);
    }

    @Override
    protected void init() {
        super.init();
        if(!TransfurManager.isTransfurred(minecraft.player)){//TMP potentially remove this in future if non tf players will have more abilities
            minecraft.setScreen(null);
            return;
        }
        transfurType = TransfurManager.getTransfurType(minecraft.player);

        buttons.clear();

        List<? extends Ability> abilities = transfurType.abilities;
        int amount = abilities.size();

        if(amount == 0) {
            minecraft.setScreen(null);
            return;
        }
        if(amount == 1 && abilities.get(0).hasScreen()) {
            minecraft.setScreen(abilities.get(0).getScreen(minecraft.player));
            return;
        }
        selected = TransfurHandler.nonNullOf(minecraft.player).getSelectedAbility();
        currentlyActive = abilities.indexOf(selected);

        int sizeDeg = 360 / amount;
        //int i = 90 - sizeDeg / 2;
        int i = 90 + sizeDeg / 2;

        for(int ii = 0; ii < amount; ii++){
            addRadialButton(i, i += sizeDeg, radius, innerRadius, halfWidth, halfHeight);
        }
    }

    @Override
    protected int buttonColor(int button) {
        if(button == selectedButton && !transfurType.abilities.get(selectedButton).isActive()) return Color.ORANGE.getRGB();
        return super.buttonColor(button);
    }

    @Override
    protected void renderIcon(GuiGraphics guiGraphics, int x, int y, float partialTick, int button) {
        transfurType.abilities.get(button).drawIcon(minecraft.player, guiGraphics, x, y, false);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(super.mouseClicked(mouseX, mouseY, pButton)) return true;
        if(selectedButton == -1) return false;

        if(selectedButton == currentlyActive){
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if(selected.hasScreen()) minecraft.setScreen(selected.getScreen(minecraft.player));
            return true;
        }

        Ability selected = transfurType.abilities.get(selectedButton);

        if(pButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT || !selected.isActive()){
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            if(selected.hasScreen()) minecraft.setScreen(selected.getScreen(minecraft.player));
            return true;
        }

        if(!transfurType.abilities.get(selectedButton).isActive()) return true;
        currentlyActive = selectedButton;

        PacketDistributor.SERVER.noArg().send(
                new ServerboundSelectAbilityPacket(AbilityUtils.abilityIdOf(transfurType.abilities.get(currentlyActive))));

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) {
            minecraft.setScreen(null);
            return;
        }
        if(!TransfurManager.isTransfurred(minecraft.player)) {//TMP a bit of hardcoding until more abilities are added to non tf players
            minecraft.setScreen(new GrabAbilityPlayerScreen());
            return;
        }
        if(TransfurManager.getTransfurType(minecraft.player) != transfurType) {
            init();
        } else {
            Ability newSelected = TransfurHandler.nonNullOf(minecraft.player).getSelectedAbility();
            if (newSelected != selected) {
                selected = newSelected;
                currentlyActive = transfurType.abilities.indexOf(selected);
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}