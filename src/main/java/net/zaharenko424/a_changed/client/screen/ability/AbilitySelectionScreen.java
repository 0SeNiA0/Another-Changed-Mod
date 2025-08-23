package net.zaharenko424.a_changed.client.screen.ability;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.ability.AbilityHolder;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.cmrs.client.gui.screen.MouseMoveListener;
import net.zaharenko424.cmrs.client.gui.widget.RadialButton;
import net.zaharenko424.cmrs.client.gui.WidgetHelper;
import net.zaharenko424.a_changed.network.packets.ability.ServerboundSelectAbilityPacket;
import net.zaharenko424.a_changed.util.AbilityUtils;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AbilitySelectionScreen extends Screen implements MouseMoveListener {

    protected AbilityHolder holder;
    protected final List<RadialButton> buttons = new ArrayList<>();
    protected final List<Ability> lastAbilities = new ArrayList<>();

    public AbilitySelectionScreen() {
        super(Component.empty());

        minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        holder = TransfurHandler.nonNullOf(player);

        lastAbilities.addAll(holder.getAbilities());
        int amount = lastAbilities.size();

        if(amount == 0){
            minecraft.setScreen(null);
            return;
        }

        if(amount == 1 && lastAbilities.getFirst().hasScreen()){
            minecraft.setScreen(lastAbilities.getFirst().getScreen(player));
            return;
        }

        float sizeRad = Mth.TWO_PI / amount;
        float off = Mth.DEG_TO_RAD * 4;

        RadialButton button;
        for(int i = 0; i < amount; i++){
            button = makeButton(lastAbilities.get(i))
                    .setRotation(Mth.HALF_PI + sizeRad * i + off).setSize(sizeRad - off * 2, Mth.DEG_TO_RAD * 8);
            button.rebuildMesh();
            buttons.add(button);
        }
    }

    protected RadialButton makeButton(Ability ability){
        return new RadialButton()
                .setOutlineColorFunc(button -> {
                    if(!ability.isSelectable()) return button.isHovering() ? Color.ORANGE.getRGB() : -14236;
                    if(holder.getSelectedAbility() == ability && button.isHovering()) return Color.GREEN.getRGB();
                    if(holder.getSelectedAbility() == ability) return -16711836;
                    return button.isHovering() ? Color.GRAY.getRGB() : Color.BLACK.getRGB();
                })
                .setRadius(100).setThickness(60, 4)
                .setOnClick((button, click) -> click(click, ability))
                .setRenderTransform(WidgetHelper.hoverAnim(.1f, .02f, .02f, w -> w.isHovering() || ability == holder.getSelectedAbility()))
                .setRenderIcon((button, graphics, x, y) ->
                        ability.drawIcon(minecraft.player, graphics, x.intValue() - 16, y.intValue() - 16, false))
                .setExtendClickAreaOutside(true).setExtendClickAreaInside(true);
    }

    @Override
    protected void init() {
        super.init();

        if(!holder.getAbilities().equals(lastAbilities)){
            List<? extends Ability> abilities = holder.getAbilities();
            int amount = abilities.size();

            if(amount == 0){
                minecraft.setScreen(null);
                return;
            }

            if(amount == 1 && abilities.getFirst().hasScreen()){
                minecraft.setScreen(abilities.getFirst().getScreen(minecraft.player));
                return;
            }

            List<RadialButton> newButtons = new ArrayList<>(amount);
            Ability ability;
            RadialButton button;
            int in;
            float sizeRad = Mth.TWO_PI / amount;
            float off = Mth.DEG_TO_RAD * 4;
            for(int i = 0; i < amount; i++){
                ability = abilities.get(i);

                in = lastAbilities.indexOf(ability);
                if(in != -1){
                    lastAbilities.remove(ability);
                    button = buttons.remove(in);
                    button.setRotation(Mth.HALF_PI + sizeRad * i + off).setSize(sizeRad - off * 2, Mth.DEG_TO_RAD * 8);
                    button.rebuildMesh();
                    newButtons.add(buttons.remove(in));
                    continue;
                }

                button = makeButton(ability)
                        .setRotation(Mth.HALF_PI + sizeRad * i + off).setSize(sizeRad - off * 2, Mth.DEG_TO_RAD * 8);
                button.rebuildMesh();
                newButtons.add(button);
            }

            buttons.clear();
            buttons.addAll(newButtons);
            lastAbilities.clear();
            lastAbilities.addAll(abilities);
        }

        int halfWidth = width / 2;
        int halfHeight = height / 2;

        for(RadialButton button : buttons) {
            button.setOrigin(halfWidth, halfHeight, 0);
            addRenderableWidget(button);
        }
    }

    protected boolean click(int button, Ability ability){
        Ability selected = holder.getSelectedAbility();
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        if(ability == selected){
            if(selected.hasScreen()) minecraft.setScreen(selected.getScreen(minecraft.player));
            return true;
        }

        if(!ability.isSelectable()){
            if(ability.hasScreen()) minecraft.setScreen(ability.getScreen(minecraft.player));
            return true;
        }

        if(button == GLFW.GLFW_MOUSE_BUTTON_RIGHT){
            if(ability.hasScreen()) minecraft.setScreen(ability.getScreen(minecraft.player));
            //return true;//TODO switch selected or just open the menu of clicked ability?
        }

        PacketDistributor.sendToServer(new ServerboundSelectAbilityPacket(AbilityUtils.abilityIdOf(ability)));
        return true;
    }

    @Override
    public void tick() {
        if(!InputConstants.isKeyDown(minecraft.getWindow().getWindow(), Keybindings.ABILITY_SELECTION.getKey().getValue())) {
            minecraft.setScreen(null);
            return;
        }

        if(!holder.getAbilities().equals(lastAbilities)) init();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}