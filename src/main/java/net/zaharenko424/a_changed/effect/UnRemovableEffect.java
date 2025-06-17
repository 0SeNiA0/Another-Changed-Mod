package net.zaharenko424.a_changed.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.neoforge.common.EffectCure;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class UnRemovableEffect extends MobEffect {

    public UnRemovableEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void fillEffectCures(@NotNull Set<EffectCure> cures, @NotNull MobEffectInstance effectInstance) {}
}