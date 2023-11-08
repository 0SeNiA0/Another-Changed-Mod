package net.zaharenko424.testmod.entity;

import org.jetbrains.annotations.NotNull;

public interface Transfurrable {

    int mod$getTransfurProgress();

    void mod$setTransfurProgress(int amount, @NotNull String transfurType);
    @NotNull String mod$getTransfurType();

    void mod$setTransfurType(@NotNull String transfurType);

    void mod$transfur(@NotNull String transfurType);
}