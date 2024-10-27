package net.zaharenko424.a_changed.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.NotNull;

public class Structures {

    public static final ResourceKey<Structure> LAB = key("lab");
    public static final ResourceKey<StructureSet> LAB_SET = setKey("lab");

    private static @NotNull ResourceKey<Structure> key(String str){
        return Utils.resourceKey(Registries.STRUCTURE, str);
    }

    private static ResourceKey<StructureSet> setKey(String str){
        return Utils.resourceKey(Registries.STRUCTURE_SET, str);
    }
}