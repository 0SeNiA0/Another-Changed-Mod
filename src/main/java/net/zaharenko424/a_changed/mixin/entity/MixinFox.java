package net.zaharenko424.a_changed.mixin.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.transfurSystem.TransfurManager;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractLatexCat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.function.Predicate;

@Mixin(Fox.class)
public class MixinFox {

    /**
     *  Makes foxes not afraid of cat transfurred players.
     */
    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ai/goal/AvoidEntityGoal;<init>(Lnet/minecraft/world/entity/PathfinderMob;Ljava/lang/Class;FDDLjava/util/function/Predicate;)V", ordinal = 0),
            index = 5, method = "registerGoals")
    private Predicate<LivingEntity> onRegisterGoals(Predicate<LivingEntity> original){
        return original.and(entity -> !TransfurManager.isTransfurred((Player) entity)
                || !(TransfurManager.getTransfurType(entity) instanceof AbstractLatexCat));
    }
}