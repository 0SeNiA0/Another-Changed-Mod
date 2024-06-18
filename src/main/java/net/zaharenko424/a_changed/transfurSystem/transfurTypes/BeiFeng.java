package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.client.model.HypnoCatModel;
import net.zaharenko424.a_changed.util.Latex;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;

public class BeiFeng extends TransfurType {

    public BeiFeng(@NotNull ResourceLocation loc) {
        super(Properties.of(loc, Latex.WHITE).eyeHeight(1.75f,1.5f).maxHealthModifier(4),
                FMLLoader.getDist().isClient() ? MemorizingSupplier.of(HypnoCatModel::new) : null);
    }
}