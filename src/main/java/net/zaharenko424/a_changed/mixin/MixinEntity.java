package net.zaharenko424.a_changed.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.EntityAccessor;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityAccessor {

    @Shadow @Final protected SynchedEntityData entityData;
    @Shadow @Final private static EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID;

    @Inject(at = @At("HEAD"), method = "getAirSupply", cancellable = true)
    private void onGetAirSupply(@NotNull CallbackInfoReturnable<Integer> cir){
        int air = entityData.get(DATA_AIR_SUPPLY_ID);
        cir.setReturnValue(Math.max(air, -20));
    }

    @Inject(at = @At("HEAD"), method = "push(Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    private void onPush(Entity entity, CallbackInfo ci){

        if((entity instanceof Player player && TransfurManager.isGrabbed(player))
                || (getSelf() instanceof Player player1 && GrabCapability.nonNullOf(player1).getTarget() == entity))
            ci.cancel();
    }
}