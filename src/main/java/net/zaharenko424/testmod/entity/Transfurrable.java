package net.zaharenko424.testmod.entity;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Transfurrable {

    int mod$getTransfurProgress();

    void mod$setTransfurProgress(int amount, @NotNull ResourceLocation transfurType);
    @Nullable ResourceLocation mod$getTransfurType();

    void mod$setTransfurType(@NotNull ResourceLocation transfurType);

    void mod$transfur(@NotNull ResourceLocation transfurType);
}