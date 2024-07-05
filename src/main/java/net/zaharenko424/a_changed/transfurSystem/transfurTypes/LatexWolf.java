package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.neoforged.fml.loading.FMLLoader;
import org.jetbrains.annotations.NotNull;

public class LatexWolf extends TransfurType {

    public LatexWolf(@NotNull Properties properties){
        super(properties.eyeHeight(1.75f,1.5f).maxHealthModifier(4),
                FMLLoader.getDist().isClient() ? ClientOnly.latexWolfModel(properties.gender, properties.location) : null);
    }
}