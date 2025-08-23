package net.zaharenko424.cmrs;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.zaharenko424.cmrs.registry.AttachmentRegistry;
import net.zaharenko424.cmrs.registry.MaterialRegistry;
import net.zaharenko424.cmrs.registry.ModelPropertyRegistry;
import net.zaharenko424.cmrs.registry.RenderLayerRegistry;
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
        return ResourceLocation.fromNamespaceAndPath(MODID, "textures/" + path + ".png");
    }

    public CMRS(IEventBus modEventBus, ModContainer container){
        AttachmentRegistry.ATTACHMENTS.register(modEventBus);
        ModelPropertyRegistry.PROPERTIES.register(modEventBus);

        container.registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_SPEC);

        if(FMLEnvironment.dist.isClient()) {//Server doesn't crash
            MaterialRegistry.MATERIALS.register(modEventBus);
            RenderLayerRegistry.LAYERS.register(modEventBus);
        }
    }
}