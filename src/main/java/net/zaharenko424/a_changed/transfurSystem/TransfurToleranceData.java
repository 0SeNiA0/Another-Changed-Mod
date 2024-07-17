package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedData;
import net.zaharenko424.a_changed.AChanged;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
public class TransfurToleranceData extends SavedData {

    public static final SavedData.Factory<TransfurToleranceData> FACTORY = new Factory<>(()-> {
        TransfurManager.TRANSFUR_TOLERANCE = TransfurManager.DEF_TRANSFUR_TOLERANCE;
        return new TransfurToleranceData();
    }, tag -> {
        TransfurManager.TRANSFUR_TOLERANCE = tag.getFloat("transfur_tolerance");
        return new TransfurToleranceData();
    });

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag pCompoundTag) {
        pCompoundTag.putFloat("transfur_tolerance", TransfurManager.TRANSFUR_TOLERANCE);
        AChanged.LOGGER.error("saving tf tolerance, saved: {}", TransfurManager.TRANSFUR_TOLERANCE);
        return pCompoundTag;
    }
}