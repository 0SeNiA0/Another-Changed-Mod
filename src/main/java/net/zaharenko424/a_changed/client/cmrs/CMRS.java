package net.zaharenko424.a_changed.client.cmrs;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class CMRS {

    public static final String MOD_ID = "cmrs";

    @Contract("_ -> new")
    public static @NotNull ResourceLocation resourceLoc(String namespace){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, namespace);
    }
}