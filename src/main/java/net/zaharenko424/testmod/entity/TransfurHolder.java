package net.zaharenko424.testmod.entity;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface TransfurHolder extends Transfurrable {

    boolean mod$isTransfurred();

    void mod$transfur(@NotNull ResourceLocation transfurType);

    void mod$unTransfur();
}