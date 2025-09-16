package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.ability.network.packets.BidirectionalAbilityPacket;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.util.AbilityUtils;
import net.zaharenko424.cmrs.api.MatrixStack;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.RadialButton;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class SoundAbilityScreen extends Screen implements MouseMoveListener {

    protected final List<RadialButton> buttons;

    protected SoundAbilityScreen(Component pTitle, List<Pair<String, SoundEvent>> sounds, int radius, int thickness) {
        super(pTitle);

        int amount = sounds.size();
        buttons = new ArrayList<>(amount);

        float sizeRad = Mth.TWO_PI / amount;
        float off = Mth.DEG_TO_RAD * 4;

        RadialButton button;
        for(int i = 0; i < amount; i++){
            button = makeButton(sounds.get(i).first(), i, radius, thickness)
                    .setRotation(Mth.HALF_PI + sizeRad * i + off).setSize(sizeRad - off * 2, Mth.DEG_TO_RAD * 8);
            button.rebuildMesh();
            buttons.add(button);
        }
    }

    protected RadialButton makeButton(String name, int index, int radius, int thickness){
        return new RadialButton().setRadius(radius).setThickness(thickness, 4)
                .setOutlineColorFunc(button -> button.isHovering() ? Color.ORANGE.getRGB() : -14236)
                .setOnClick((button ,click) -> click(index))
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .02f, .02f))
                .setRenderIcon((button, graphics, x, y) -> {
                    PoseStack stack = graphics.pose();
                    MatrixStack.push(stack);
                    graphics.drawCenteredString(minecraft.font, name, x.intValue(), y.intValue(), Color.GREEN.getRGB());
                    MatrixStack.pop(stack);
                })
                .setExtendClickAreaOutside(true).setExtendClickAreaInside(true);
    }

    protected abstract DeferredHolder<Ability, ? extends Ability> ability();

    @Override
    protected void init() {
        super.init();
        int halfWidth = width / 2;
        int halfHeight = height / 2;

        for(RadialButton button : buttons) {
            button.setOrigin(halfWidth, halfHeight, 0);
            addRenderableWidget(button);
        }
    }

    protected boolean click(int soundIndex){
        PacketDistributor.sendToServer(new BidirectionalAbilityPacket(ability(),
                buf -> buf.writeByte(soundIndex), 1));

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) {
            minecraft.setScreen(null);
            return;
        }

        if(!AbilityUtils.hasAbility(ability(), minecraft.player)) minecraft.setScreen(null);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}