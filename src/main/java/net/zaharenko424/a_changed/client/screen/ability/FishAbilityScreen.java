package net.zaharenko424.a_changed.client.screen.ability;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.client.cmrs.gui.widget.RadialButton;
import net.zaharenko424.a_changed.registry.AbilityRegistry;

import java.awt.*;
import java.util.List;

public class FishAbilityScreen extends SoundAbilityScreen {

    public FishAbilityScreen(List<Pair<String, SoundEvent>> sounds) {
        super(Component.empty(), sounds, 100, 60);
    }

    @Override
    protected RadialButton makeButton(String name, int index, int radius, int thickness) {
        return super.makeButton(name, index, radius, thickness)
                .setOutlineColorFunc(button -> button.isHovering() ? Color.CYAN.getRGB() : -10158081);
    }

    @Override
    protected DeferredHolder<Ability, ? extends Ability> ability() {
        return AbilityRegistry.FISH_PASSIVE;
    }
}