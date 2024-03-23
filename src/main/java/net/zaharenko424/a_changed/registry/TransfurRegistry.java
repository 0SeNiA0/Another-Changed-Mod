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

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.AChanged.resourceLoc;

public final class TransfurRegistry {

    public static final DeferredRegister<AbstractTransfurType> TRANSFUR_TYPES = DeferredRegister.create(resourceLoc("transfur_registry"), MODID);
    public static final Registry<AbstractTransfurType> TRANSFUR_REGISTRY = TRANSFUR_TYPES.makeRegistry(builder->{});

    //Transfur types
    public static final DeferredHolder<AbstractTransfurType, BeiFeng> BEI_FENG_TF = TRANSFUR_TYPES
            .register("bei_feng", ()-> new BeiFeng(AbstractTransfurType.Properties.of(resourceLoc("bei_feng"))));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> BENIGN_TF = TRANSFUR_TYPES
            .register("benign", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("benign")).organic(true).onTransfur(entity -> {//TODO replace with unremovable effects
                        entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, -1, 0, false, false, false));
                        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, -1, 2, false, false, false));
                    }).onUnTransfur(entity -> {//OR move to TransfurEvent as an exception & check for this TF
                        entity.removeEffect(MobEffects.BLINDNESS);
                        entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
                    })));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> DARK_LATEX_WOLF_F_TF = TRANSFUR_TYPES
            .register("dark_latex_wolf_female", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("dark_latex_wolf_female")).gender(Gender.FEMALE)));
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> DARK_LATEX_WOLF_M_TF = TRANSFUR_TYPES
            .register("dark_latex_wolf_male", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("dark_latex_wolf_male")).gender(Gender.MALE)));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> GAS_WOLF_TF = TRANSFUR_TYPES
            .register("gas_wolf", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("gas_wolf")).poseSize(Pose.STANDING, EntityDimensions.scalable(.6f,1.85f)).organic(true)));

    public static final DeferredHolder<AbstractTransfurType, HypnoCat> HYPNO_CAT_TF = TRANSFUR_TYPES
            .register("hypno_cat", ()-> new HypnoCat(AbstractLatexCat.CatProperties.of(resourceLoc("hypno_cat"))));

    public static final DeferredHolder<AbstractTransfurType, LatexShark> LATEX_SHARK_F_TF = TRANSFUR_TYPES
            .register("latex_shark_female", ()-> new LatexShark(AbstractWaterLatex.WaterLatexProperties
                    .of(resourceLoc("latex_shark_female")).gender(Gender.FEMALE)));
    public static final DeferredHolder<AbstractTransfurType, LatexShark> LATEX_SHARK_M_TF = TRANSFUR_TYPES
            .register("latex_shark_male", ()-> new LatexShark(AbstractWaterLatex.WaterLatexProperties
                    .of(resourceLoc("latex_shark_male")).gender(Gender.MALE)));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> PURE_WHITE_LATEX_WOLF_TF = TRANSFUR_TYPES
            .register("pure_white_latex_wolf", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("pure_white_latex_wolf"))));
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> WHITE_LATEX_WOLF_F_TF = TRANSFUR_TYPES
            .register("white_latex_wolf_female", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("white_latex_wolf_female")).gender(Gender.FEMALE)));
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> WHITE_LATEX_WOLF_M_TF = TRANSFUR_TYPES
            .register("white_latex_wolf_male", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("white_latex_wolf_male")).gender(Gender.MALE)));
}