package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.ability.network.packets.BidirectionalAbilityPacket;
import net.zaharenko424.a_changed.attachment.GrabData;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.RadialButton;

public class GrabAbilityPlayerScreen extends Screen implements MouseMoveListener {

    public static final ResourceLocation yes = AChanged.textureLoc("gui/want_to_be_grabbed");
    public static final ResourceLocation nope = AChanged.textureLoc("gui/dont_want_to_be_grabbed");

    protected final RadialButton buttonY, buttonN;
    protected GrabData data;

    public GrabAbilityPlayerScreen() {
        super(Component.empty());

        buttonY = new RadialButton()
                .setRotation(Mth.DEG_TO_RAD * (90 + 6)).setSize(Mth.DEG_TO_RAD * (180 - 12), Mth.DEG_TO_RAD * 8)
                .setRadius(100).setThickness(60, 4)
                .setOutlineColorFunc(WidgetHelper.outlineColor(button -> data.wantsToBeGrabbed()))
                .setOnClick((button, click) -> {
                    if(data.wantsToBeGrabbed()) return false;
                    click(true);
                    return true;
                })
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .02f, .02f, w -> w.isHovering() || data.wantsToBeGrabbed()))
                .setRenderIcon((button, graphics, x, y) -> WidgetHelper.blit(yes, graphics.pose(), x-16, y-16, 32, 32, 64, 64))
                .setExtendClickAreaOutside(true).setExtendClickAreaInside(true);
        buttonY.rebuildMesh();

        buttonN = new RadialButton()
                .setRotation(Mth.DEG_TO_RAD * (270 + 6)).setSize(Mth.DEG_TO_RAD * (180 - 12), Mth.DEG_TO_RAD * 8)
                .setRadius(100).setThickness(60, 4)
                .setOutlineColorFunc(WidgetHelper.outlineColor(button -> !data.wantsToBeGrabbed()))
                .setOnClick((button, click) -> {
                    if(!data.wantsToBeGrabbed()) return false;
                    click(false);
                    return true;
                })
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .02f, .02f, w -> w.isHovering() || !data.wantsToBeGrabbed()))
                .setRenderIcon((button, graphics, x, y) -> WidgetHelper.blit(nope, graphics.pose(), x - 16, y - 16, 32, 32, 64, 64))
                .setExtendClickAreaOutside(true).setExtendClickAreaInside(true);
        buttonN.rebuildMesh();

        data = GrabData.dataOf(Minecraft.getInstance().player);
    }

    @Override
    protected void init() {
        if(TransfurManager.isTransfurred(minecraft.player)) {
            minecraft.setScreen(new GrabAbilityLatexScreen());
            return;
        }

        buttonY.setOrigin(width / 2f, height / 2f, 0);
        buttonN.setOrigin(width / 2f, height / 2f, 0);

        addRenderableWidget(buttonY);
        addRenderableWidget(buttonN);
    }

    protected void click(boolean y){
        PacketDistributor.sendToServer(new BidirectionalAbilityPacket(AbilityRegistry.GRAB_ABILITY,
                buf -> buf.writeByte(1).writeBoolean(y), 2));

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) Minecraft.getInstance().setScreen(null);
        if(TransfurManager.isTransfurred(minecraft.player)) minecraft.setScreen(new GrabAbilityLatexScreen());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}