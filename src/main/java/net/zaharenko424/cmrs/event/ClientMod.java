package net.zaharenko424.cmrs.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.client.ModelDefinitionCache;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientMod {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event){
        ModelDefinitionCache.init();
        CustomModelManager.init();
    }
}