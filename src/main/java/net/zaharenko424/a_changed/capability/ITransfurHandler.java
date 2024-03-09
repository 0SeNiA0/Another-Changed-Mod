package net.zaharenko424.a_changed.capability;

import net.minecraft.nbt.CompoundTag;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface ITransfurHandler {

    float getTransfurProgress();

    void setTransfurProgress(float amount, AbstractTransfurType transfurType);

    @Nullable AbstractTransfurType getTransfurType();

    void setTransfurType(AbstractTransfurType transfurType);

    boolean isTransfurred();

    void transfur(AbstractTransfurType transfurType);

    void unTransfur();

    boolean isBeingTransfurred();

    void setBeingTransfurred(boolean isBeingTransfurred);

    ITransfurHandler load(CompoundTag tag);

    CompoundTag save();

    void tick();
}