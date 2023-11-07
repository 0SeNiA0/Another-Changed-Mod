package net.zaharenko424.testmod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Transfurrable {

    int mod$getTransfurProgress();

    void mod$setTransfurProgress(int amount, @NotNull String transfurType);
    @Nullable String mod$getTransfurType();

    void mod$setTransfurType(@NotNull String transfurType);

    void mod$transfur(@NotNull String transfurType);
}