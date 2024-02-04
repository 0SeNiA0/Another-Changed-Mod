package net.zaharenko424.a_changed.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class MixinServerGamePacketListenerImpl {

    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/server/level/ServerPlayer.isDeadOrDying ()Z"), method = "tick")
    private boolean onTick(@NotNull ServerPlayer player){
        return !player.isDeadOrDying() && !TransfurManager.isGrabbed(player);
    }
}