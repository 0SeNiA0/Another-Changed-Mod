package net.zaharenko424.a_changed.ability.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.zaharenko424.a_changed.ability.api.Ability;
import net.zaharenko424.a_changed.ability.api.AbilityHolder;
import net.zaharenko424.a_changed.client.Keybindings;
import net.zaharenko424.a_changed.client.screen.ability.AbilitySelectionScreen;
import net.zaharenko424.a_changed.util.AbilityUtils;
import net.zaharenko424.a_changed.util.SequencedSetView;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.SequencedSet;

@ParametersAreNonnullByDefault
@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onKeyPress(InputEvent.Key event){
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if(player == null) return;

        AbilityHolder holder = AbilityUtils.of(player);
        SequencedSet<? extends Ability> abilities = holder.getAbilities();
        if(abilities.isEmpty()) return;

        if(quickAbilitySelect(holder, abilities)) return;

        if(Keybindings.ABILITY_SELECTION.isDown()){
            Screen screen = new AbilitySelectionScreen();// <-- sets screen in init so check before setting
            if(Minecraft.getInstance().screen == null) minecraft.setScreen(screen);
            return;
        }

        holder.inputTick();
    }

    private static boolean quickAbilitySelect(AbilityHolder holder, SequencedSet<? extends Ability> abilities){
        if(Keybindings.QUICK_SELECT_ABILITY_1.consumeClick() && !abilities.isEmpty()){
            holder.selectAbility(abilities.getFirst());
            return true;
        }
        if(Keybindings.QUICK_SELECT_ABILITY_2.consumeClick() && abilities.size() > 1){
            holder.selectAbility(SequencedSetView.byIndex(abilities, 1));
            return true;
        }
        if(Keybindings.QUICK_SELECT_ABILITY_3.consumeClick() && abilities.size() > 2){
            holder.selectAbility(SequencedSetView.byIndex(abilities, 2));
            return true;
        }
        return false;
    }
}
