package net.zaharenko424.a_changed.attachments;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public class GrabChanceData extends SavedData {

    public static final float DEF_CHANCE = .3f;
    private static final String NAME = "grab_chance";
    private static final SavedData.Factory<GrabChanceData> FACTORY = new Factory<>(GrabChanceData::new, tag -> {
        GrabChanceData data = new GrabChanceData();
        data.grabChance = tag.getFloat(NAME);
        return data;
    });

    private float grabChance = DEF_CHANCE;

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        tag.putFloat(NAME, grabChance);
        return tag;
    }

    public static GrabChanceData of(ServerLevel level){
        return level.getServer().overworld().getDataStorage().computeIfAbsent(FACTORY, NAME);
    }

    public float getGrabChance(){
        return grabChance;
    }

    @ApiStatus.Internal
    public void setGrabChance(float chance){
        grabChance = Mth.clamp(chance, 0, 1);
        setDirty();
    }
}