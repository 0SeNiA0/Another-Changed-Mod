package net.zaharenko424.a_changed.mixin;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow @Final protected SynchedEntityData entityData;
    @Shadow @Final private static EntityDataAccessor<Integer> DATA_AIR_SUPPLY_ID;

    @Inject(at = @At("HEAD"), method = "getAirSupply", cancellable = true)
    public void onGetAirSupply(@NotNull CallbackInfoReturnable<Integer> cir){
        int air = entityData.get(DATA_AIR_SUPPLY_ID);
        cir.setReturnValue(Math.max(air, -20));
    }
}