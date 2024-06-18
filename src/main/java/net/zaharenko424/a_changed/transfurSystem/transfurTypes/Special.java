package net.zaharenko424.a_changed.transfurSystem.transfurTypes;

import net.neoforged.fml.loading.FMLLoader;
import net.zaharenko424.a_changed.util.MemorizingSupplier;
import org.jetbrains.annotations.NotNull;

public class Special extends TransfurType {

    public Special(@NotNull Properties properties) {
        super(properties.maxHealthModifier(4).organic(true),
                FMLLoader.getDist().isClient() ?
                MemorizingSupplier.of(() -> null)
                : null);
    }
}