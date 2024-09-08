package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
@ApiStatus.Internal
public class TransfurToleranceData extends SavedData {

    private static final String NAME = "transfur_tolerance";
    private static final SavedData.Factory<TransfurToleranceData> FACTORY = new Factory<>(()-> {
        TransfurManager.TRANSFUR_TOLERANCE = TransfurManager.DEF_TRANSFUR_TOLERANCE;
        return new TransfurToleranceData();
    }, tag -> {
        TransfurManager.TRANSFUR_TOLERANCE = tag.getFloat(NAME);
        return new TransfurToleranceData();
    });

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putFloat(NAME, TransfurManager.TRANSFUR_TOLERANCE);
        return tag;
    }

    public static TransfurToleranceData of(ServerLevel level){
        return level.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, NAME);
    }

    public static void setDirty(ServerLevel level){
        of(level).setDirty();
    }
}