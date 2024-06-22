package net.zaharenko424.a_changed.mixin.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.EntityAccessor;
import net.zaharenko424.a_changed.capability.GrabCapability;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityAccessor {

    @ModifyReturnValue(at = @At("TAIL"), method = "getAirSupply")
    private int capAirSupply(int original){
        return Math.max(original, -20);
    }

    /**
     * Stops grabbed entities from pushing the player, that grabbed them.
     */
    @Inject(at = @At("HEAD"), method = "push(Lnet/minecraft/world/entity/Entity;)V", cancellable = true)
    private void onPush(Entity entity, CallbackInfo ci){
        if((entity instanceof Player player && TransfurManager.isGrabbed(player))
                || (getSelf() instanceof Player player1 && GrabCapability.nonNullOf(player1).getTarget() == entity))
            ci.cancel();
    }
}