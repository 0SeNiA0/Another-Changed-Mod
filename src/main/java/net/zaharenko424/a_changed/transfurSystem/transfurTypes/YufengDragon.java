package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.client.model.YufengDragonModel;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;

public class YufengDragon extends AbstractFlyingLatex {

    public YufengDragon(@NotNull ResourceLocation loc) {
        super(FlyingProperties.of(loc).maxHealthModifier(6).colors(-13686230,-14408668),
                FMLLoader.getDist().isClient() ? MemorizingSupplier.of(YufengDragonModel::new) : null);
    }
}