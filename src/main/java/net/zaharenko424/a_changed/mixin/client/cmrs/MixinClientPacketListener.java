package net.zaharenko424.a_changed.mixin.client.cmrs;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.world.entity.Entity;
import net.zaharenko424.cmrs.client.CustomModelManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ClientPacketListener.class)
public abstract class MixinClientPacketListener extends ClientCommonPacketListenerImpl {

    @Shadow
    private ClientLevel level;

    private MixinClientPacketListener(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
        super(minecraft, connection, commonListenerCookie);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;removeEntity(ILnet/minecraft/world/entity/Entity$RemovalReason;)V"),
            method = "lambda$handleRemoveEntities$1")
    private void onHandleRemoveEntities(int entityId, CallbackInfo ci){
        Entity entity = level.getEntity(entityId);
        if(entity instanceof RemotePlayer player) CustomModelManager.getInstance().playerDied(player);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/social/PlayerSocialManager;removePlayer(Ljava/util/UUID;)V"),
            method = "handlePlayerInfoRemove")
    private void onHandlePlayerInfoRemove(ClientboundPlayerInfoRemovePacket packet, CallbackInfo ci, @Local UUID uid){
        CustomModelManager.getInstance().unloadPlayer(uid);
    }
}
