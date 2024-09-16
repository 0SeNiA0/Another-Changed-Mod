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
import static net.zaharenko424.a_changed.AChanged.resourceLoc;

public class MobEffectRegistry {

    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, MODID);

    public static final DeferredHolder<MobEffect, UnRemovableRegen> ASSIMILATION_BUFF = EFFECTS
            .register("assimilation_buff", ()-> (UnRemovableRegen) new UnRemovableRegen(MobEffectCategory.BENEFICIAL, 2, 0)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, resourceLoc("assim_attack_damage"), 6, AttributeModifier.Operation.ADD_VALUE)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, resourceLoc("assim_movement_speed"), .4, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Attributes.MAX_HEALTH, resourceLoc("assim_max_health"), 16, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, ElectrocutedEffect> ELECTROCUTED_DEBUFF = EFFECTS
            .register("electrocuted", ElectrocutedEffect::new);
    public static final DeferredHolder<MobEffect, UnRemovableEffect> FRESH_AIR = EFFECTS
            .register("fresh_air", ()-> new UnRemovableEffect(MobEffectCategory.BENEFICIAL, 15597018));
    public static final DeferredHolder<MobEffect, FriendlyGrabEffect> FRIENDLY_GRAB = EFFECTS
            .register("friendly_grab", FriendlyGrabEffect::new);
    public static final DeferredHolder<MobEffect, UnRemovableEffect> GRAB_COOLDOWN = EFFECTS
            .register("grab_cooldown", ()-> new UnRemovableEffect(MobEffectCategory.NEUTRAL, 0));
    public static final DeferredHolder<MobEffect, UnRemovableEffect> GRABBED_DEBUFF = EFFECTS
            .register("grabbed_debuff", ()-> (UnRemovableEffect) new UnRemovableEffect(MobEffectCategory.HARMFUL, 0)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, resourceLoc("grabbed_attack_damage"), -20, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, UnRemovableEffect> HOLDING_DEBUFF = EFFECTS
            .register("holding_debuff", ()-> (UnRemovableEffect) new UnRemovableEffect(MobEffectCategory.NEUTRAL, 0)
                .addAttributeModifier(Attributes.MOVEMENT_SPEED, resourceLoc("holding_movement_speed"), -.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                .addAttributeModifier(Attributes.ATTACK_DAMAGE, resourceLoc("holding_attack_damage"), -12, AttributeModifier.Operation.ADD_VALUE));
    public static final DeferredHolder<MobEffect, LatexSolventEffect> LATEX_SOLVENT = EFFECTS
            .register("latex_solvent", LatexSolventEffect::new);
    public static final DeferredHolder<MobEffect, UnTransfurEffect> UNTRANSFUR = EFFECTS
            .register("untransfur", UnTransfurEffect::new);
}