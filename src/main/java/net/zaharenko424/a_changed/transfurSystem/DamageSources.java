package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.attachment.TransfurHandler;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DamageSources {

    ResourceKey<DamageType> assimilation = create("assimilation");
    ResourceKey<DamageType> electricity = create("electricity");
    ResourceKey<DamageType> solvent = create("latex_solvent");
    ResourceKey<DamageType> syringe = create("syringe");
    ResourceKey<DamageType> placedSyringe = create("placed_syringe");
    ResourceKey<DamageType> transfur = create("transfur");
    ResourceKey<DamageType> transfurKill = create("transfur_kill");
    ResourceKey<DamageType> untransfur = create("untransfur");
    ResourceKey<DamageType> untransfurKill = create("untransfur_kill");

    @Contract("_ -> new")
    static @NotNull DamageSource assimilation(@NotNull LivingEntity attacker){
        return new DamageSource(holder(attacker.level(), assimilation), null, attacker);
    }

    static @NotNull DamageSource transfur(@NotNull LivingEntity attacker){
        return transfur(attacker.level(), attacker);
    }

    @Contract("_, _ -> new")
    static @NotNull DamageSource transfur(@NotNull Level level, @Nullable LivingEntity attacker){
        return new DamageSource(holder(level, transfur), attacker, attacker);
    }

    static @NotNull DamageSource transfurKill(@NotNull Level level, @Nullable LivingEntity attacker){
        return new DamageSource(holder(level, transfurKill), attacker, attacker);
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

    static @NotNull DamageSource placedSyringe(@NotNull Entity projectile, @Nullable Entity attacker){
        return new DamageSource(holder(projectile.level(), placedSyringe), projectile, attacker);
    }

    static @NotNull DamageSource electricity(@NotNull Level level, @Nullable Entity attacker){
        return new DamageSource(holder(level, electricity), attacker, attacker);
    }

    static @NotNull DamageSource untransfur(@NotNull Level level, Entity attacker){
        return new DamageSource(holder(level, untransfur), attacker, attacker);
    }

    static @NotNull DamageSource untransfurKill(@NotNull Level level, @Nullable LivingEntity attacker){
        return untransfurKill(level, attacker, attacker);
    }

    static @NotNull DamageSource untransfurKill(@NotNull Level level, Entity projectile, Entity attacker){
        return new DamageSource(holder(level, untransfurKill), projectile, attacker);
    }

    private static @NotNull Holder<DamageType> holder(@NotNull Level level, ResourceKey<DamageType> key){
        return level.holderOrThrow(key);
    }

    private static @NotNull ResourceKey<DamageType> create(String str){
        return Utils.resourceKey(Registries.DAMAGE_TYPE, str);
    }

    static boolean checkTFTarget(@Nullable Entity target){
        if(!(target instanceof LivingEntity entity)) return false;

        TransfurHandler handler = TransfurHandler.of(entity);
        if(handler == null) return false;

        return !handler.isTransfurred() && !handler.isBeingTransfurred();
    }
}