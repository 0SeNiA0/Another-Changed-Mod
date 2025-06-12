package net.zaharenko424.a_changed.client.cmrs;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public class CMRS {

    public static final String MODID = "cmrs";

    public static final Logger LOGGER = LogUtils.getLogger();

    @Contract("_ -> new")
    public static @NotNull ResourceLocation resourceLoc(String namespace){
        return ResourceLocation.fromNamespaceAndPath(MODID, namespace);
    }

    @Contract("_ -> new")
    public static @NotNull ResourceLocation textureLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID,"textures/" + path + ".png");
    }
}