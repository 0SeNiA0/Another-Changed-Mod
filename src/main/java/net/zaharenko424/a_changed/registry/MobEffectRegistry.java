package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.effects.FreshAirEffect;
import net.zaharenko424.a_changed.effects.LatexSolventEffect;
import net.zaharenko424.a_changed.effects.UnTransfurEffect;

import static net.zaharenko424.a_changed.AChanged.MODID;

public class MobEffectRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT,MODID);

    public static final DeferredHolder<MobEffect, MobEffect> FRESH_AIR = EFFECTS
            .register("fresh_air", FreshAirEffect::new);
    public static final DeferredHolder<MobEffect, LatexSolventEffect> LATEX_SOLVENT = EFFECTS
            .register("latex_solvent", LatexSolventEffect::new);
    public static final DeferredHolder<MobEffect, UnTransfurEffect> UNTRANSFUR = EFFECTS
            .register("untransfur", UnTransfurEffect::new);
}