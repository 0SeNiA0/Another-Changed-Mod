package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import net.zaharenko424.a_changed.util.Utils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DamageSources {

    ResourceKey<DamageType> assimilation = create("assimilation");
    ResourceKey<DamageType> transfur = create("transfur");
    ResourceKey<DamageType> solvent = create("latex_solvent");

    @Contract("_, _ -> new")
    static <T extends Entity,E extends LivingEntity> @NotNull DamageSource assimilation(@NotNull T attackerDirect, @Nullable E attackerIndirect){
        return new DamageSource(attackerDirect.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(assimilation), attackerDirect, attackerIndirect);
    }

    @Contract("_, _ -> new")
    static <T extends Entity,E extends Entity> @NotNull DamageSource transfur(@NotNull T attackerDirect, @Nullable E attackerIndirect){
        return transfur(attackerDirect.level(), attackerDirect, attackerIndirect);
    }

    static <T extends Entity,E extends Entity> @NotNull DamageSource transfur(@NotNull Level level, @Nullable T attackerDirect, @Nullable E attackerIndirect){
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(transfur), attackerDirect, attackerIndirect);
    }

    static <T extends Entity> @NotNull DamageSource latexSolvent(@NotNull Level level, @Nullable T attackerDirect){
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(solvent), attackerDirect, null);
    }

    private static @NotNull ResourceKey<DamageType> create(String str){
        return Utils.resourceKey(Registries.DAMAGE_TYPE, str);
    }

    static boolean checkTarget(Entity target){
        return target instanceof LivingEntity entity && entity.getCapability(TransfurCapability.CAPABILITY) != null
                && (!(target instanceof Player player) || (!TransfurManager.isTransfurred(player) && !TransfurManager.isBeingTransfurred(player)));
    }
}