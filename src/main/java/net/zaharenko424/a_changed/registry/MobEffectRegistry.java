package net.zaharenko424.a_changed.registry;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.effects.*;

import static net.zaharenko424.a_changed.AChanged.MODID;

public class MobEffectRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);

    public static final DeferredHolder<MobEffect, UnRemovableRegen> ASSIMILATION_BUFF = EFFECTS
            .register("assimilation_buff", ()-> (UnRemovableRegen) new UnRemovableRegen(MobEffectCategory.BENEFICIAL, 2, 0)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, "4e4c9021-3c8d-497d-ad86-87c3dc248c37", 6, AttributeModifier.Operation.ADDITION)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, "10cc7158-9de0-4649-ad20-6536794cfc4e", .4, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.MAX_HEALTH, "63367a57-f186-436c-94fb-6f87f2d208ce", 16, AttributeModifier.Operation.ADDITION));
    public static final DeferredHolder<MobEffect, InvisibleEffect> FRESH_AIR = EFFECTS
            .register("fresh_air", ()-> new InvisibleEffect(MobEffectCategory.BENEFICIAL, 15597018));
    public static final DeferredHolder<MobEffect, FriendlyGrabEffect> FRIENDLY_GRAB = EFFECTS
            .register("friendly_grab", FriendlyGrabEffect::new);
    public static final DeferredHolder<MobEffect, InvisibleEffect> GRAB_COOLDOWN = EFFECTS
            .register("grab_cooldown", ()-> new InvisibleEffect(MobEffectCategory.NEUTRAL, 0));
    public static final DeferredHolder<MobEffect, UnRemovableEffect> GRABBED_DEBUFF = EFFECTS
            .register("grabbed_debuff", ()-> (UnRemovableEffect) new UnRemovableEffect(MobEffectCategory.HARMFUL, 0)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, "d6ed1398-3f98-4284-aa37-1d3f79086945", -20, AttributeModifier.Operation.ADDITION));
    public static final DeferredHolder<MobEffect, UnRemovableEffect> HOLDING_DEBUFF = EFFECTS
            .register("holding_debuff", ()-> (UnRemovableEffect) new UnRemovableEffect(MobEffectCategory.NEUTRAL, 0)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, "2d5cba29-8b3b-4309-9baf-537d0236bdee", -.25, AttributeModifier.Operation.MULTIPLY_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, "9fce3f0a-044b-409f-8b6b-1dbfd6c85d89", -12, AttributeModifier.Operation.ADDITION));
    public static final DeferredHolder<MobEffect, LatexSolventEffect> LATEX_SOLVENT = EFFECTS
            .register("latex_solvent", LatexSolventEffect::new);
    public static final DeferredHolder<MobEffect, UnTransfurEffect> UNTRANSFUR = EFFECTS
            .register("untransfur", UnTransfurEffect::new);
}