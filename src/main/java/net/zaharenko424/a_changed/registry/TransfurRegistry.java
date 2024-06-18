package net.zaharenko424.a_changed.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.transfurSystem.Gender;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.*;
import net.zaharenko424.a_changed.util.Latex;

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.AChanged.resourceLoc;

public class TransfurRegistry {

    public static final DeferredRegister<TransfurType> TRANSFUR_TYPES = DeferredRegister.create(resourceLoc("transfur_registry"), MODID);
    public static final Registry<TransfurType> TRANSFUR_REGISTRY = TRANSFUR_TYPES.makeRegistry(builder->{});

    //Transfur types
    public static final DeferredHolder<TransfurType, BeiFeng> BEI_FENG_TF = TRANSFUR_TYPES
            .register("bei_feng", ()-> new BeiFeng(resourceLoc("bei_feng")));

    public static final DeferredHolder<TransfurType, LatexWolf> BENIGN_TF = TRANSFUR_TYPES
            .register("benign", ()-> new LatexWolf(TransfurType.Properties
                    .of(resourceLoc("benign"), Latex.WHITE).organic(true).onTransfur(entity -> {//TODO replace with unremovable effects
                        entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, -1, 0, false, false, false));
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 2, false, false, false));
                    }).onUnTransfur(entity -> {//OR move to TransfurEvent as an exception & check for this TF
                        entity.removeEffect(MobEffects.BLINDNESS);
                        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    }).colors(-14211289, -14803426)));

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