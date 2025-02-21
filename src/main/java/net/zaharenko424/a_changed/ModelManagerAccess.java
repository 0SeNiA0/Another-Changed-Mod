package net.zaharenko424.a_changed;

import net.minecraft.resources.ResourceLocation;
import net.zaharenko424.a_changed.util.Thing;

import java.io.File;
import java.util.HashMap;

public interface ModelManagerAccess {

    HashMap<ResourceLocation, Thing<File, File, File, File>> achanged$getConvertedTextures();
}
