package net.zaharenko424.a_changed.mixin.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Cat;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(Cat.CatAvoidEntityGoal.class)
public class MixinCat {

    /**
     *  Makes cats not afraid of cat transfurred players.
     */
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/AvoidEntityGoal;<init>(Lnet/minecraft/world/entity/PathfinderMob;Ljava/lang/Class;FDDLjava/util/function/Predicate;)V"),
            method = "<init>", index = 5)
    private static Predicate<LivingEntity> onInit(Predicate<LivingEntity> original){
        return original.and(entity -> !TransfurManager.isTransfurred(entity) || !TransfurManager.hasCatAbility(entity));
    }
}