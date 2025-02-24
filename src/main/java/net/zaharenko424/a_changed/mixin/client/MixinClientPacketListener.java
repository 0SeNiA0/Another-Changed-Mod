package net.zaharenko424.a_changed.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.zaharenko424.a_changed.capability.TransfurHandler;
import net.zaharenko424.a_changed.client.cmrs.CustomModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;handleEntityEvent(B)V"), method = "handleEntityEvent")
    private void onHandleEntityEvent(ClientboundEntityEventPacket packet, CallbackInfo ci, @Local Entity entity){
        if(!(entity instanceof RemotePlayer player)) return;

        ResourceLocation lastModelId = TransfurHandler.nonNullOf(player).getLastTFModelId();
        if(lastModelId != null) CustomModelManager.getInstance().removePlayerModel(player, lastModelId);
    }
}
