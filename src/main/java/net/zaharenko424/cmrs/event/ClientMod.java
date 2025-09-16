package net.zaharenko424.cmrs.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;
import net.zaharenko424.cmrs.client.ModelPropertyManager;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        ModelDefinitionCache.init();
        CustomModelManager.init();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event){
        ModelPropertyManager.getInstance();
    }
}