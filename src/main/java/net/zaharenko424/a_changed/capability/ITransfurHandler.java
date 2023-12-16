package net.zaharenko424.a_changed.capability;

import net.minecraft.nbt.CompoundTag;
import net.zaharenko424.a_changed.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITransfurHandler {

    float getTransfurProgress();

    void setTransfurProgress(float amount,@NotNull AbstractTransfurType transfurType);

    @Nullable AbstractTransfurType getTransfurType();

    void setTransfurType(@NotNull AbstractTransfurType transfurType);

    boolean isTransfurred();

    void transfur(@NotNull AbstractTransfurType transfurType);

    void unTransfur();

    void load(@NotNull CompoundTag tag);

    CompoundTag save();

    void tick();
}