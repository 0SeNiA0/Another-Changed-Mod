package net.zaharenko424.cmrs.event;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.zaharenko424.cmrs.client.CustomModelManager;
import net.zaharenko424.cmrs.client.ModelPropertyManager;

@EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public static void onLogIn(ClientPlayerNetworkEvent.LoggingIn event){
        ModelPropertyManager.getInstance().logIn(event);
    }

    @SubscribeEvent
    public static void onLogOut(ClientPlayerNetworkEvent.LoggingOut event){
        ModelPropertyManager.getInstance().logOut(event);

        LocalPlayer player = event.getPlayer();
        if(player != null) CustomModelManager.getInstance().playerDied(player);
    }

    @SubscribeEvent
    public static void onClone(ClientPlayerNetworkEvent.Clone event){
        ModelPropertyManager.getInstance().clonePlayer(event);

        LocalPlayer old = event.getOldPlayer();
        if(old.getRemovalReason() == null || old.getRemovalReason() == Entity.RemovalReason.CHANGED_DIMENSION) return;
        CustomModelManager.getInstance().playerDied(old);
    }
}
