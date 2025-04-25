package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.attachments.TransfurHandler;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DamageSources {

    ResourceKey<DamageType> assimilation = create("assimilation");
    ResourceKey<DamageType> electricity = create("electricity");
    ResourceKey<DamageType> solvent = create("latex_solvent");
    ResourceKey<DamageType> syringe = create("syringe");
    ResourceKey<DamageType> transfur = create("transfur");
    ResourceKey<DamageType> untransfur = create("untransfur");

    @Contract("_ -> new")
    static @NotNull DamageSource assimilation(@NotNull LivingEntity attacker){
        return new DamageSource(holder(attacker.level(), assimilation), null, attacker);
    }

    static @NotNull DamageSource transfur(@NotNull Entity attacker){
        return transfur(attacker.level(), attacker, attacker);
    }

    @Contract("_, _ -> new")
    static @NotNull DamageSource transfur(@Nullable Entity attackerProjectile, @NotNull Entity attacker){
        return transfur(attacker.level(), attackerProjectile, attacker);
    }

    static @NotNull DamageSource transfur(@NotNull Level level, @Nullable Entity attackerProjectile, @Nullable Entity attacker){
        return new DamageSource(holder(level, transfur), attackerProjectile != null ? attackerProjectile : attacker, attacker);
    }

    static @NotNull DamageSource latexSolvent(@NotNull Level level, @Nullable Entity attacker){
        return latexSolvent(level, attacker, attacker);
    }

    static @NotNull DamageSource latexSolvent(@NotNull Level level, Entity projectile, @Nullable Entity attacker){
        return new DamageSource(holder(level, solvent), projectile, attacker);
    }

    static @NotNull DamageSource syringe(@NotNull Level level, @Nullable Entity attacker){
        return syringe(level, attacker, attacker);
    }

    static @NotNull DamageSource syringe(@NotNull Level level, Entity projectile, @Nullable Entity attacker){
        return new DamageSource(holder(level, syringe), projectile, attacker);
    }

    static @NotNull DamageSource electricity(@NotNull Level level, @Nullable Entity attacker){
        return new DamageSource(holder(level, electricity), attacker, attacker);
    }

    static @NotNull DamageSource untransfur(@NotNull Level level, Entity attacker){
        return untransfur(level, attacker, attacker);
    }

    static @NotNull DamageSource untransfur(@NotNull Level level, Entity projectile, Entity attacker){
        return new DamageSource(holder(level, untransfur), projectile, attacker);
    }

    private static @NotNull Holder<DamageType> holder(@NotNull Level level, ResourceKey<DamageType> key){
        return level.holderOrThrow(key);
    }

    private static @NotNull ResourceKey<DamageType> create(String str){
        return Utils.resourceKey(Registries.DAMAGE_TYPE, str);
    }

    static boolean checkTFTarget(Entity target){
        return target instanceof LivingEntity entity && TransfurHandler.of(entity) != null
                && (!(target instanceof Player player) || (!TransfurManager.isTransfurred(player) && !TransfurManager.isBeingTransfurred(player)));
    }
}