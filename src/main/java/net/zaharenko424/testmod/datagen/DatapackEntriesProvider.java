package net.zaharenko424.testmod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.zaharenko424.testmod.TestMod;
import net.zaharenko424.testmod.TransfurDamageSource;
import net.zaharenko424.testmod.datagen.worldgen.ConfiguredFeatureProvider;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
@ParametersAreNonnullByDefault
public class DatapackEntriesProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ConfiguredFeatureProvider::bootstrap)
            .add(Registries.DAMAGE_TYPE, DatapackEntriesProvider::damageType);

    public DatapackEntriesProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(TestMod.MODID));
    }

    private static void damageType(BootstapContext<DamageType> context){
        context.register(TransfurDamageSource.solvent,new DamageType("solvent", DamageScaling.ALWAYS,0));
        context.register(TransfurDamageSource.transfur,new DamageType("transfur",DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,0.1f));
    }
}