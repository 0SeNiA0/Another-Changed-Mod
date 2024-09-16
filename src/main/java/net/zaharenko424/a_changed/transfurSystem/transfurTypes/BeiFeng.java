package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.client.model.BeiFengModel;
import net.zaharenko424.a_changed.registry.AbilityRegistry;
import net.zaharenko424.a_changed.util.Latex;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;

public class BeiFeng extends TransfurType {

    public BeiFeng(@NotNull ResourceLocation loc) {
        super(Properties.of(loc, Latex.WHITE).maxHealthModifier(4).colors(-11442787, -14013910)
                .addAbility(AbilityRegistry.GRAB_ABILITY),
                FMLLoader.getDist().isClient() ? MemorizingSupplier.of(BeiFengModel::new) : null);
    }
}