package net.zaharenko424.a_changed.client.screen.ability;

import it.unimi.dsi.fastutil.Pair;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.zaharenko424.a_changed.ability.Ability;
import net.zaharenko424.a_changed.registry.AbilityRegistry;

import java.util.List;

public class WolfAbilityScreen extends SoundAbilityScreen {

    public WolfAbilityScreen(List<Pair<String, SoundEvent>> sounds) {
        super(Component.empty(), sounds, 100, 60);
    }

    @Override
    protected DeferredHolder<Ability, ? extends Ability> ability() {
        return AbilityRegistry.WOLF_PASSIVE;
    }
}