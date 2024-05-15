package net.zaharenko424.a_changed.util;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static net.zaharenko424.a_changed.transfurSystem.TransfurManager.TRANSFUR_TYPE_KEY;

@ParametersAreNonnullByDefault
public class NBTUtils {

    public static final String KEY = "changed";

    public static @NotNull CompoundTag modTag(CompoundTag tag){
        if(tag.contains(KEY)) return tag.getCompound(KEY);
        tag.put(KEY, new CompoundTag());
        return tag.getCompound(KEY);
    }

    public static boolean hasModTag(@Nullable CompoundTag tag){
        return tag != null && tag.contains(KEY);
    }

    public static @Nullable AbstractTransfurType readTransfurType(CompoundTag tag){
        if(!tag.contains(TransfurManager.TRANSFUR_TYPE_KEY)) return null;
        return TransfurManager.getTransfurType(new ResourceLocation(tag.getString(TRANSFUR_TYPE_KEY)));
    }

    public static void putBlockPos(CompoundTag tag, BlockPos pos){
        tag.putInt("x", pos.getX());
        tag.putInt("y", pos.getY());
        tag.putInt("z", pos.getZ());
    }

    @Contract("_ -> new")
    public static @NotNull BlockPos getBlockPos(CompoundTag tag){
        return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
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
        for(int i = 0; i < list.size(); i++){
            tag.putString(String.valueOf(i),list.get(i));
        }
    }

    public static void readFromTag(CompoundTag tag, List<String> list){
        if(!tag.contains("Size")) return;
        int size = tag.getInt("Size");
        for(int i = 0; i < size; i++){
            list.add(tag.getString(String.valueOf(i)));
        }
    }
}