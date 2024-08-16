package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.client.model.HypnoCatModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;

public class HypnoCat extends TransfurType {

    public HypnoCat(@NotNull ResourceLocation loc) {
        super(CatProperties.of(loc).maxHealthModifier(4).colors(-13421773, -2621626)
                .addAbility(AbilityRegistry.GRAB_ABILITY).addAbility(AbilityRegistry.HYPNOSIS_ABILITY),
                FMLLoader.getDist().isClient() ? MemorizingSupplier.of(HypnoCatModel::new) : null);
    }
}