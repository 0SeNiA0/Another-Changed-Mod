package net.zaharenko424.a_changed.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
public class NBTUtils {

    private static final String KEY="changed";

    public static @NotNull CompoundTag modTag(CompoundTag tag){
        if(tag.contains(KEY)) return tag.getCompound(KEY);
        tag.put(KEY,new CompoundTag());
        return tag.getCompound(KEY);
    }

    public static boolean hasModTag(@Nullable CompoundTag tag){
        return tag != null && tag.contains(KEY);
    }

    public static void putAABB(CompoundTag tag, AABB aabb){
        tag.putFloat("minX", (float) aabb.minX);
        tag.putFloat("minY", (float) aabb.minY);
        tag.putFloat("minZ", (float) aabb.minZ);
        tag.putFloat("maxX", (float) aabb.maxX);
        tag.putFloat("maxY", (float) aabb.maxY);
        tag.putFloat("maxZ", (float) aabb.maxZ);
    }

    @Contract("_ -> new")
    public static @NotNull AABB getAABB(CompoundTag tag){
        return new AABB(tag.getFloat("minX"), tag.getFloat("minY"), tag.getFloat("minZ"),
                        tag.getFloat("maxX"), tag.getFloat("maxY"), tag.getFloat("maxZ"));
    }

    public static void writeToTag(CompoundTag tag, List<String> list){
        if(list.isEmpty()) return;
        tag.putInt("Size",list.size());
        for(int i=0;i<list.size();i++){
            tag.putString(String.valueOf(i),list.get(i));
        }
    }

    public static void readFromTag(CompoundTag tag, List<String> list){
        if(!tag.contains("Size")) return;
        int size=tag.getInt("Size");
        for(int i=0;i<size;i++){
            list.add(tag.getString(String.valueOf(i)));
        }
    }
}