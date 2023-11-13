package net.zaharenko424.testmod;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface TransfurDamageSource {

    ResourceKey<DamageType> key=ResourceKey.create(Registries.DAMAGE_TYPE,new ResourceLocation(TestMod.MODID,"transfur"));

    @Contract("_, _ -> new")
    static <T extends Entity,E extends LivingEntity> @NotNull DamageSource transfur(@NotNull T target, @NotNull E entity){
        return new DamageSource(entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(key),target,entity);
    }
}