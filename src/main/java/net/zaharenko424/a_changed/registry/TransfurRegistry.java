package net.zaharenko424.a_changed.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.*;
import net.zaharenko424.a_changed.util.Latex;

import static net.zaharenko424.a_changed.AChanged.*;

public class TransfurRegistry {

    public static final DeferredRegister<TransfurType> TRANSFUR_TYPES = DeferredRegister.create(resourceLoc("transfur_registry"), MODID);
    public static final Registry<TransfurType> TRANSFUR_REGISTRY = TRANSFUR_TYPES.makeRegistry(builder->{});

    //Transfur types
    public static final DeferredHolder<TransfurType, BeiFeng> BEI_FENG_TF = TRANSFUR_TYPES
            .register("bei_feng", ()-> new BeiFeng(resourceLoc("bei_feng")));

    public static final DeferredHolder<TransfurType, LatexWolf> BENIGN_TF = TRANSFUR_TYPES
            .register("benign", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("benign"), Latex.WHITE).organic(true)
                    .addModifier(Attributes.MOVEMENT_SPEED, "benign_slowdown", -.3, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .onTransfur(entity -> {
                        MobEffectInstance unremovableBlindness = new MobEffectInstance(MobEffects.BLINDNESS, -1, 0, false, false, false);
                        unremovableBlindness.getCures().clear();
                        entity.addEffect(unremovableBlindness);
                    }).onUnTransfur(entity -> entity.removeEffect(MobEffects.BLINDNESS)).colors(-14211289, -14803426)));

    public static final DeferredHolder<TransfurType, LatexWolf> DARK_LATEX_WOLF_F_TF = TRANSFUR_TYPES
            .register("dark_latex_wolf_female", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("dark_latex_wolf_female"), Latex.DARK).gender(Gender.FEMALE).colors(-13686230, -14146010)));
    public static final DeferredHolder<TransfurType, LatexWolf> DARK_LATEX_WOLF_M_TF = TRANSFUR_TYPES
            .register("dark_latex_wolf_male", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("dark_latex_wolf_male"), Latex.DARK).gender(Gender.MALE).colors(-13686230, -14146010)));

    public static final DeferredHolder<TransfurType, LatexWolf> GAS_WOLF_TF = TRANSFUR_TYPES
            .register("gas_wolf", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("gas_wolf"), Latex.WHITE).poseSize(Pose.STANDING, EntityDimensions.scalable(.6f,1.85f))
                    .colors(-13686230, -14146010).organic(true)));

    public static final DeferredHolder<TransfurType, HypnoCat> HYPNO_CAT_TF = TRANSFUR_TYPES
            .register("hypno_cat", ()-> new HypnoCat(resourceLoc("hypno_cat")));

    public static final DeferredHolder<TransfurType, LatexShark> LATEX_SHARK_F_TF = TRANSFUR_TYPES
            .register("latex_shark_female", ()-> new LatexShark(resourceLoc("latex_shark_female"), Gender.FEMALE));
    public static final DeferredHolder<TransfurType, LatexShark> LATEX_SHARK_M_TF = TRANSFUR_TYPES
            .register("latex_shark_male", ()-> new LatexShark(resourceLoc("latex_shark_male"), Gender.MALE));

    public static final DeferredHolder<TransfurType, LatexWolf> PURE_WHITE_LATEX_WOLF_TF = TRANSFUR_TYPES
            .register("pure_white_latex_wolf", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("pure_white_latex_wolf"), Latex.WHITE)));

    public static final DeferredHolder<TransfurType, SnowLeopard> SNOW_LEOPARD_F_TF = TRANSFUR_TYPES
            .register("snow_leopard_female", ()-> new SnowLeopard(resourceLoc("snow_leopard_female"), Gender.FEMALE));
    public static final DeferredHolder<TransfurType, SnowLeopard> SNOW_LEOPARD_M_TF = TRANSFUR_TYPES
            .register("snow_leopard_male", ()-> new SnowLeopard(resourceLoc("snow_leopard_male"), Gender.MALE));

    public static final DeferredHolder<TransfurType, Special> SPECIAL_TF = TRANSFUR_TYPES
            .register("special", ()-> new Special(TransfurType.Properties
                    .of(resourceLoc("special"), Latex.WHITE)));

    public static final DeferredHolder<TransfurType, LatexWolf> WHITE_LATEX_WOLF_F_TF = TRANSFUR_TYPES
            .register("white_latex_wolf_female", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("white_latex_wolf_female"), Latex.WHITE).gender(Gender.FEMALE)));
    public static final DeferredHolder<TransfurType, LatexWolf> WHITE_LATEX_WOLF_M_TF = TRANSFUR_TYPES
            .register("white_latex_wolf_male", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("white_latex_wolf_male"), Latex.WHITE).gender(Gender.MALE)));
}