package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.GrabMode;
import net.zaharenko424.a_changed.attachments.GrabData;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.cmrs.gui.screen.MouseMoveListener;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.RadialButton;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.WidgetHelper;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundAbilityPacket;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;

public class GrabAbilityLatexScreen extends Screen implements MouseMoveListener {

    protected final RadialButton none;
    protected final RadialButton friendly;
    protected final RadialButton assimilate;
    protected final RadialButton replicate;

    protected GrabData data;

    public GrabAbilityLatexScreen() {
        super(Component.empty());

        none = makeButton(GrabMode.NONE).setRotation(Mth.DEG_TO_RAD * (45 + 4));
        none.rebuildMesh();

        friendly = makeButton(GrabMode.FRIENDLY).setRotation(Mth.DEG_TO_RAD * (135 + 4));
        friendly.rebuildMesh();

        assimilate = makeButton(GrabMode.ASSIMILATE).setRotation(Mth.DEG_TO_RAD * (225 + 4));
        assimilate.rebuildMesh();

        replicate = makeButton(GrabMode.REPLICATE).setRotation(Mth.DEG_TO_RAD * (315 + 4));
        replicate.rebuildMesh();

        data = GrabData.dataOf(Minecraft.getInstance().player);
    }

    protected RadialButton makeButton(GrabMode mode){
        return new RadialButton().setRadius(100).setThickness(60, 4)
                .setSize(Mth.DEG_TO_RAD * (90 - 8), Mth.DEG_TO_RAD * 8)
                .setOutlineColorFunc(WidgetHelper.outlineColor(button -> data.getMode() == mode))
                .setOnClick((button, click) -> {
                    if(data.getMode() == mode) return false;
                    click(mode);
                    return true;
                })
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .02f, .02f, w -> w.isHovering() || data.getMode() == mode))
                .setRenderIcon(((button, graphics, x, y) -> {
                    WidgetHelper.blit(mode.texture, graphics.pose(), x-16, y-16, 32, 32, 64, 64);
                }))
                .setExtendClickAreaOutside(true).setExtendClickAreaInside(true);
    }

    @Override
    protected void init() {
        super.init();
        if(!TransfurManager.isTransfurred(minecraft.player)) {
            minecraft.setScreen(new GrabAbilityPlayerScreen());
            return;
        }

        int halfWidth = width / 2;
        int halfHeight = height / 2;
        none.setOrigin(halfWidth, halfHeight, 0);
        friendly.setOrigin(halfWidth, halfHeight, 0);
        assimilate.setOrigin(halfWidth, halfHeight, 0);
        replicate.setOrigin(halfWidth, halfHeight, 0);

        addRenderableWidget(none);
        addRenderableWidget(friendly);
        addRenderableWidget(assimilate);
        addRenderableWidget(replicate);
    }

    protected void click(GrabMode mode){
        PacketDistributor.sendToServer(new ServerboundAbilityPacket(AbilityRegistry.GRAB_ABILITY.getId(),
                new FriendlyByteBuf(Unpooled.buffer(2)).writeByte(0).writeEnum(mode)));

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) {
            minecraft.setScreen(null);
            return;
        }
        if(!TransfurManager.isTransfurred(minecraft.player)) minecraft.setScreen(new GrabAbilityPlayerScreen());
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}