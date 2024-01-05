package net.zaharenko424.a_changed.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.AbstractTransfurType;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.BeiFeng;
import net.zaharenko424.a_changed.transfurSystem.transfurTypes.LatexWolf;

import static net.zaharenko424.a_changed.AChanged.MODID;
import static net.zaharenko424.a_changed.AChanged.resourceLoc;

public final class TransfurRegistry {

    public static final DeferredRegister<AbstractTransfurType> TRANSFUR_TYPES = DeferredRegister.create(resourceLoc("atransfur_registry"),MODID);
    public static final Registry<AbstractTransfurType> TRANSFUR_REGISTRY = TRANSFUR_TYPES.makeRegistry(builder->{});

    //Transfur types
    public static final DeferredHolder<AbstractTransfurType, BeiFeng> BEI_FENG_TF = TRANSFUR_TYPES
            .register("bei_feng", ()-> new BeiFeng(AbstractTransfurType.Properties
                    .of(resourceLoc("bei_feng")).maxHealthModifier(4)));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> DARK_LATEX_WOLF_M_TF = TRANSFUR_TYPES
            .register("dark_latex_wolf_male", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("dark_latex_wolf_male")).maxHealthModifier(4)));
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> DARK_LATEX_WOLF_F_TF = TRANSFUR_TYPES
            .register("dark_latex_wolf_female", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("dark_latex_wolf_female")).gender(false).maxHealthModifier(4)));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> GAS_WOLF_TF = TRANSFUR_TYPES
            .register("gas_wolf", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("gas_wolf")).poseSize(Pose.STANDING, EntityDimensions.scalable(.6f,1.85f)).maxHealthModifier(4).organic(true)));

    public static final DeferredHolder<AbstractTransfurType, LatexWolf> WHITE_LATEX_WOLF_M_TF = TRANSFUR_TYPES
            .register("white_latex_wolf_male", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("white_latex_wolf_male")).maxHealthModifier(4)));
    public static final DeferredHolder<AbstractTransfurType, LatexWolf> WHITE_LATEX_WOLF_F_TF = TRANSFUR_TYPES
            .register("white_latex_wolf_female", ()-> new LatexWolf(AbstractTransfurType.Properties
                    .of(resourceLoc("white_latex_wolf_female")).gender(false).maxHealthModifier(4)));
}