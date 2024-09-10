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
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.client.screen.AbstractRadialMenuScreen;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundAbilityPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.function.Function;

public class GrabEscapeScreen extends AbstractRadialMenuScreen {

    private static final ResourceLocation MOUSE_LEFT = AChanged.textureLoc("gui/grab_escape/mouse_left");
    private static final ResourceLocation MOUSE_RIGHT = AChanged.textureLoc("gui/grab_escape/mouse_right");
    private static final ResourceLocation KEY_LEFT = AChanged.textureLoc("gui/grab_escape/key_left");
    private static final ResourceLocation KEY_RIGHT = AChanged.textureLoc("gui/grab_escape/key_right");
    private static final ResourceLocation KEY_UP = AChanged.textureLoc("gui/grab_escape/key_up");
    private static final ResourceLocation KEY_DOWN = AChanged.textureLoc("gui/grab_escape/key_down");
    private static final ResourceLocation KEY_SPACE = AChanged.textureLoc("gui/grab_escape/key_space");

    private final Type type;
    private int lastClickButton = -1;
    private int clicks = 0;

    public GrabEscapeScreen(RandomSource random) {
        super(Component.empty(), 100, 60);
        Type[] values = Type.values();
        type = values[random.nextInt(values.length)];
    }

    @Override
    protected int buttonOffsetDeg() {
        return type != Type.SPACE ? 6 : 30;
    }

    @Override
    protected void init() {
        if(!TransfurManager.isGrabbed(minecraft.player) || !minecraft.player.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)) {
            minecraft.setScreen(null);
            return;
        }

        super.init();
        switch(type){
            case LEFT_RIGHT_MOUSE, LEFT_RIGHT_KEY -> {
                addRadialButton(90, 270, halfWidth, halfHeight);
                addRadialButton(270, 450, halfWidth, halfHeight);
            }
            case UP_DOWN_KEY -> {
                addRadialButton(180, 360, halfWidth, halfHeight);//UP
                addRadialButton(0, 180, halfWidth, halfHeight);//DOWN
            }
            case SPACE -> addRadialButton(90, 450, halfWidth, halfHeight);
        }
    }

    @Override
    protected void renderIcon(GuiGraphics guiGraphics, int x, int y, float partialTick, int button) {
        guiGraphics.blit(type.buttonIcon.apply(button), x, y, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);

        guiGraphics.drawCenteredString(minecraft.font, Component.translatable("screen.a_changed.grab_escape.time_remaining", minecraft.player.getEffect(MobEffectRegistry.GRABBED_DEBUFF).getDuration() / 20f), halfWidth, halfHeight - 8, Color.RED.getRGB());
        guiGraphics.drawCenteredString(minecraft.font, Component.translatable("screen.a_changed.grab_escape.clicks", clicks, type.clicksRequired), halfWidth, halfHeight + 8, Color.CYAN.getRGB());
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {}

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        if(super.keyPressed(pKeyCode, pScanCode, pModifiers)) return true;
        return handleClick(pKeyCode);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if(super.mouseClicked(pMouseX, pMouseY, pButton)) return true;
        return handleClick(pButton);
    }

    protected boolean handleClick(int button){
        if(type.switchKey1 != -1){
            if(button != type.switchKey0 && button != type.switchKey1) return false;

            if(lastClickButton != -1 && lastClickButton == button) return false;

            lastClickButton = button;
            clicks++;
            currentlyActive = button == type.switchKey0 ? 0 : 1;
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }

        if(button != type.switchKey0) return false;
        clicks++;
        if(currentlyActive != 0) currentlyActive = 0;
        selectedButton = selectedButton == -1 ? 0 : -1;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if(!TransfurManager.isGrabbed(minecraft.player) || !minecraft.player.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)) {
            minecraft.setScreen(null);
            return;
        }

        if(clicks >= type.clicksRequired){
            PacketDistributor.sendToServer(new ServerboundAbilityPacket(AbilityRegistry.GRAB_ABILITY.getId(),
                    new FriendlyByteBuf(Unpooled.buffer(2)).writeByte(2).writeBoolean(true)));
            minecraft.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
            minecraft.setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    enum Type {
        LEFT_RIGHT_MOUSE(InputConstants.MOUSE_BUTTON_LEFT, InputConstants.MOUSE_BUTTON_RIGHT, 15, button -> button == 0 ? MOUSE_LEFT : MOUSE_RIGHT),
        LEFT_RIGHT_KEY(InputConstants.KEY_LEFT, InputConstants.KEY_RIGHT, 15, button -> button == 0 ? KEY_LEFT : KEY_RIGHT),
        UP_DOWN_KEY(InputConstants.KEY_UP, InputConstants.KEY_DOWN, 15, button -> button == 0 ? KEY_UP : KEY_DOWN),
        SPACE(InputConstants.KEY_SPACE, -1, 20, button -> KEY_SPACE);

        final int switchKey0, switchKey1;
        final int clicksRequired;
        final Function<Integer, ResourceLocation> buttonIcon;

        Type(int switchKey0, int switchKey1, int clicksRequired, Function<Integer, ResourceLocation> buttonIcon){
            this.switchKey0 = switchKey0;
            this.switchKey1 = switchKey1;
            this.clicksRequired = clicksRequired;
            this.buttonIcon = buttonIcon;
        }
    }
}