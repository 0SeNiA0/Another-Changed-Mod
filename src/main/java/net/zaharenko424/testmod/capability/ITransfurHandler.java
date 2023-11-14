package net.zaharenko424.testmod.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITransfurHandler {

    int getTransfurProgress();

    void setTransfurProgress(int amount,@NotNull ResourceLocation transfurType);

    @Nullable ResourceLocation getTransfurType();

    void setTransfurType(@NotNull ResourceLocation transfurType);

    boolean isTransfurred();

    void transfur(@NotNull ResourceLocation transfurType);

    void unTransfur();

    void load(@NotNull CompoundTag tag);

    CompoundTag save();

    void tick();
}