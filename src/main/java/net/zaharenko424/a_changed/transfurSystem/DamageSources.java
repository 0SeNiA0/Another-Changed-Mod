package net.zaharenko424.a_changed.transfurSystem;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.zaharenko424.a_changed.AChanged;
import net.zaharenko424.a_changed.capability.TransfurCapability;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface DamageSources {

    ResourceKey<DamageType> transfur = ResourceKey.create(Registries.DAMAGE_TYPE,new ResourceLocation(AChanged.MODID,"transfur"));
    ResourceKey<DamageType> solvent = ResourceKey.create(Registries.DAMAGE_TYPE,new ResourceLocation(AChanged.MODID,"latex_solvent"));

    @Contract("_, _ -> new")
    static <T extends Entity,E extends LivingEntity> @NotNull DamageSource transfur(@NotNull T target, @Nullable E entity){
        return new DamageSource(target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(transfur),target,entity);
    }

    @Contract("_ -> new")
    static <T extends Entity> @NotNull DamageSource latexSolvent(@NotNull T target){
        return new DamageSource(target.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(solvent),target,null);
    }

    static boolean checkTarget(Entity target){
        return target instanceof LivingEntity entity && entity.getCapability(TransfurCapability.CAPABILITY).isPresent()
                && (!(target instanceof Player player) || (!TransfurManager.isTransfurred(player) && !TransfurManager.isBeingTransfurred(player)));
    }
}