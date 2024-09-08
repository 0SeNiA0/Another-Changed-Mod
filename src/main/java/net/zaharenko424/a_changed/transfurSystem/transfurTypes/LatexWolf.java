package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends TransfurType {

    public LatexWolf(@NotNull Properties properties){
        super(properties.eyeHeight(1.75f,1.5f).maxHealthModifier(4)
                .addAbility(AbilityRegistry.GRAB_ABILITY).addAbility(AbilityRegistry.WOLF_PASSIVE),
                FMLLoader.getDist().isClient() ? ClientOnly.latexWolfModel(properties.gender, properties.location) : null);
    }
}