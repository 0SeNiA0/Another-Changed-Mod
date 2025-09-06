package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.network.packets.BidirectionalAbilityPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.registry.MobEffectRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.RadialButton;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GrabEscapeScreen extends Screen implements MouseMoveListener {

    private static final ResourceLocation MOUSE_LEFT = AChanged.textureLoc("gui/grab_escape/mouse_left");
    private static final ResourceLocation MOUSE_RIGHT = AChanged.textureLoc("gui/grab_escape/mouse_right");

    private static final ResourceLocation KEY_LEFT = AChanged.textureLoc("gui/grab_escape/key_left");
    private static final ResourceLocation KEY_RIGHT = AChanged.textureLoc("gui/grab_escape/key_right");

    private static final ResourceLocation KEY_UP = AChanged.textureLoc("gui/grab_escape/key_up");
    private static final ResourceLocation KEY_DOWN = AChanged.textureLoc("gui/grab_escape/key_down");

    private static final ResourceLocation KEY_SPACE = AChanged.textureLoc("gui/grab_escape/key_space");

    protected final Type type;
    protected final int[] clicks = {0};
    protected final List<RadialButton> buttons = new ArrayList<>(2);

    public GrabEscapeScreen() {
        super(Component.empty());
        Type[] values = Type.values();
        type = values[Minecraft.getInstance().player.getRandom().nextInt(values.length)];

        if(type == Type.SPACE){
            boolean[] selected = new boolean[]{false};
            RadialButton button1 = makeButton()
                    .setRotation(Mth.DEG_TO_RAD * (90 + 30)).setSize(Mth.DEG_TO_RAD * (360 - 60), Mth.DEG_TO_RAD * 8)
                    .setOnClick((button, click) -> {
                        if(click != InputConstants.KEY_SPACE) return false;
                        selected[0] = !selected[0];
                        if(type.clicksRequired <= ++clicks[0]) success();
                        return true;
                    })
                    .setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f, button -> selected[0]))
                    .setRenderIcon((button, graphics, x, y) -> WidgetHelper.blit(KEY_SPACE, graphics.pose(), x - 16, y - 16, 32, 32, 64, 64));
            button1.rebuildMesh();
            buttons.add(button1);
            return;
        }

        if(type == Type.UP_DOWN_KEY){
            makeCyclingButtons(180, 0);
            return;
        }

        makeCyclingButtons(90, 270);
    }

    protected RadialButton makeButton(){
        return new RadialButtonK()
                .setRadius(100).setThickness(40, 4);
    }

    protected void makeCyclingButtons(int rot0, int rot1){
        boolean[] selected = new boolean[]{Minecraft.getInstance().player.getRandom().nextBoolean(), false};
        selected[1] = !selected[0];
        RadialButton button1 = makeButton()
                .setRotation(Mth.DEG_TO_RAD * (rot0 + 6)).setSize(Mth.DEG_TO_RAD * (180 - 12), Mth.DEG_TO_RAD * 8)
                .setOnClick((button, click) -> {
                    if(click != type.switchKey0 || selected[0]) return false;
                    selected[0] = true;
                    selected[1] = false;
                    if(type.clicksRequired <= ++clicks[0]) success();
                    return true;
                })
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f, button -> selected[0]))
                .setRenderIcon((button, graphics, x, y) -> WidgetHelper.blit(type.tex0, graphics.pose(), x - 16, y - 16, 32, 32, 64, 64));
        button1.rebuildMesh();
        buttons.add(button1);
        RadialButton button2 = makeButton()
                .setRotation(Mth.DEG_TO_RAD * (rot1 + 6)).setSize(Mth.DEG_TO_RAD * (180 - 12), Mth.DEG_TO_RAD * 8)
                .setOnClick((button, click) -> {
                    if(click != type.switchKey1 || selected[1]) return false;
                    selected[1] = true;
                    selected[0] = false;
                    if(type.clicksRequired <= ++clicks[0]) success();
                    return true;
                })
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .025f, .025f, button -> selected[1]))
                .setRenderIcon((button, graphics, x, y) -> WidgetHelper.blit(type.tex1, graphics.pose(), x - 16, y - 16, 32, 32, 64, 64));
        button2.rebuildMesh();
        buttons.add(button2);
    }

    @Override
    protected void init() {
        if(!TransfurManager.isGrabbed(minecraft.player) || !minecraft.player.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)) {
            minecraft.setScreen(null);
            return;
        }

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        for(RadialButton button : buttons){
            button.setOrigin(halfWidth, halfHeight, 0);
            addRenderableWidget(button);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        MobEffectInstance effect = minecraft.player.getEffect(MobEffectRegistry.GRABBED_DEBUFF);
        guiGraphics.drawCenteredString(minecraft.font, Component.translatable("screen.a_changed.grab_escape.time_remaining",  effect == null ? "?" : effect.getDuration() / 20f), halfWidth, halfHeight - 8, Color.RED.getRGB());
        guiGraphics.drawCenteredString(minecraft.font, Component.translatable("screen.a_changed.grab_escape.clicks", clicks[0], type.clicksRequired), halfWidth, halfHeight + 8, Color.CYAN.getRGB());
    }

    protected void success(){
        PacketDistributor.sendToServer(new BidirectionalAbilityPacket(AbilityRegistry.GRAB_ABILITY,
                buf -> buf.writeByte(2).writeBoolean(true), 2));
        minecraft.player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
        minecraft.setScreen(null);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(super.keyPressed(keyCode, scanCode, modifiers)) return true;

        for(RadialButton button : buttons){
            if(button.keyPressed(keyCode, scanCode, modifiers)) return true;
        }

        return false;
    }

    @Override
    public void tick() {
        super.tick();
        if(!TransfurManager.isGrabbed(minecraft.player) || !minecraft.player.hasEffect(MobEffectRegistry.GRABBED_DEBUFF)) {
            minecraft.setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected enum Type {
        LEFT_RIGHT_MOUSE(InputConstants.MOUSE_BUTTON_LEFT, MOUSE_LEFT, InputConstants.MOUSE_BUTTON_RIGHT, MOUSE_RIGHT, 15),
        LEFT_RIGHT_KEY(InputConstants.KEY_LEFT, KEY_LEFT, InputConstants.KEY_RIGHT, KEY_RIGHT, 15),
        UP_DOWN_KEY(InputConstants.KEY_UP, KEY_UP, InputConstants.KEY_DOWN, KEY_DOWN, 15),
        SPACE(InputConstants.KEY_SPACE, KEY_SPACE, -1, null, 20);

        public final int switchKey0, switchKey1;
        public final ResourceLocation tex0, tex1;
        public final int clicksRequired;

        Type(int switchKey0, ResourceLocation tex0, int switchKey1, ResourceLocation tex1, int clicksRequired){
            this.switchKey0 = switchKey0;
            this.tex0 = tex0;
            this.switchKey1 = switchKey1;
            this.tex1 = tex1;
            this.clicksRequired = clicksRequired;
        }
    }

    protected static class RadialButtonK extends RadialButton {

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return true;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            return onClick.applyAsBoolean(this, keyCode);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            return onClick.applyAsBoolean(this, button);
        }
    }
}