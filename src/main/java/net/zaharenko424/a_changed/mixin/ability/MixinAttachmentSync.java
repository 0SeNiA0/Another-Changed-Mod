package net.zaharenko424.a_changed.mixin.ability;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.attachment.*;
import net.neoforged.neoforge.common.util.FriendlyByteBufUtil;
import net.neoforged.neoforge.network.payload.SyncAttachmentsPayload;
import net.zaharenko424.a_changed.ability.api.AttachmentSelfSyncHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(AttachmentSync.class)
public abstract class MixinAttachmentSync {

    @Shadow
    private static SyncAttachmentsPayload.Target syncTarget(AttachmentHolder holder) {
        return null;
    }

    @Unique
    private static final ThreadLocal<ServerPlayer> sendTo = new ThreadLocal<>();

    //Sync different data to self then to everyone else
    @Inject(at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/common/util/FriendlyByteBufUtil;writeCustomData(Ljava/util/function/Consumer;Lnet/minecraft/core/RegistryAccess;)[B"),
            method = "syncUpdate", cancellable = true)
    private static <T> void onSync(AttachmentHolder holder, AttachmentType<T> type, List<ServerPlayer> players, CallbackInfo ci, @Local RegistryAccess registryAccess){
        if(!(holder instanceof ServerPlayer player)) return;
        AttachmentSyncHandler<T> sync = ((AttachmentTypeAccessor<T>) (Object) type).getSyncHandler();

        if(!(sync instanceof AttachmentSelfSyncHandler<?>)) return;

        var data = FriendlyByteBufUtil.writeCustomData(buf -> {
            var existingData = holder.getExistingDataOrNull(type);
            if (existingData != null) {
                buf.writeBoolean(true);
                ((AttachmentSelfSyncHandler<T>)sync).writeToSelf(buf, holder.getData(type), false);
            } else {
                buf.writeBoolean(false);
            }
        }, registryAccess);

        player.connection.send(new SyncAttachmentsPayload(syncTarget(holder), List.of(type), data).toVanillaClientbound());

        if(players.size() == 1) ci.cancel();
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/attachment/AttachmentSyncHandler;sendToPlayer(Lnet/neoforged/neoforge/attachment/IAttachmentHolder;Lnet/minecraft/server/level/ServerPlayer;)Z", ordinal = 1),
            method = "syncUpdate")
    private static <T> boolean preventDoubleSelfSync(AttachmentSyncHandler<T> instance, IAttachmentHolder holder, ServerPlayer _to, Operation<Boolean> original){
        boolean ret = original.call(instance, holder, _to);
        if(!(instance instanceof AttachmentSelfSyncHandler<?>)) return ret;
        return _to != holder && ret;
    }


    //Sync different data to self then to everyone else
    @Inject(at = @At("HEAD"), method = "syncInitialAttachments")
    private static void captureSendTo(AttachmentHolder holder, ServerPlayer _to, CallbackInfoReturnable<SyncAttachmentsPayload> cir){
        sendTo.set(_to);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/attachment/AttachmentSyncHandler;write(Lnet/minecraft/network/RegistryFriendlyByteBuf;Ljava/lang/Object;Z)V"),
            method = "lambda$syncInitialAttachments$3")
    private static <T> void onInitialSync(AttachmentSyncHandler<T> instance, RegistryFriendlyByteBuf registryFriendlyByteBuf, T data, boolean initialSync, Operation<Void> original, @Local(argsOnly = true) AttachmentHolder holder){
        if(instance instanceof AttachmentSelfSyncHandler<?> it && sendTo.get() == holder){
            ((AttachmentSelfSyncHandler<T>)it).writeToSelf(registryFriendlyByteBuf, data, initialSync);
        } else original.call(instance, registryFriendlyByteBuf, data, initialSync);
    }

    @Inject(at = @At("RETURN"), method = "syncInitialAttachments")
    private static void resetSendTo(AttachmentHolder holder, ServerPlayer _to, CallbackInfoReturnable<SyncAttachmentsPayload> cir){
        sendTo.remove();
    }
}
