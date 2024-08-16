package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.client.model.YufengDragonModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;

public class YufengDragon extends TransfurType {

    public YufengDragon(@NotNull ResourceLocation loc) {
        super(FlyingProperties.of(loc).maxHealthModifier(6).colors(-13686230,-14408668)
                .addAbility(AbilityRegistry.GRAB_ABILITY),
                FMLLoader.getDist().isClient() ? MemorizingSupplier.of(YufengDragonModel::new) : null);
    }
}