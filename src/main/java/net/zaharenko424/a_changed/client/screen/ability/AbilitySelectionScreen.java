package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.AbstractRadialMenuScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.TransfurType;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class AbilitySelectionScreen extends AbstractRadialMenuScreen {

    private TransfurType transfurType;
    private Ability selected;

    public AbilitySelectionScreen() {
        super(Component.empty());
    }

    @Override
    protected int buttonOffsetDeg() {
        return 4;
    }

    @Override
    protected void init() {
        if(!TransfurManager.isTransfurred(minecraft.player)){//TMP potentially remove this in future if non tf players will have more abilities
            minecraft.setScreen(null);
            return;
        }
        transfurType = TransfurManager.getTransfurType(minecraft.player);

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        int radius = 100;
        int innerRadius = radius - 40;

        buttons.clear();

        List<? extends Ability> abilities1 = transfurType.abilities;
        int amount = abilities1.size();

        if(amount == 0) {
            minecraft.setScreen(null);
            return;
        }
        if(amount == 1) {
            minecraft.setScreen(abilities1.get(0).getScreen(minecraft.player));
            return;
        }
        selected = TransfurHandler.nonNullOf(minecraft.player).getSelectedAbility();
        currentlyActive = abilities1.indexOf(selected);

        int sizeDeg = 360 / amount;
        //int i = 90 - sizeDeg / 2;
        int i = 90 + sizeDeg / 2;

        for(int ii = 0; ii < amount; ii++){
            addRadialButton(i, i += sizeDeg, radius, innerRadius, halfWidth, halfHeight);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        RadialButton button;
        int halfWidth = width / 2;
        int halfHeight = height / 2;
        final int radius = 80;
        float minRad, maxRad;
        float radCenter;
        int x, y;
        for(int i = 0; i < buttons.size(); i++){
            button = buttons.get(i);
            minRad = button.radMin();
            maxRad = button.radMax();
            if(minRad > maxRad) maxRad += 2 * Mth.PI;//TMP temporary fix. save clamped & non clamped rad values in buttons?
            radCenter = (maxRad - minRad) / 2 + minRad;
            x = (int) (Mth.cos(radCenter) * radius + halfWidth);
            y = (int) (Mth.sin(radCenter) * radius + halfHeight);
            transfurType.abilities.get(i).drawIcon(minecraft.player, guiGraphics, x - 16, y - 16, false);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int pButton) {
        if(super.mouseClicked(mouseX, mouseY, pButton)) return true;
        if(selectedButton == -1) return false;

        if(selectedButton == currentlyActive){
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Screen screen = selected.getScreen(minecraft.player);
            if(screen != null) minecraft.setScreen(screen);
            return true;
        }

        if(pButton == GLFW.GLFW_MOUSE_BUTTON_RIGHT){
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            Screen screen = transfurType.abilities.get(selectedButton).getScreen(minecraft.player);
            if(screen != null) minecraft.setScreen(screen);
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
}